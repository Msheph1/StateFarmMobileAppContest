package com.max.finalproj;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ContactController {

    
    @GetMapping({"/contact","/contactform"})
    public String showContactForm(){
        return "redirect:/contact.html";
    }



    @PostMapping("/results")
    public String sendContactForm(HttpServletRequest request, Model model) {
        
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");
        
        
        return "confirmation";

    }
}
