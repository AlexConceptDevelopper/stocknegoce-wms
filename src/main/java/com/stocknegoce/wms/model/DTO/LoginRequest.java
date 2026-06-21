package com.stocknegoce.wms.model.DTO;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO représentant la requête de login.
 */
@Getter
@Setter
public class LoginRequest {
    private String login;
    private String password;
}
