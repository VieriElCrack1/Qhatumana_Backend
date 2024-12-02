package pe.edu.cibertec.qhatumana.controller.backoffice;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.qhatumana.model.bd.EnlaceMenu;
import pe.edu.cibertec.qhatumana.model.bd.Usuario;
import pe.edu.cibertec.qhatumana.model.dto.request.auth.AuthCreateUserRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.auth.AuthLoginRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.auth.AuthResponse;
import pe.edu.cibertec.qhatumana.service.Impl.UsuarioService;
import pe.edu.cibertec.qhatumana.service.interfaces.IEnlaceMenuService;
import pe.edu.cibertec.qhatumana.service.interfaces.IUsuarioService;
import pe.edu.cibertec.qhatumana.util.jwt.JwtUtil;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final IUsuarioService usuarioService;
    private final UsuarioService userDetaislService;
    private final IEnlaceMenuService enlaceMenuService;
    private final JwtUtil jwtUtil;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> menu(Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        if (auth != null) {
            String username = auth.getName();
            Usuario u = usuarioService.iniciarSesion(username);

            List<EnlaceMenu> lista = enlaceMenuService.traerEnlaceUsuario(u.getRol().getIdrol());

            Map<Integer, Map<String, Object>> menuMap = new HashMap<>();

            for (EnlaceMenu enlaceMenu : lista) {
                if (enlaceMenu.getPadreid() == null) {
                    Map<String, Object> menuItem = new HashMap<>();
                    menuItem.put("padre", enlaceMenu);
                    menuItem.put("submenus", new ArrayList<EnlaceMenu>());
                    menuMap.put(enlaceMenu.getIdenlace(), menuItem);
                }
            }

            for (EnlaceMenu enlaceMenu : lista) {
                if (enlaceMenu.getPadreid() != null) {
                    Map<String, Object> parent = menuMap.get(enlaceMenu.getPadreid().getIdenlace());
                    if (parent != null) {
                        List<EnlaceMenu> submenus = (List<EnlaceMenu>) parent.get("submenus");
                        submenus.add(enlaceMenu);
                    }
                }
            }

            response.put("ENLACES", menuMap);
            response.put("USUARIO", u);

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLoginRequest userRequest) {
        try {
            AuthResponse authResponse = userDetaislService.loginUser(userRequest);
            String refreshToken = jwtUtil.createRefreshToken(authResponse.username());
            authResponse = new AuthResponse(
                    authResponse.username(),
                    authResponse.message(),
                    authResponse.jwt(),
                    authResponse.status(),
                    refreshToken
            );
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println("Error en loguearse : " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody AuthCreateUserRequest createUserRequest) {
        try {
            AuthResponse authResponse = userDetaislService.crearteUser(createUserRequest);

            String refreshToken = jwtUtil.createRefreshToken(authResponse.username());
            authResponse = new AuthResponse(
                    authResponse.username(),
                    authResponse.message(),
                    authResponse.jwt(),
                    authResponse.status(),
                    refreshToken
            );
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println("Error en loguearse : " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
        try {
            String refreshToken = request.getHeader("X-Refresh-Token");

            if (refreshToken == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthResponse("", "Refresh token no proporcionado", "", false, ""));
            }

            DecodedJWT decodedJWT = jwtUtil.validateToken(refreshToken);
            String username = jwtUtil.extractUsername(decodedJWT);
            String newAccessToken = jwtUtil.createToken(new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList())); //nuevo token de acceso
            String newRefreshToken = jwtUtil.createRefreshToken(username);//nuevo refresh token

            AuthResponse authResponse = new AuthResponse(username, "Tokens refreshed", newAccessToken,true, newRefreshToken);

            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            System.out.println("Error al refrescar el token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**Cerrar Sesion y borrar las coookies de auth**/
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok().build();
    }

}
