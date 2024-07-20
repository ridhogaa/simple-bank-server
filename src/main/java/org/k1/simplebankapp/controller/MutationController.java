package org.k1.simplebankapp.controller;

import org.k1.simplebankapp.dto.BaseResponse;
import org.k1.simplebankapp.entity.enums.MutationType;
import org.k1.simplebankapp.service.MutationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;

@RestController
@RequestMapping("v1/mutations")
public class MutationController {

    @Autowired
    private MutationService mutationService;

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam int month,
            @RequestParam int day,
            @RequestParam(required = true) MutationType type,
            @RequestParam(required = true) String noAccount,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(BaseResponse.success(mutationService.findAllByMonthAndDayAndType(month, day, type, noAccount, pageable), "Success Get All Mutations"));
    }
}
