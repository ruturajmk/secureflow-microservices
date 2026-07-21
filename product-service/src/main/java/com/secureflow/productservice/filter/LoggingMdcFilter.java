package com.secureflow.productservice.filter;

import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingMdcFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(LoggingMdcFilter.class);

    private final Tracer tracer;

    @Value("${spring.application.name}")
    private String applicationName;

    public LoggingMdcFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        try {

            MDC.put("service", applicationName);

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

            MDC.put("method", request.getMethod());
            MDC.put("uri", request.getRequestURI());
            MDC.put("clientIp", request.getRemoteAddr());

            filterChain.doFilter(request, response);

        } finally {

            MDC.put("status",
                    String.valueOf(response.getStatus()));

            MDC.put("duration",
                    (System.currentTimeMillis() - startTime) + " ms");

            log.info("HTTP Request Finished");

            MDC.clear();

        }
    }
}
