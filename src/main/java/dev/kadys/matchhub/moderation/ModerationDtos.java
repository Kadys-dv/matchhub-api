package dev.kadys.matchhub.moderation;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.UUID;
public final class ModerationDtos {private ModerationDtos(){} public record CreateRequest(UUID matchId,@NotBlank @Size(max=80) String reason,@NotBlank @Size(max=1000) String details){} public record UpdateRequest(@NotNull ReportStatus status){} public record Response(UUID id,UUID reporterId,String reporterName,UUID matchId,String matchTitle,String reason,String details,ReportStatus status,Instant createdAt,Instant resolvedAt){} }
