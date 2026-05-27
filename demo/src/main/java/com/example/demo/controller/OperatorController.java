package com.example.demo.controller;

import com.example.demo.model.Operator;
import com.example.demo.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/operators")
public class OperatorController {

    @Autowired
    private OperatorService operatorService;

    @GetMapping
    public List<Operator> listAllOperators() {
        return operatorService.getAllOperators();
    }
}