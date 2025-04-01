package app.moz.smartdev.configs;

import app.moz.smartdev.entity.User;
import app.moz.smartdev.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

   private final  JwtService jwtService;

   private final UserDetailsService userDetailsService;

   private final UserRepository userRepository;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        String requestURI = request.getRequestURI();

        log.info("Processing request for URI: {}", requestURI);
        if (requestURI.startsWith("/api/v1/auth/") || requestURI.startsWith("/api/v1/user/") ||
                requestURI.startsWith("/api/v1/integrations/") || requestURI.startsWith("/api/v1/new/clients/") ||
                requestURI.startsWith("/api/github/") || requestURI.startsWith("/api/v1/auth/change-password") ||
                requestURI.startsWith("/oauth2/authorization/github") || requestURI.startsWith("/v2/api-docs") ||
                requestURI.startsWith("/v3/api-docs/") || requestURI.startsWith("/swagger-resources/") ||
                requestURI.startsWith("/swagger-ui.html") || requestURI.startsWith("/swagger-ui/") ||
                requestURI.startsWith("/webjars/") || requestURI.startsWith("/swagger-ui/index.html")) {
            log.info("Skipping authentication for URI: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("No Bearer token found for URI: {}", requestURI);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: No Bearer token found");
            return;
        }
        jwt = authHeader.substring(7);

        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            Optional<User> userOptional = userRepository.findByUsername(userEmail);

            if (userOptional.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            User user = userOptional.get();

            if (jwtService.isTokenValid(jwt, user)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
