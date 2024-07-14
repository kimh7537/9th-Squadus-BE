package com.cotato.squadus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {


    @GetMapping("/test")
    public String index() {
        log.debug("log 확인");
        return "Hello World";
    }

}

