package dev.kadys.matchhub.moderation;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface ModerationReportRepository extends JpaRepository<ModerationReport,UUID>{Page<ModerationReport> findByStatusOrderByCreatedAtDesc(ReportStatus status,Pageable pageable);long countByStatus(ReportStatus status);}
