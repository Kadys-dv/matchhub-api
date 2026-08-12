package dev.kadys.matchhub.config;

import dev.kadys.matchhub.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminBootstrap implements ApplicationRunner {
    private final UserRepository users; private final String adminEmail;
    public AdminBootstrap(UserRepository users,@Value("${app.admin.email:}") String adminEmail) { this.users=users;this.adminEmail=adminEmail; }
    @Override @Transactional public void run(ApplicationArguments args) {
        if (adminEmail==null || adminEmail.isBlank()) return;
        users.findByEmailIgnoreCase(adminEmail.trim()).ifPresent(user -> { if (user.getRole()!=dev.kadys.matchhub.user.Role.ADMIN) user.promoteToAdmin(); });
    }
}
