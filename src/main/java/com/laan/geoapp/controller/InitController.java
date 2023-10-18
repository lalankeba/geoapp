package com.laan.geoapp.controller;

import com.laan.geoapp.util.PathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping(PathUtil.WELCOME)
@Slf4j
public class InitController {

    @GetMapping
    public ResponseEntity<Object> welcome() {
        String message = "Welcome to Geo App";
        log.info(message);
        return new ResponseEntity<>(Collections.singletonMap("message", message), HttpStatus.OK);
    }
}
