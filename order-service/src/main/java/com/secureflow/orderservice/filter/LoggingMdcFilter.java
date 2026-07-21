package com.secureflow.orderservice.filter;

import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingMdcFilter extends OncePerRequestFilter {

    private final Tracer tracer;

    public LoggingMdcFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {

            MDC.put("service", "order-service");

            if (tracer.currentSpan() != null) {
                MDC.put("traceId",
                        tracer.currentSpan().context().traceId());
            }

            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            if (authentication != null &&
                    authentication.isAuthenticated()) {

                MDC.put("user", authentication.getName());
            } else {
                MDC.put("user", "anonymous");
            }

            filterChain.doFilter(request, response);

        } finally {

            MDC.clear();

        }
    }
}
