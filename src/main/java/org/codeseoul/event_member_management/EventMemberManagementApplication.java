/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class EventMemberManagementApplication {

  @Value("${app.cors-allowed-origins}")
  private String[] corsAllowedOrigins;

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(corsAllowedOrigins);
      }
    };
  }

  public static void main(String[] args) {
    SpringApplication.run(EventMemberManagementApplication.class, args);
  }
}
