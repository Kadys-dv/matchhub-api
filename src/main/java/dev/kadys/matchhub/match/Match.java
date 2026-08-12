package dev.kadys.matchhub.match;

import dev.kadys.matchhub.user.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "matches")
public class Match {
    @Id private UUID id;
    @Column(nullable = false, length = 120) private String title;
    @Column(nullable = false, length = 40) private String sport;
    @Column(nullable = false, length = 240) private String address;
    @Column(name = "starts_at", nullable = false) private Instant startsAt;
    @Column(nullable = false) private int capacity;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private MatchStatus status;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "organizer_id") private User organizer;
    @Version private long version;
    @Column(name = "created_at", nullable = false) private Instant createdAt;

    protected Match() {}
    public Match(String title, String sport, String address, Instant startsAt, int capacity, User organizer) {
        this.id = UUID.randomUUID(); this.title = title; this.sport = sport; this.address = address;
        this.startsAt = startsAt; this.capacity = capacity; this.organizer = organizer;
        this.status = MatchStatus.OPEN; this.createdAt = Instant.now();
    }
    public void markFull() { status = MatchStatus.FULL; }
    public void reopen() { status = MatchStatus.OPEN; }
    public void complete() { status = MatchStatus.COMPLETED; }
    public void cancel() { status = MatchStatus.CANCELLED; }
    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getSport() { return sport; }
    public String getAddress() { return address; }
    public Instant getStartsAt() { return startsAt; }
    public int getCapacity() { return capacity; }
    public MatchStatus getStatus() { return status; }
    public User getOrganizer() { return organizer; }
}
