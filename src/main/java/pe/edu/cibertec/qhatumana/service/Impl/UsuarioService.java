package pe.edu.cibertec.qhatumana.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.multipart.MultipartFile;
import pe.edu.cibertec.qhatumana.model.bd.Rol;
import pe.edu.cibertec.qhatumana.model.bd.Usuario;
import pe.edu.cibertec.qhatumana.model.dto.request.auth.AuthCreateUserRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.auth.AuthLoginRequest;
import pe.edu.cibertec.qhatumana.model.dto.request.usuario.UsuarioRequest;
import pe.edu.cibertec.qhatumana.model.dto.response.UsuarioResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.auth.AuthResponse;
import pe.edu.cibertec.qhatumana.model.dto.response.usuario.UsuarioPerfilResponse;
import pe.edu.cibertec.qhatumana.repository.RolRepository;
import pe.edu.cibertec.qhatumana.repository.UsuarioRepository;
import pe.edu.cibertec.qhatumana.service.interfaces.IUsuarioService;
import pe.edu.cibertec.qhatumana.util.exception.handler.ResourceNotFoundException;
import pe.edu.cibertec.qhatumana.util.firebase.storage.ArchivoService;
import pe.edu.cibertec.qhatumana.util.jwt.JwtUtil;

import java.io.File;
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
    private final ArchivoService archivoService;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario u = usuarioRepository.save(usuario);
        usuarioRepository.flush();
        return u;
    }

    @Override
    public Usuario iniciarSesion(String username) {
        return usuarioRepository.iniciarSesion(username);
    }

    @Override
    public UsuarioResponse buscarUsuario(int id) {
        return convertirUsuarioResponse(usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontro el usuario")));
    }

    @Override
    public UsuarioPerfilResponse actualizarUsuarioConImagen(Integer idusuario, MultipartFile file) {
        try {
            Usuario usuario = usuarioRepository.findById(idusuario).orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario"));
            //archivo foto
            String nombreArchivo = file.getOriginalFilename();
            File archivo = archivoService.convertToFile(file);
            String contenido = file.getContentType();
            String imagenUrl = archivoService.uploadFile(archivo, nombreArchivo, contenido, "usuarios");
            usuario.setNomfoto(nombreArchivo);
            usuario.setUrlfoto(imagenUrl);
            usuarioRepository.actualizarUsuarioConImagen(usuario.getUrlfoto(), usuario.getNomfoto(), idusuario);
            return UsuarioPerfilResponse.builder()
                    .message("Guardado Exitosamente")
                    .status(HttpStatus.OK.value())
                    .build();
        }catch (DataAccessException e) {
            return UsuarioPerfilResponse.builder()
                    .message("Error no se pudo acceder a la base de datos.")
                    .messagedescrip(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
        }catch (NullPointerException e) {
            return UsuarioPerfilResponse.builder()
                    .message("Error los valores enviados son vacios.")
                    .messagedescrip(e.getMessage())
                    .status(HttpStatus.NO_CONTENT.value())
                    .build();
        }catch (Exception e) {
            return UsuarioPerfilResponse.builder()
                    .message("Error en modificar el perfil de usuario.")
                    .messagedescrip(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
    }

    @Override
    public UsuarioPerfilResponse actualizarUsuarioSinImagen(UsuarioRequest request) {
        try {
            Usuario usuario = usuarioRepository.findById(request.getIdusuario()).orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario"));
            usuario.setNomusuario(request.getNombre());
            usuario.setApeusuario(request.getApeusuario());
            usuario.setUsername(request.getEmail());
            usuario.setReppassword(request.getPassword());
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
            usuario.setDni(request.getDni());
            usuarioRepository.actualizarUsuarioSinImagen(request.getNombre(), request.getApeusuario(), request.getEmail(), usuario.getPassword(), usuario.getReppassword(), request.getDni(), request.getIdusuario());
            return UsuarioPerfilResponse.builder()
                    .message("Guardado Exitosamente")
                    .status(HttpStatus.OK.value())
                    .build();
        }catch (DataAccessException e) {
            return UsuarioPerfilResponse.builder()
                    .message("Error no se pudo acceder a la base de datos.")
                    .messagedescrip(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
        }catch (NullPointerException e) {
            return UsuarioPerfilResponse.builder()
                    .message("Error los valores enviados son vacios.")
                    .messagedescrip(e.getMessage())
                    .status(HttpStatus.NO_CONTENT.value())
                    .build();
        }catch (Exception e) {
            return UsuarioPerfilResponse.builder()
                    .message("Error en modificar el perfil de usuario sin imagen.")
                    .messagedescrip(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Usuario bean = usuarioRepository.iniciarSesion(username);
            if (bean == null) {
                throw new UsernameNotFoundException("Email no encontrado");
            }
            if(!bean.getEstado()) {
                throw new UsernameNotFoundException("El Usuario Esta Deshabilitado");
            }
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority(bean.getRol().getNomrol()));
            return new User(username, bean.getPassword(), authorities);
        } catch (Exception e) {
            System.out.println("Error en : " + e.getMessage());
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    public AuthResponse crearteUser(AuthCreateUserRequest createUserRequest) {
        AuthResponse auth;
        try {
            Rol rol = rolRepository.findById(4)
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

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
            throw new RuntimeException(e.getMessage());
        }
        return authResponse;
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException(String.format("Usuario y/o password invalido"));
        }

        if(!username.equals(userDetails.getUsername())) {
            throw new BadCredentialsException("Email no encontrado");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Password Incorrecto");
        }

        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }

    private UsuarioResponse convertirUsuarioResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .idusuario(usuario.getIdusuario())
                .nomusuario(usuario.getNomusuario())
                .apeusuario(usuario.getApeusuario())
                .email(usuario.getUsername())
                .dni(usuario.getDni())
                .urlfoto(usuario.getUrlfoto())
                .estado(usuario.getEstado() ? "Activo" : "Inactivo")
                .build();
    }
}
