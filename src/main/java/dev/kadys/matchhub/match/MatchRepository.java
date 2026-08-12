package dev.kadys.matchhub.match;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;

public interface MatchRepository extends JpaRepository<Match, UUID> {
    Page<Match> findByStatusOrderByStartsAtAsc(MatchStatus status, Pageable pageable);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Match m where m.id = :id")
    Optional<Match> findByIdForUpdate(@Param("id") UUID id);
}
