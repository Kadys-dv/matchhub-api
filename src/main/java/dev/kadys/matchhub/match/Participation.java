package dev.kadys.matchhub.match;

import dev.kadys.matchhub.user.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "participations", uniqueConstraints = @UniqueConstraint(name = "uk_participation_match_player", columnNames = {"match_id", "player_id"}))
public class Participation {
    @Id private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "match_id") private Match match;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "player_id") private User player;
    @Column(name = "joined_at", nullable = false) private Instant joinedAt;
    protected Participation() {}
    public Participation(Match match, User player) { this.id = UUID.randomUUID(); this.match = match; this.player = player; this.joinedAt = Instant.now(); }
    public User getPlayer() { return player; }
    public Instant getJoinedAt() { return joinedAt; }
}
