package notesmaking.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("Configuring CORS mappings...");
        registry.addMapping("/**")
                .allowedOrigins("https://eclectic-gaufre-6ce38f.netlify.app/", // Add your deployed frontend URL
                        "http://localhost:5174","https://eclectic-gaufre-6ce38f.netlify.app")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }

}

