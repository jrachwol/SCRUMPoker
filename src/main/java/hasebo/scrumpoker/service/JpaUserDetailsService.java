package hasebo.scrumpoker.service;

import hasebo.scrumpoker.model.SecurityUser;
import hasebo.scrumpoker.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
        return userRepository
                .findByName(user)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found: " + user));
    }
}

