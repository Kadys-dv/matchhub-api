package dev.kadys.matchhub.auth;

import dev.kadys.matchhub.error.*;
import dev.kadys.matchhub.user.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.List;

@Service
public class AuthService {
    private final UserRepository users; private final PasswordEncoder passwords; private final JwtEncoder jwtEncoder;
    private final String issuer; private final Duration ttl;
    public AuthService(UserRepository users, PasswordEncoder passwords, JwtEncoder jwtEncoder,
                       @Value("${app.jwt.issuer}") String issuer, @Value("${app.jwt.ttl}") Duration ttl) {
        this.users=users; this.passwords=passwords; this.jwtEncoder=jwtEncoder; this.issuer=issuer; this.ttl=ttl;
    }
    @Transactional
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(email)) throw new ConflictException("E-mail já cadastrado.");
        return tokenFor(users.save(new User(request.name().trim(), email, passwords.encode(request.password()))));
    }
    @Transactional(readOnly = true)
    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        User user = users.findByEmailIgnoreCase(request.email().trim()).orElseThrow(() -> new UnauthorizedException("Credenciais inválidas."));
        if (!passwords.matches(request.password(), user.getPasswordHash())) throw new UnauthorizedException("Credenciais inválidas.");
        return tokenFor(user);
    }
    private AuthDtos.AuthResponse tokenFor(User user) {
        Instant now=Instant.now();
        var claims=JwtClaimsSet.builder().issuer(issuer).issuedAt(now).expiresAt(now.plus(ttl)).subject(user.getId().toString())
            .claim("email", user.getEmail()).claim("name", user.getName()).claim("roles", List.of(user.getRole().name())).build();
        String token=jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(() -> "HS256").build(), claims)).getTokenValue();
        return new AuthDtos.AuthResponse(token,"Bearer",ttl.toSeconds(),user.getId(),user.getName(),user.getRole().name());
    }
}
