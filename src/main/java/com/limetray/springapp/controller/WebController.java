package com.limetray.springapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @GetMapping("/monitor")
    public String queueMonitor() {
        return "queue-monitor";  // This will serve queue-monitor.html from templates folder
    }
    
    @GetMapping("/")
    public String home() {
        return "redirect:/monitor";  // Redirect root to monitor page
    }
}