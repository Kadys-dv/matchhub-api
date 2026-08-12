package dev.kadys.matchhub.moderation;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
@RestController @RequestMapping("/api/v1/reports") public class ModerationController {private final ModerationService service;public ModerationController(ModerationService service){this.service=service;}@PostMapping @ResponseStatus(HttpStatus.CREATED) ModerationDtos.Response create(@Valid @RequestBody ModerationDtos.CreateRequest request,@org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt){return service.create(request,UUID.fromString(jwt.getSubject()));}@GetMapping @PreAuthorize("hasRole('ADMIN')") Page<ModerationDtos.Response> list(@RequestParam(defaultValue="PENDING") ReportStatus status,@PageableDefault(size=20) Pageable pageable){return service.list(status,pageable);}@PatchMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") ModerationDtos.Response update(@PathVariable UUID id,@Valid @RequestBody ModerationDtos.UpdateRequest request){return service.update(id,request.status());}}
