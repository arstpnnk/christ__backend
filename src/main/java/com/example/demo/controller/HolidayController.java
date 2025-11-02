package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;  // Добавить этот импорт

@RestController
@RequestMapping("/holidays")
public class HolidayController {

    @GetMapping
    public String getHolidays() {
        return "List of religious holidays";
    }
}
