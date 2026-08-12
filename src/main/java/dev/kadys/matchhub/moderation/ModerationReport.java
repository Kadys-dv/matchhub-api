package dev.kadys.matchhub.moderation;

import dev.kadys.matchhub.match.Match;
import dev.kadys.matchhub.user.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="moderation_reports")
public class ModerationReport {
    @Id private UUID id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="reporter_id") private User reporter;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="match_id") private Match match;
    @Column(nullable=false,length=80) private String reason;
    @Column(nullable=false,length=1000) private String details;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private ReportStatus status;
    @Column(name="created_at",nullable=false) private Instant createdAt;
    @Column(name="resolved_at") private Instant resolvedAt;
    protected ModerationReport() {}
    public ModerationReport(User reporter,Match match,String reason,String details){this.id=UUID.randomUUID();this.reporter=reporter;this.match=match;this.reason=reason;this.details=details;this.status=ReportStatus.PENDING;this.createdAt=Instant.now();}
    public void setStatus(ReportStatus status){this.status=status;this.resolvedAt=status==ReportStatus.PENDING?null:Instant.now();}
    public UUID getId(){return id;} public User getReporter(){return reporter;} public Match getMatch(){return match;} public String getReason(){return reason;} public String getDetails(){return details;} public ReportStatus getStatus(){return status;} public Instant getCreatedAt(){return createdAt;} public Instant getResolvedAt(){return resolvedAt;}
}
