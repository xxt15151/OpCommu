package com.example.demo.service;

import com.example.demo.model.Operator;
import com.example.demo.repository.AdminOperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminOperatorService {

    @Autowired
    private AdminOperatorRepository adminOperatorRepository;

    public List<Operator> listAllOperators() {
        return adminOperatorRepository.findAll();
    }

    public Operator getOperatorById(Long id) {
        return adminOperatorRepository.findById(id);
    }

    public boolean createOperator(Operator operator) {
        int rows = adminOperatorRepository.save(operator);
        return rows > 0;
    }

    public boolean updateOperator(Operator operator) {
        int rows = adminOperatorRepository.update(operator);
        return rows > 0;
    }

    public boolean deleteOperator(Long id) {
        int rows = adminOperatorRepository.deleteById(id);
        return rows > 0;
    }
}