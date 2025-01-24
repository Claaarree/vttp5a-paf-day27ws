package vttp5a_paf.day27ws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

public class OpenAPIConfiguration {
    @Configuration
public class OpenAPIConfig {
    // endpoint to test is localhost:8080/swagger-ui/index.html
    
    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI().info(
            new Info()
            .title("PAF Day 26ws")
            .description("Testing API using OpenAPI public interface")
            .version("1.0")
        );
    }
}
}
