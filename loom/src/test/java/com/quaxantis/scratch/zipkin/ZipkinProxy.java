package com.quaxantis.scratch.zipkin;

import com.quaxantis.scratch.zipkin.TracedRequest.ClientRequest;
import com.quaxantis.scratch.zipkin.TracedRequest.ServerRequest;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.DefaultApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public final class ZipkinProxy {
    private static final Logger log = LoggerFactory.getLogger(ZipkinProxy.class);

    static final ScopedValue<ServerRequest> SPAN = ScopedValue.newInstance();

    private final DefaultApi zipkinClient;

    public ZipkinProxy() {
        this(new DefaultApi(new ApiClient()));
    }

    public ZipkinProxy(DefaultApi zipkinClient) {
        this.zipkinClient = zipkinClient;
    }

    public <T> T proxy(T service) {
        return proxy(service, invocation ->
                wrapSpan(service.getClass().getSimpleName(), invocation));
    }

    private <T> T proxy(T service, Answer<?> answer) {
        @SuppressWarnings("unchecked")
        Class<T> serviceClass = (Class<T>) service.getClass();
        return serviceClass.cast(
                mock(serviceClass, withSettings()
                        .spiedInstance(service)
                        .defaultAnswer(answer)));
    }

    private Object wrapSpan(String serviceName, InvocationOnMock invocation) throws Throwable {
        String methodName = invocation.getMethod().getName();
        if (invocation.getMethod().isAnnotationPresent(Span.class)) {
            try (ClientRequest clientRequest = sendClientRequest(serviceName, methodName)) {
                randomLatency();
                try (ServerRequest server = ServerRequest.receive(clientRequest)) {
                    return ScopedValue.callWhere(SPAN, server, () -> callRealMethod(invocation));
                } finally {
                    randomLatency();
                }
            }
        } else {
            return callRealMethod(invocation);
        }
    }

    private void randomLatency() throws InterruptedException {
        long delay = (long) (100d * Math.random()) + 50;
        Thread.sleep(delay);
    }

    private Object callRealMethod(InvocationOnMock invocation) throws Exception {
        try {
            return invocation.callRealMethod();
        } catch (Exception | Error e) {
            throw e;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private ClientRequest sendClientRequest(String serviceName, String methodName) {
        if (SPAN.isBound()) {
            return SPAN.get().sendRequest(serviceName, methodName);
        } else {
            log.debug(STR. "Creating new trace for \{ serviceName }:\{ methodName }" );
            return TracedRequest.sendRootRequest(serviceName, methodName, zipkinClient);
        }
    }

    public static <T> Supplier<T> propagateAsyncTrace(Supplier<T> supplier) {
        ServerRequest currentSpan = SPAN.get();
        return () -> ScopedValue.getWhere(SPAN, currentSpan, supplier);
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Span {
    }
}
