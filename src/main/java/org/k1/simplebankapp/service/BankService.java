package org.k1.simplebankapp.service;

import org.k1.simplebankapp.entity.Bank;

import java.util.List;

public interface BankService {

    List<Bank> findAll();
}
