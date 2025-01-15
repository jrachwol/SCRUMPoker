package hasebo.scrumpoker.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class CsrfLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(CsrfLoggingFilter.class.getName());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        String receivedToken = request.getHeader("X-XSRF-TOKEN"); // Przy REST korzystamy z nagłówka
        if (receivedToken == null) {
            receivedToken = request.getParameter("_csrf"); // Dla aplikacji HTML
        }

        if (csrfToken != null) {
            logger.info("Expected CSRF Token: " + csrfToken.getToken());
        } else {
            logger.warning("Expected CSRF Token is null!");
        }

        if (receivedToken != null) {
            logger.info("Received CSRF Token: " + receivedToken);
        } else {
            logger.warning("Received CSRF Token is missing!");
        }

        filterChain.doFilter(request, response);
    }
}