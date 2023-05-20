/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.common;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class Auditable {

  @Column(nullable = false)
  @CreatedDate
  private OffsetDateTime createdAt;

  @Column(nullable = false)
  @LastModifiedDate
  private OffsetDateTime updatedAt;
}
