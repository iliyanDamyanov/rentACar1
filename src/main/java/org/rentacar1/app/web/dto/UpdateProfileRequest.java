package org.rentacar1.app.web.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    
}
