package dev.kadys.matchhub.match;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ParticipationRepository extends JpaRepository<Participation, UUID> {
    long countByMatchId(UUID matchId);
    boolean existsByMatchIdAndPlayerId(UUID matchId, UUID playerId);
    long deleteByMatchIdAndPlayerId(UUID matchId, UUID playerId);
}
