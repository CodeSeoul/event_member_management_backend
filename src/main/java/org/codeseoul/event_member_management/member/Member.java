package org.codeseoul.event_member_management.member;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codeseoul.event_member_management.rsvp.Rsvp;
import org.codeseoul.event_member_management.sns_account.SnsAccount;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
@NoArgsConstructor
@Data
@Table(
        uniqueConstraints = @UniqueConstraint(
                // TODO: change to snake case
                columnNames = {"username", "email", "phoneNumber"}
        )
)
public class Member {
    private @Id
    @GeneratedValue Long id;
    private String username;
    private String displayName;
    private String email;
    private String phoneNumber;
    private String imageUrl;
    private String shortBio;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Rsvp> rsvps = new HashSet<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
    private List<SnsAccount> snsAccounts = new ArrayList<>();

    @Column(insertable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(insertable = false, updatable = false)
    @UpdateTimestamp
    private Timestamp updatedAt;

    public Member(String username, String displayName, String email, String phoneNumber, String imageUrl, String shortBio) {
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.shortBio = shortBio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Member member))
            return false;
        return Objects.equals(this.id, member.id)
                && Objects.equals(this.username, member.username)
                && Objects.equals(this.email, member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.username, this.email);
    }

    @Override
    public String toString() {
        return "Member{" + "id=" + this.id + ", username='" + this.username + "'}";
    }
}
