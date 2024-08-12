package com.devcci.devtoy.member.web.docs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {

    @GetMapping("/api/usage")
    public String swagger() {
        return "redirect:/swagger-ui/index.html";
    }
}
