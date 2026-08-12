package dev.kadys.matchhub.match;

import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {
    private final MatchService service;
    public MatchController(MatchService service) { this.service=service; }
    @GetMapping Page<MatchDtos.Response> list(@RequestParam(defaultValue="OPEN") MatchStatus status,
        @PageableDefault(size=20,sort="startsAt",direction=Sort.Direction.ASC) Pageable pageable) { return service.list(status,pageable); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    MatchDtos.Response create(@Valid @RequestBody MatchDtos.CreateRequest request, @org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt) { return service.create(request,userId(jwt)); }
    @PostMapping("/{id}/participants/me") MatchDtos.Response join(@PathVariable UUID id,@org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt) { return service.join(id,userId(jwt)); }
    @DeleteMapping("/{id}/participants/me") MatchDtos.Response leave(@PathVariable UUID id,@org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt) { return service.leave(id,userId(jwt)); }
    @PostMapping("/{id}/complete") MatchDtos.Response complete(@PathVariable UUID id,@org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt) { return service.complete(id,userId(jwt)); }
    private UUID userId(Jwt jwt) { return UUID.fromString(jwt.getSubject()); }
}
