package dev.kadys.matchhub.match;

import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.UUID;

public final class MatchDtos {
    private MatchDtos() {}
    public record CreateRequest(@NotBlank @Size(max=120) String title, @NotBlank @Size(max=40) String sport,
        @NotBlank @Size(max=240) String address, @NotNull @Future Instant startsAt, @Min(2) @Max(100) int capacity) {}
    public record Response(UUID id,String title,String sport,String address,Instant startsAt,int capacity,long confirmed,
        MatchStatus status,UUID organizerId,String organizerName) {
        static Response of(Match match,long confirmed) { return new Response(match.getId(),match.getTitle(),match.getSport(),match.getAddress(),match.getStartsAt(),match.getCapacity(),confirmed,match.getStatus(),match.getOrganizer().getId(),match.getOrganizer().getName()); }
    }
}
