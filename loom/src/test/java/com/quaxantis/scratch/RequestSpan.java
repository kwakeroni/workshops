package com.quaxantis.scratch;

import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.Endpoint;
import org.openapitools.client.model.Span;

import java.util.List;
import java.util.UUID;

public final class RequestSpan implements AutoCloseable {
    final long start;
    final Span current;
    final DefaultApi zipkinClient;

    RequestSpan(Span parent, Span.KindEnum kind, String serviceName, String name, DefaultApi zipkinClient) {
        UUID uuid = UUID.randomUUID();
        String most = Long.toHexString(uuid.getMostSignificantBits()).toLowerCase(); // 16
        String least = Long.toHexString(uuid.getLeastSignificantBits()).toLowerCase(); // 16
        this.zipkinClient = zipkinClient;
        this.start = System.currentTimeMillis();
        this.current = new Span()
                .id(most) // 16
                .parentId((parent != null) ? parent.getId() : null)
                .traceId((parent != null) ? parent.getTraceId() : most + least) // 32
                .localEndpoint(new Endpoint().serviceName(serviceName))
                .name(name)
                .kind(kind);
    }

    private RequestSpan(RequestSpan original, Span.KindEnum kind) {
        this.zipkinClient = original.zipkinClient;
        this.start = System.currentTimeMillis();
        this.current = new Span()
                .id(original.current.getId())
                .parentId(original.current.getParentId())
                .traceId(original.current.getTraceId())
                .localEndpoint(original.current.getLocalEndpoint())
                .name(original.current.getName())
                .kind(kind);
    }

    @Override
    public void close() {
        try {
            this.current
                    .timestamp(this.start)
                    .duration(System.currentTimeMillis() - this.start);
            if (this.current.getParentId() == null) {
                System.out.printf("-- Finished trace http://127.0.0.1:9411/zipkin/traces/%s%n", this.current.getTraceId());
            }
            zipkinClient.spansPost(List.of(this.current));
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }

    }

    public RequestSpan spawn(DefaultApi zipkinClient, Span.KindEnum kind, String serviceName, String name) {
        return new RequestSpan(this.current, kind, serviceName, name, zipkinClient);
    }
}
