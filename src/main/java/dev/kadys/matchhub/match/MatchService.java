package dev.kadys.matchhub.match;

import dev.kadys.matchhub.error.*;
import dev.kadys.matchhub.user.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class MatchService {
    private final MatchRepository matches; private final ParticipationRepository participations; private final UserRepository users;
    public MatchService(MatchRepository matches,ParticipationRepository participations,UserRepository users) { this.matches=matches;this.participations=participations;this.users=users; }
    @Transactional public MatchDtos.Response create(MatchDtos.CreateRequest request,UUID organizerId) {
        User organizer=user(organizerId);
        Match match=matches.save(new Match(request.title().trim(),request.sport().trim(),request.address().trim(),request.startsAt(),request.capacity(),organizer));
        participations.save(new Participation(match,organizer)); return MatchDtos.Response.of(match,1);
    }
    @Transactional(readOnly=true) public Page<MatchDtos.Response> list(MatchStatus status,Pageable pageable) {
        return matches.findByStatusOrderByStartsAtAsc(status,pageable).map(m -> MatchDtos.Response.of(m,participations.countByMatchId(m.getId())));
    }
    @Transactional public MatchDtos.Response join(UUID matchId,UUID playerId) {
        Match match=lockedMatch(matchId);
        if (match.getStatus()!=MatchStatus.OPEN) throw new ConflictException("A partida não está aberta para novas participações.");
        if (participations.existsByMatchIdAndPlayerId(matchId,playerId)) throw new ConflictException("Jogador já confirmado nesta partida.");
        long confirmed=participations.countByMatchId(matchId);
        if (confirmed>=match.getCapacity()) { match.markFull(); throw new ConflictException("Não há vagas disponíveis."); }
        participations.save(new Participation(match,user(playerId))); confirmed++;
        if (confirmed>=match.getCapacity()) match.markFull();
        return MatchDtos.Response.of(match,confirmed);
    }
    @Transactional public MatchDtos.Response leave(UUID matchId,UUID playerId) {
        Match match=lockedMatch(matchId);
        if (match.getOrganizer().getId().equals(playerId)) throw new ConflictException("O organizador não pode abandonar a própria partida.");
        if (participations.deleteByMatchIdAndPlayerId(matchId,playerId)==0) throw new NotFoundException("Participação não encontrada.");
        long confirmed=participations.countByMatchId(matchId); if (match.getStatus()==MatchStatus.FULL) match.reopen();
        return MatchDtos.Response.of(match,confirmed);
    }
    @Transactional public MatchDtos.Response complete(UUID matchId,UUID requesterId) {
        Match match=lockedMatch(matchId);
        if (!match.getOrganizer().getId().equals(requesterId)) throw new ForbiddenException("Somente o organizador pode concluir a partida.");
        if (match.getStatus()==MatchStatus.COMPLETED) throw new ConflictException("A partida já foi concluída.");
        match.complete(); return MatchDtos.Response.of(match,participations.countByMatchId(matchId));
    }
    @Transactional public MatchDtos.Response cancel(UUID matchId,UUID requesterId,boolean admin) {
        Match match=lockedMatch(matchId);
        if (!admin && !match.getOrganizer().getId().equals(requesterId)) throw new ForbiddenException("Somente o organizador ou administrador pode cancelar a partida.");
        if (match.getStatus()==MatchStatus.COMPLETED || match.getStatus()==MatchStatus.CANCELLED) throw new ConflictException("Esta partida não pode mais ser cancelada.");
        match.cancel(); return MatchDtos.Response.of(match,participations.countByMatchId(matchId));
    }
    @Transactional(readOnly=true) public List<MatchDtos.ParticipantResponse> participants(UUID matchId) {
        if (!matches.existsById(matchId)) throw new NotFoundException("Partida não encontrada.");
        return participations.findByMatchIdOrderByJoinedAtAsc(matchId).stream()
            .map(p -> new MatchDtos.ParticipantResponse(p.getPlayer().getId(),p.getPlayer().getName(),p.getPlayer().getEmail(),p.getJoinedAt())).toList();
    }
    private Match lockedMatch(UUID id) { return matches.findByIdForUpdate(id).orElseThrow(() -> new NotFoundException("Partida não encontrada.")); }
    private User user(UUID id) { return users.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado.")); }
}
