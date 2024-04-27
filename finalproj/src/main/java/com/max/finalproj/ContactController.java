package com.max.finalproj;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ContactController {

    
    @GetMapping({"/index","/"})
    public String showContactForm(){
        return "redirect:/html/index2.html";
    }


    @PostMapping("/results")
    public String sendContactForm(HttpServletRequest request, Model model) {
        
        


        
        
        return "confirmation";

    }
}
