package yoon.docker.mapService.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi friendApiGroup(){
        return GroupedOpenApi.builder()
                .group("MAP API")
                .pathsToMatch("/api/v1/maps/**")
                .build();
    }
}
