package pe.edu.cibertec.qhatumana.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.qhatumana.model.bd.Rol;
import pe.edu.cibertec.qhatumana.model.bd.Usuario;
import pe.edu.cibertec.qhatumana.model.dto.request.auth.AuthCreateUserRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.auth.AuthLoginRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.auth.AuthResponse;
import pe.edu.cibertec.qhatumana.repository.RolRepository;
import pe.edu.cibertec.qhatumana.repository.UsuarioRepository;
import pe.edu.cibertec.qhatumana.service.interfaces.IUsuarioService;
import pe.edu.cibertec.qhatumana.util.jwt.JwtUtil;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UsuarioService implements IUsuarioService, UserDetailsService {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario u = usuarioRepository.save(usuario);
        usuarioRepository.flush();
        return u;
    }

    @Override
    public int obtenerId() {
        return usuarioRepository.obtenerId();
    }

    @Override
    public Usuario iniciarSesion(String username) {
        return usuarioRepository.iniciarSesion(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Usuario bean = usuarioRepository.iniciarSesion(username);
            if (bean == null) {
                throw new UsernameNotFoundException("Usuario no encontrado");
            }
            if(bean.getEstado() == false) {
                throw new UsernameNotFoundException("El Usuario Esta Deshabilitado");
            }
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority(bean.getRol().getNomrol()));
            return new User(username, bean.getPassword(), authorities);
        } catch (Exception e) {
            System.out.println("Error en : " + e.getMessage());
            throw new UsernameNotFoundException("Error al cargar el usuario", e);
        }
    }

    public AuthResponse crearteUser(AuthCreateUserRequest createUserRequest) {
        AuthResponse auth;
        try {
            String username = createUserRequest.username();
            String password = createUserRequest.password();

            Usuario user = new Usuario();
            user.setNomusuario(createUserRequest.nomuser());
            user.setApeusuario(createUserRequest.apeuser());
            user.setUsername(createUserRequest.username());

            user.setPassword(password);
            user.setReppassword(createUserRequest.password());
            user.setDni(createUserRequest.dni());
            user.setFechareg(LocalDate.now());

            Rol rol = rolRepository.findById(4)
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
            user.setRol(rol);
            user.setEstado(true);

            guardarUsuario(user);

            Usuario savedUser = usuarioRepository.iniciarSesion(user.getUsername());

            Authentication authentication = authenticate(savedUser.getUsername(), createUserRequest.password());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtUtils.createToken(authentication);
            String refreshToken = jwtUtils.createRefreshToken(username);

            auth = new AuthResponse(username, "Usuario Registrado Exitosamente", token, true, refreshToken);
        }catch (Exception e) {
            System.out.println("Error en register : " + e.getMessage());
            throw new UsernameNotFoundException("No se cargo el usuario", e);
        }
        return auth;
    }

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        AuthResponse authResponse = null;
        try {
            String username = authLoginRequest.username();
            String password = authLoginRequest.password();

            Authentication authentication = authenticate(username, password);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtUtils.createToken(authentication);
            String refreshToken = jwtUtils.createRefreshToken(username);

            authResponse = new AuthResponse(username, "Sesión Iniciada Exitosamente", accessToken, true, refreshToken);
        }catch (Exception e) {
            System.out.println("Error en loginUser : " + e.getMessage());
        }
        return authResponse;
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException(String.format("Invalid username or password"));
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }

        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }
}
