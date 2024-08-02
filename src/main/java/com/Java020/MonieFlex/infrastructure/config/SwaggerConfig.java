package com.Java020.MonieFlex.infrastructure.config;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "MonieFlex App",
                description = "APIs for MonieFlex Fintech App",
                version = "1.0",
                contact = @Contact(
                        name = "SQ020",
                        email = "monieflexapp@gmail.com",
                        url = "https://github.com/decadev-sq020"
                ),
                license = @License(
                        name = "MonieFlex Application",
                        url = "https://github.com/decadev-sq020"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "MonieFlex RESTful API Documentation",
                url = "https://github.com/egbas"
        ),
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Auth Description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}
