package com.example.devopsdemo;




import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("")
   public String hello(){
        return "Hello js";
    }
 @GetMapping("/test")
   public String test(){
        return " moustapha";
    }



}
