package org.codeseoul.event_member_management.rsvp;

import lombok.Data;
import lombok.NonNull;
import org.codeseoul.event_member_management.event.Event;
import org.codeseoul.event_member_management.member.Member;
import org.codeseoul.event_member_management.rsvp_state.RsvpState;

import java.time.OffsetDateTime;

@Data
public class RsvpReadSchema {
    private Long eventId;
    private String eventTitle;
    private Long memberId;
    private String memberDisplayName;
    private String rsvpState;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public RsvpReadSchema(@NonNull Rsvp rsvp) {
        Event event = rsvp.getEvent();
        Member member = rsvp.getMember();
        RsvpState rsvpState = rsvp.getState();
        this.eventId = event.getId();
        this.eventTitle = event.getTitle();
        this.memberId = member.getId();
        this.memberDisplayName = member.getDisplayName();
        this.rsvpState = rsvpState.getState();
        this.createdAt = rsvp.getCreatedAt();
        this.updatedAt = rsvp.getUpdatedAt();
    }
}


