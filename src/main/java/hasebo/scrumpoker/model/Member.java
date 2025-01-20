package hasebo.scrumpoker.model;

import hasebo.scrumpoker.interfaces.ScrumPokerMember;
import hasebo.scrumpoker.interfaces.SpringSecurityUserDetails;
import jakarta.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Getter
@Setter
@Table(name="member")
@NoArgsConstructor
public class Member implements ScrumPokerMember, SpringSecurityUserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    private String roles;
//    private String lastView;

    @OneToMany(mappedBy = "owner")
    private List<Room> rooms = new ArrayList<>();

    public Member(String name, String password, String roles) {
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    // Methods for Interface ScrumPokerMember
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    // Methods for Interface SpringSecurityUserDetails implements UserDetails (Spring Security)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles
            .split(","))
            .map(SimpleGrantedAuthority::new)
            .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }

}
