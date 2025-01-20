package hasebo.scrumpoker.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface SpringSecurityUserDetails extends UserDetails {

    Long getId();
}
