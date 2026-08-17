package com.youtube_thumbnail.tags.Controller;

import com.youtube_thumbnail.tags.Model.VideoDetails;
import com.youtube_thumbnail.tags.Service.thumbnailService;
import com.youtube_thumbnail.tags.Service.youtubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
public class VideoController {

    @Autowired
    private youtubeService youtubeService;

    @Autowired
    private thumbnailService thumbnailService;

    @GetMapping("/youtube/video-details")
    public String showVideoform() {
        return "video-details";
    }

    @PostMapping("/youtube/video-details")
    public Mono<String> submitVideoForm(ServerWebExchange exchange, Model model) {
        return exchange.getFormData().flatMap(formData -> {
            String videoUrlOrId = formData.getFirst("videoUrlOrId");
            String Id = thumbnailService.extractVideoId(videoUrlOrId);
            if (Id == null) {
                model.addAttribute("error", "Invalid YouTube URL or Video ID");
                return Mono.just("video-details");
            }

            return youtubeService.getVideoDetails(Id)
                    .flatMap(videoDetails -> {
                        if (videoDetails == null) {
                            model.addAttribute("error", "No video found for the given ID");
                            return Mono.just("video-details");
                        }
                        model.addAttribute("videoDetails", videoDetails);
                        model.addAttribute("videoUrlorId", videoUrlOrId);
                        return Mono.just("video-details");
                    })
                    .defaultIfEmpty("video-details");
        });
    }
}
