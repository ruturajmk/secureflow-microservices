package com.secureflow.apigateway.filter;

import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log =
            LoggerFactory.getLogger(RequestLoggingFilter.class);

    private final Tracer tracer;

    @Value("${spring.application.name}")
    private String applicationName;

    public RequestLoggingFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        long startTime = System.currentTimeMillis();

        String method =
                exchange.getRequest().getMethod().name();

        String uri =
                exchange.getRequest().getURI().getPath();

        String clientIp =
                exchange.getRequest()
                        .getRemoteAddress() != null
                        ? exchange.getRequest()
                          .getRemoteAddress()
                          .getAddress()
                          .getHostAddress()
                        : "UNKNOWN";

        String traceId =
                tracer.currentSpan() != null
                        ? tracer.currentSpan().context().traceId()
                        : "N/A";

        log.info(
                "[{}] REQUEST START | TRACE={} | METHOD={} | URI={} | CLIENT={}",
                applicationName,
                traceId,
                method,
                uri,
                clientIp
        );

        return chain.filter(exchange)
                .doFinally(signal -> {

                    long duration =
                            System.currentTimeMillis() - startTime;

                    int status =
                            exchange.getResponse().getStatusCode() != null
                                    ? exchange.getResponse()
                                      .getStatusCode()
                                      .value()
                                    : 200;

                    log.info(
                            "[{}] REQUEST END | TRACE={} | STATUS={} | TIME={} ms",
                            applicationName,
                            traceId,
                            status,
                            duration
                    );
                });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
