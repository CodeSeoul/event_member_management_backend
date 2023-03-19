package org.codeseoul.event_member_management.sns_account;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codeseoul.event_member_management.common.Auditable;
import org.codeseoul.event_member_management.member.Member;
import org.codeseoul.event_member_management.sns_service.SnsService;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Data
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"externalId", "snsServiceId"}
    )
)
public class SnsAccount extends Auditable {
    private @Id @GeneratedValue Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @NotNull
    private String externalId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "snsServiceId")
    private SnsService snsService;

    public SnsAccount(Member member, String externalId, SnsService snsService) {
        this.member = member;
        this.externalId = externalId;
        this.snsService = snsService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SnsAccount account))
            return false;
        return Objects.equals(this.id, account.id)
                && Objects.equals(this.externalId, account.externalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.externalId);
    }

    @Override
    public String toString() {
        return "Event{" + "id=" + this.id + ", externalId='" + this.externalId + "'}";
    }
}
