package dev.skaringa.qupa.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@Api(tags = "Test Controller")
@RequiredArgsConstructor
@RequestMapping("api")
public class TestController {
    @GetMapping("test")
    public String test() {
        return "Hello World";
    }
}
