package com.hamdan.slinkapi.infra.doc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList("ApiKeyAuth")
                        .addList("UsernameAuth"))
                .components(new Components()
                        .addSecuritySchemes("ApiKeyAuth", createAPIKeyScheme())
                        .addSecuritySchemes("UsernameAuth", createUserNameScheme()))
                .info(new Info().title("SLink REST API")
                        .description("API REST para encurtador de URL")
                        .version("1.0").contact(new Contact().name("Gabriel Hamdan")
                                .email( "gabriel_hamdan@hotmail.com").url("https://www.linkedin.com/in/gabriel-hamdan/"))
                        .license(new License().name("MIT License")
                                .url("https://mit-license.org/")));
    }

    private SecurityScheme createUserNameScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("userName");
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("X-API-Key");
    }

}
