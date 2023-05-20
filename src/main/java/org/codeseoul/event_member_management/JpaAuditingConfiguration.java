/* (C) 2023 */
package org.codeseoul.event_member_management;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class JpaAuditingConfiguration {

  @Bean(name = "auditingDateTimeProvider")
  public DateTimeProvider dateTimeProvider() {
    return () -> Optional.of(OffsetDateTime.now(ZoneId.of("UTC")));
  }
}
