package com.meaningfull.insight.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Shahaf Pariente on 10/20/2020
 */
@RestController
public class HomeController {

    @GetMapping({"/"})
    String home() {
        return "hello";
    }
}
