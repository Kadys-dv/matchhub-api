package dev.kadys.matchhub.user;

import dev.kadys.matchhub.error.NotFoundException;
import dev.kadys.matchhub.match.ParticipationRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class AdminUserService {
    private final UserRepository users; private final ParticipationRepository participations;
    public AdminUserService(UserRepository users,ParticipationRepository participations) { this.users=users;this.participations=participations; }
    @Transactional(readOnly=true) public Page<AdminDtos.UserResponse> list(String query,Pageable pageable) {
        Page<User> page=query==null || query.isBlank()?users.findAll(pageable):users.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query.trim(),query.trim(),pageable);
        return page.map(this::response);
    }
    @Transactional public AdminDtos.UserResponse setStatus(UUID id,boolean enabled) {
        User user=users.findById(id).orElseThrow(() -> new NotFoundException("Atleta não encontrado.")); user.setEnabled(enabled); return response(user);
    }
    private AdminDtos.UserResponse response(User user) { return new AdminDtos.UserResponse(user.getId(),user.getName(),user.getEmail(),user.getRole(),user.isEnabled(),user.getCreatedAt(),participations.countByPlayerId(user.getId())); }
}
