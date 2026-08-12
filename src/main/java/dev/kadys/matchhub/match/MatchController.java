package dev.kadys.matchhub.match;

import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {
    private final MatchService service;
    public MatchController(MatchService service) { this.service=service; }
    @GetMapping Page<MatchDtos.Response> list(@RequestParam(defaultValue="OPEN") MatchStatus status,@PageableDefault(size=20,sort="startsAt",direction=Sort.Direction.ASC) Pageable pageable) { return service.list(status,pageable); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) MatchDtos.Response create(@Valid @RequestBody MatchDtos.CreateRequest request,@org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt) { return service.create(request,userId(jwt)); }
    @PostMapping("/{id}/participants/me") MatchDtos.Response join(@PathVariable UUID id,@org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt) { return service.join(id,userId(jwt)); }
    @DeleteMapping("/{id}/participants/me") MatchDtos.Response leave(@PathVariable UUID id,@org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt) { return service.leave(id,userId(jwt)); }
    @GetMapping("/{id}/participants") List<MatchDtos.ParticipantResponse> participants(@PathVariable UUID id) { return service.participants(id); }
    @PostMapping("/{id}/complete") MatchDtos.Response complete(@PathVariable UUID id,@org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt) { return service.complete(id,userId(jwt)); }
    @PostMapping("/{id}/cancel") MatchDtos.Response cancel(@PathVariable UUID id,@org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt,Authentication authentication) { return service.cancel(id,userId(jwt),authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))); }
    private UUID userId(Jwt jwt) { return UUID.fromString(jwt.getSubject()); }
}
