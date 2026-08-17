package com.youtube_thumbnail.tags.Controller;

import com.youtube_thumbnail.tags.Model.searchVideo;
import com.youtube_thumbnail.tags.Service.youtubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/youtube")
public class tagsController {

    private static final Logger logger = LoggerFactory.getLogger(tagsController.class);

    @Value("${youtube.api.key}")
    private String apiKey;

    @Autowired
    private youtubeService youtubeService;

    @GetMapping
    public String home() {
        return "home";
    }

    private String IsApiKeyValid(String apiKey) {
        return (apiKey != null && !apiKey.isEmpty()) ? "valid" : "invalid";
    }

    @PostMapping("/search")
    public Mono<String> VideoTags(ServerWebExchange exchange, Model model) {
        return exchange.getFormData()
                .flatMap(formData -> {
                    String videoTitle = formData.getFirst("videoTitle");

                    logger.info("Received video title: {}", videoTitle);

                    if(videoTitle == null || videoTitle.trim().isEmpty()){
                        model.addAttribute("error", "Video title is required. Please provide a valid video title.");
                        return Mono.just("home");
                    }

                    if(IsApiKeyValid(apiKey).equals("invalid")){
                        model.addAttribute("error", "Invalid API Key");
                        return Mono.just("home");
                    }

                    return youtubeService.searchVideos(videoTitle)
                            .flatMap(result -> {
                                if(result == null){
                                    model.addAttribute("error", "No video found for the given title");
                                    return Mono.just("home");
                                }
                                model.addAttribute("primaryVideo", result.getPrimaryVideo());
                                model.addAttribute("relatedVideos", result.getRelatedVideos());
                                return Mono.just("home");
                            })
                            .onErrorResume(e -> {
                                model.addAttribute("error", "An error occurred while searching for the video");
                                return Mono.just("home");
                            });
                });
    }
}
