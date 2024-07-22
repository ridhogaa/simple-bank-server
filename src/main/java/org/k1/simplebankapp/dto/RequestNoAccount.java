package org.k1.simplebankapp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RequestNoAccount {
    @NotBlank
    private String noAccount;
}
