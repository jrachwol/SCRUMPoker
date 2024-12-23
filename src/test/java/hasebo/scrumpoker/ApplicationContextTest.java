package hasebo.scrumpoker;

import hasebo.scrumpoker.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ApplicationContextTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {

        // Sprawdzenie, czy beany są wstrzyknięte poprawnie
        assertThat(memberRepository).isNotNull();
        assertThat(passwordEncoder).isNotNull();
    }
}