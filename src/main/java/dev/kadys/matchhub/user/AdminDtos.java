package dev.kadys.matchhub.user;

import java.time.Instant;
import java.util.UUID;

public final class AdminDtos {
    private AdminDtos() {}
    public record UserResponse(UUID id,String name,String email,Role role,boolean enabled,Instant createdAt,long participations) {}
    public record StatusRequest(boolean enabled) {}
}
