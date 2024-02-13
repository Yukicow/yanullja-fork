package com.battlecruisers.yanullja.common.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Slf4j
public class CustomHttpLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(
            request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(
            response);

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        // Continue the filter chain
        filterChain.doFilter(wrappedRequest, wrappedResponse);

        stopWatch.stop();
        log.info("[{}] 총 소요된 시간 : {}s", request.getRequestURI(),
            stopWatch.getTotalTimeMillis());

        // Important: copy the content of the wrapped response to the actual response
        wrappedResponse.copyBodyToResponse();
    }


}
