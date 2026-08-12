package dev.kadys.matchhub.user;

import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final AdminUserService service;
    public AdminUserController(AdminUserService service) { this.service=service; }
    @GetMapping Page<AdminDtos.UserResponse> list(@RequestParam(required=false) String query,@PageableDefault(size=20,sort="createdAt",direction=Sort.Direction.DESC) Pageable pageable) { return service.list(query,pageable); }
    @PatchMapping("/{id}/status") AdminDtos.UserResponse status(@PathVariable UUID id,@RequestBody AdminDtos.StatusRequest request) { return service.setStatus(id,request.enabled()); }
}
