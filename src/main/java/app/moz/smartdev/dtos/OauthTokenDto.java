package app.moz.smartdev.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OauthTokenDto {
    private UUID id;
    private UUID userId;
    private UUID providerId;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Timestamp expiresAt;
    private Timestamp createdAt;
}