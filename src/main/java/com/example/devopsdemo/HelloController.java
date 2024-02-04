package com.example.devopsdemo;




import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("")
   public String hello(){
        return "Hello ghoulam";
    }
    @GetMapping("/ayoub")
   public String hellop(){
        return "Hello meeeeeee  njnmm";
    }

 @GetMapping("/ayoub")
   public String helloppp(){
        return "Hello meeeeeee  njnmm";
    }



}
