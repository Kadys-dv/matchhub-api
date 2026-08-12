package dev.kadys.matchhub.report;

import dev.kadys.matchhub.match.*;
import dev.kadys.matchhub.user.UserRepository;
import dev.kadys.matchhub.moderation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {
    private final MatchRepository matches; private final UserRepository users; private final ParticipationRepository participations; private final ModerationReportRepository reports;
    public ReportController(MatchRepository matches,UserRepository users,ParticipationRepository participations,ModerationReportRepository reports) { this.matches=matches;this.users=users;this.participations=participations;this.reports=reports; }
    @GetMapping("/summary") Summary summary() {
        return new Summary(users.count(),users.countByEnabledTrue(),matches.countByStatus(MatchStatus.OPEN),matches.countByStatus(MatchStatus.FULL),matches.countByStatus(MatchStatus.COMPLETED),participations.count(),reports.countByStatus(ReportStatus.PENDING));
    }
    public record Summary(long totalUsers,long activeUsers,long openMatches,long fullMatches,long completedMatches,long totalParticipations,long pendingReports) {}
}
