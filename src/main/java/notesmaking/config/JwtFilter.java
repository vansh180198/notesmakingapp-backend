package notesmaking.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import notesmaking.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private static final String[] PUBLIC_ENDPOINTS = {"/users/login", "/users/register"};
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String INVALID_TOKEN_MESSAGE = "Invalid or expired token";
    private static final String NO_TOKEN_MESSAGE = "No token provided";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request Headers: " + Collections.list(request.getHeaderNames()));



        // Skip public endpoints
        for (String endpoint : PUBLIC_ENDPOINTS) {
            if (requestUri.startsWith(endpoint)) {
                logger.debug("Skipping JwtFilter for public endpoint: {}", requestUri);
                filterChain.doFilter(request, response);
                return;
            }
        }

        // Handle Server-Sent Events (SSE) requests
        if (requestUri.startsWith("/notes/stream") && isSseRequest(request)) {
            logger.debug("SSE request detected for URI: {}", requestUri);
            String token = extractTokenFromHeaderOrQuery(request);
            if (validateAndAuthenticateToken(token)) {
                filterChain.doFilter(request, response); // Proceed with SSE
            } else {
                handleUnauthorizedResponse(response, INVALID_TOKEN_MESSAGE);
            }
            return;
        }

        // Handle other requests
        String token = extractTokenFromHeader(request);
        if (validateAndAuthenticateToken(token)) {
            filterChain.doFilter(request, response);
        } else {
            handleUnauthorizedResponse(response, NO_TOKEN_MESSAGE);
        }
    }

    private boolean isSseRequest(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        return "text/event-stream".equalsIgnoreCase(acceptHeader);
    }

    private String extractTokenFromHeaderOrQuery(HttpServletRequest request) {
        String token = request.getParameter("token"); // For SSE: token in query params
        if (token == null) {
            token = extractTokenFromHeader(request); // Fallback to Authorization header
        }
        return token;
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private boolean validateAndAuthenticateToken(String token) {
        try {
            if (token != null && !token.isEmpty()) {
                String email = JwtUtil.extractUserEmail(token);
                if (email != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error(INVALID_TOKEN_MESSAGE, e);
        }
        return false;
    }

    private void handleUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
