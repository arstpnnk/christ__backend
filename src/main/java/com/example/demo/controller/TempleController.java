package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@RestController
@RequestMapping("/temples")
public class TempleController {

    @GetMapping
    public List<Map<String, Object>> getTemples(@RequestParam("lat") double lat, @RequestParam("lng") double lng) {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(Map.of("id", 1, "title", "Церковь Благодать", "lat", 53.905, "lng", 27.561));
        list.add(Map.of("id", 2, "title", "Храм Всех Святых", "lat", 53.932, "lng", 27.578));
        list.add(Map.of("id", 3, "title", "Церковь Святого Елизара", "lat", 53.913, "lng", 27.52));
        return list;
    }
}