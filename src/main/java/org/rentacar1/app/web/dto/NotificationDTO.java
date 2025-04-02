package org.rentacar1.app.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private UUID userId;
    private String recipient;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;

}
