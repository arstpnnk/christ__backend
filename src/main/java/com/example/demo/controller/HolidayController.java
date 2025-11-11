package com.example.demo.controller;

import com.example.demo.model.Holiday;
import com.example.demo.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/holidays")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    @GetMapping
    public List<Holiday> getHolidays() {
        return holidayService.getHolidays();
    }
}
