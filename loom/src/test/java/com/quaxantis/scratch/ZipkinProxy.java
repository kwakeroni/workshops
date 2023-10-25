package com.quaxantis.scratch;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.Span.KindEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public final class ZipkinProxy {
    static final ScopedValue<RequestSpan> SPAN = ScopedValue.newInstance();

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
            try (RequestSpan client = spanCallTo(serviceName, methodName)) {
                Thread.sleep(100);
//                try (RequestSpan server = client.onServer()) {
                return ScopedValue.callWhere(SPAN, client, () -> callRealMethod(invocation));
//                }
            }
        } else {
            return callRealMethod(invocation);
        }
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

    private RequestSpan spanCallTo(String serviceName, String methodName) {
        if (SPAN.isBound()) {
            return SPAN.get().spawn(zipkinClient, KindEnum.CLIENT, serviceName, methodName);
        } else {
            System.out.println(STR. "Creating new trace for \{ serviceName }:\{ methodName }" );
            return new RequestSpan(null, KindEnum.CLIENT, serviceName, methodName, zipkinClient);
        }
    }

    public static <T> Supplier<T> propagateAsyncTrace(Supplier<T> supplier) {
        RequestSpan currentSpan = SPAN.get();
        return () -> ScopedValue.getWhere(SPAN, currentSpan, supplier);
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Span {


    }
}
