/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {}
