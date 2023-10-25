package com.quaxantis.scratch;

import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.Annotation;
import org.openapitools.client.model.Endpoint;
import org.openapitools.client.model.Span;

import java.util.List;
import java.util.UUID;

public sealed abstract class TracedRequest implements AutoCloseable {
    final DefaultApi zipkinClient;
    final Span payload;

    TracedRequest(DefaultApi zipkinClient, Span payload) {
        this.zipkinClient = zipkinClient;
        this.payload = payload;
    }

    @Override
    public final void close() {
        try {
            finishPayload();
            zipkinClient.spansPost(List.of(this.payload));
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    protected abstract void finishPayload();

    /**
     * @see "https://zipkin.io/pages/instrumenting.html#communicating-trace-information"
     */
    private static Span transmit(Span original) {
        return new Span()
                .traceId(original.getTraceId())
                .id(original.getId())
                .parentId(original.getParentId());
    }

    private static Annotation annotation(String value) {
        return new Annotation()
                .timestamp(System.currentTimeMillis())
                .value(value);
    }

    public static ClientRequest sendRootRequest(String serviceName, String name, DefaultApi zipkinClient) {
        return new ClientRequest(null, null, serviceName, name, zipkinClient);
    }

    public static final class ServerRequest extends TracedRequest {
        private ServerRequest(ClientRequest request) {
            super(request.zipkinClient, transmit(request.payload));
            this.payload
                    .localEndpoint(new Endpoint().serviceName(request.getServiceName()))
                    .name(request.getOperationName())
                    .kind(Span.KindEnum.SERVER)
                    .addAnnotationsItem(annotation("sr"));
        }

        public static ServerRequest receive(ClientRequest request) {
            return new ServerRequest(request);
        }

        public ClientRequest sendRequest(String serviceName, String name) {
            return new ClientRequest(this.payload.getTraceId(), this.payload.getId(), serviceName, name, this.zipkinClient);
        }

        @Override
        protected void finishPayload() {
            // Kind.SERVER
            // timestamp is the moment a client request was received. (in v1 "sr")
            // duration is the delay until a response was sent or an error. (in v1 "ss"-"sr")
            // remoteEndpoint is the client. (in v1 "ca")
            this.payload.addAnnotationsItem(annotation("ss"));
        }
    }

    public static final class ClientRequest extends TracedRequest implements AutoCloseable {
        final long start;

        private ClientRequest(String traceId, String parentId, String serviceName, String name, DefaultApi zipkinClient) {
            super(zipkinClient, new Span());
            UUID uuid = UUID.randomUUID();
            String most = Long.toHexString(uuid.getMostSignificantBits()).toLowerCase(); // 16
            String least = Long.toHexString(uuid.getLeastSignificantBits()).toLowerCase(); // 16
            this.start = System.currentTimeMillis();
            this.payload
                    .id(most) // 16
                    .traceId((traceId != null) ? traceId : most + least) // 32
                    .parentId(parentId)
                    .remoteEndpoint(new Endpoint().serviceName(serviceName))
                    .name(name)
                    .kind(Span.KindEnum.CLIENT)
                    .addAnnotationsItem(annotation("cs"));
        }

        private String getServiceName() {
            return this.payload.getRemoteEndpoint().getServiceName();
        }

        private String getOperationName() {
            return this.payload.getName();
        }

        /**
         * @see "https://zipkin.io/zipkin-api/#/default/post_spans"
         * @see "https://zipkin.io/pages/instrumenting.html#timestamps-and-duration"
         */
        @Override
        protected void finishPayload() {
            // Span.timestamp and duration should only be set by the host that started the span.
            // Kind.CLIENT
            // timestamp is the moment a request was sent to the server. (in v1 "cs")
            // duration is the delay until a response or an error was received. (in v1 "cr"-"cs")
            // remoteEndpoint is the server. (in v1 "sa")
            this.payload
                    .timestamp(this.start)
                    .duration(System.currentTimeMillis() - this.start)
                    .addAnnotationsItem(annotation("cr"));

            if (this.payload.getParentId() == null) {
                System.out.printf("-- Finished trace http://127.0.0.1:9411/zipkin/traces/%s%n", this.payload.getTraceId());
            }
        }
    }
}
