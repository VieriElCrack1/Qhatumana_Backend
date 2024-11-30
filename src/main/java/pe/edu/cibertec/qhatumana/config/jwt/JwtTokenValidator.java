package pe.edu.cibertec.qhatumana.config.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.edu.cibertec.qhatumana.util.jwt.JwtUtil;

import java.io.IOException;
import java.util.Collection;

//esta clase es para que siempre se valide y ejecute ese token
//es decir; que siempre tengo que enviar un token

@AllArgsConstructor
@Component
public class JwtTokenValidator extends OncePerRequestFilter {

    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(jwtToken != null && jwtToken.startsWith("Bearer ")) {
            //validar token
            jwtToken = jwtToken.substring(7);
            try{
                DecodedJWT decodedJWT = jwtUtil.validateToken(jwtToken);

                String username = jwtUtil.extractUsername(decodedJWT);
                String stringAuthorities = jwtUtil.getSpecificClaim(decodedJWT, "authorities").asString();

                Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                        .commaSeparatedStringToAuthorityList(stringAuthorities);
                SecurityContext context = SecurityContextHolder.getContext();
                Authentication authentication = new
                        UsernamePasswordAuthenticationToken(username, null, authorities);
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }catch (Exception e) {
                String refreshToken = request.getHeader("X-Refresh-Token"); //si el token ha expirado

                if (refreshToken != null) {
                    try {
                        DecodedJWT decodedRefreshToken = jwtUtil.validateRefreshToken(refreshToken);
                        String nuevoAccessToken = jwtUtil.generateAccessToken(decodedRefreshToken);

                        response.setHeader("Authorization", "Bearer " + nuevoAccessToken);

                        filterChain.doFilter(request, response);
                        return;
                    } catch (Exception ex) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Invalid refresh token");
                        return;
                    }
                }
            }
        }
        //continua y falla y rechaza la authenticacion si es que no se envia el token
        filterChain.doFilter(request, response);
    }

}
