package shop.me.back.end.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import shop.me.back.end.Service.UserServiceImp;
import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserServiceImp userService;

    private static final List<String> EXCLUDE_PREFIXES = List.of(
            "/login",
            "/register",
            "/admin/register",
            "/account-verification",
            "/otp-request",
            "/password-reset",
            "/api/product/search",

            // SWAGGER
            "/swagger-ui",
            "/swagger-ui/",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/v3/api-docs",
            "/v3/api-docs/",
            "/v3/api-docs/swagger-config",
            "/swagger-resources",
            "/swagger-resources/",
            "/webjars/"
    );
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        for (String prefix : EXCLUDE_PREFIXES) {
            if (path.startsWith(prefix)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // ⭐ NORMAL JWT CHECK
        String auth = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (auth != null && auth.startsWith("Bearer ")) {
            token = auth.substring(7);

            try {
                username = jwtUtil.extractUsernameFromToken(token);
            } catch (Exception ignored) {}
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);

            if (jwtUtil.isTokenValid(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
