package com.youtube_thumbnail.tags.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class pageController {

    @GetMapping({"/", "/home"})
    public String homePage() {
        return "home";
    }

    @GetMapping("/video-details")
    public String videoDetailsPage() {
        return "video-details";
    }

}
