package com.meaningfull.insight.controller;

import com.meaningfull.insight.services.RobinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Shahaf Pariente on 8/3/2020
 */
@RestController
@RequestMapping({"/robin"})
public class RobinHoodController {

    @Autowired
    RobinService robinService;

    @GetMapping({"/login"})
    public Map<String, Object> login() {
        robinService.login();
        return Map.of("Login", "test");
    }
}
