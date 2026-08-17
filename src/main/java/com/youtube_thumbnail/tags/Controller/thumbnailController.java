package com.youtube_thumbnail.tags.Controller;

import com.youtube_thumbnail.tags.Service.thumbnailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class thumbnailController {

    private static final Logger logger = LoggerFactory.getLogger(thumbnailController.class);

    @Autowired
    private thumbnailService thumbnailService;

    @GetMapping("/thumbnail")
    public String thumbnailPage() {
        return "thumbnails";
    }

    @PostMapping("/get-thumbnail")
    public Mono<String> showThumbnail(ServerWebExchange exchange, Model model) {
        return exchange.getFormData()
                .flatMap(formData -> {
                    String videoUrlOrId = formData.getFirst("videoUrlOrId");
                    
                    logger.debug("=== FORM SUBMISSION DEBUG ===");
                    logger.info("Received video URL or ID: {}", videoUrlOrId);
                    logger.debug("Is null: {}", (videoUrlOrId == null));
                    if (videoUrlOrId != null) {
                        logger.debug("Trimmed value: '{}'", videoUrlOrId.trim());
                        logger.debug("Length: {}", videoUrlOrId.length());
                    }
                    logger.debug("=============================");

                    if (videoUrlOrId == null || videoUrlOrId.trim().isEmpty()) {
                        model.addAttribute("error", "Please provide a valid YouTube URL or Video ID");
                        return Mono.just("thumbnails");
                    }

                    String Id = thumbnailService.extractVideoId(videoUrlOrId);
                    if(Id == null){
                        model.addAttribute("error", "Invalid YouTube URL or Video ID");
                        return Mono.just("thumbnails");
                    }
                    String thumbnailUrl = "https://img.youtube.com/vi/" + Id + "/maxresdefault.jpg";
                    model.addAttribute("thumbnailUrl", thumbnailUrl);
                    model.addAttribute("videoId", Id);
                    return Mono.just("thumbnails");
                });
    }
}


