package pe.edu.cibertec.qhatumana.config.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pe.edu.cibertec.qhatumana.util.angular.AngularUtil;

@Configuration
public class AngularWebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**") //donde se permitira el cors
                .allowedOrigins(AngularUtil.URI) //origen
                .allowedMethods("GET","POST","PUT","DELETE","PATCH") //metodos a utilizar
                .allowCredentials(true) //si maneja cookies
                .allowedHeaders("*"); //todos los encabezados
    }
}
