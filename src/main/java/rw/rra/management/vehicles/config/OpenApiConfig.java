package rw.rra.management.vehicles.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    final String schemeName = "bearerAuth";
    final String bearerFormat = "JWT";
    final String scheme = "bearer";

    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(schemeName)
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        schemeName,
                                        new SecurityScheme()
                                                .name(schemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .bearerFormat(bearerFormat)
                                                .in(SecurityScheme.In.HEADER)
                                                .scheme(scheme)
                                ))
                .info(
                        new Info()
                                .title("RRA Vehicle Management System")
                                .version("1.0.0").description("This is the application developed by Angele Marie"));
    }

}

