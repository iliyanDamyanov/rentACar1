package org.rentacar1.app.notification.model;


import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private UUID id;
    private String recipient;
    private String message;
    private LocalDateTime createdAt;
    private boolean read;
}

