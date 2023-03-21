package org.codeseoul.event_member_management;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    /*
    * Whether or not to provide seed data in the database.
    * Only useful for local development.
     */
    private final boolean seedDatabase = false;
}
