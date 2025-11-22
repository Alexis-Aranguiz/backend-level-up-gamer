package levelup.backend.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI tiendaOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("API Level-Up Gamer - Backend")
                        .description("API REST para gestión de productos de la tienda Level-Up Gamer")
                        .version("v1.0"))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación adicional")
                        .url("https://github.com/Alexis-Aranguiz/backend-level-up-gamer"))
                // 🔐 --- AGREGADO: Seguridad JWT para activar "Authorize" en Swagger ---
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
