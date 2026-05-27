package com.example.demo.service;

import com.example.demo.model.Operator;
import com.example.demo.repository.OperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperatorService {

    @Autowired
    private OperatorRepository operatorRepository;

    public List<Operator> getAllOperators() {
        return operatorRepository.findAll();
    }
}