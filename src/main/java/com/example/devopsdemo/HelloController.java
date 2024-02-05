package com.example.devopsdemo;




import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("")
   public String hello(){
        return "Hello js";
    }
 @GetMapping("")
   public String test(){
        return "Hello moustapha";
    }



}
