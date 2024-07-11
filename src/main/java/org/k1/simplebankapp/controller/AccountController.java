package org.k1.simplebankapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.k1.simplebankapp.dto.auth.LoginRequest;
import org.k1.simplebankapp.dto.base.BaseResponse;
import org.k1.simplebankapp.service.account.AccountService;
import org.k1.simplebankapp.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "Account")
@RestController
@RequestMapping("v1/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping()
    public ResponseEntity<?> getAccount(Principal principal) {
        return ResponseEntity.ok(BaseResponse.success(accountService.findAllByUser(principal), "Success get account"));
    }
}
