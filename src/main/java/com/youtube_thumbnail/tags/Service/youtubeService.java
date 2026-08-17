package com.youtube_thumbnail.tags.Service;

import com.youtube_thumbnail.tags.Model.Video;
import com.youtube_thumbnail.tags.Model.VideoDetails;
import com.youtube_thumbnail.tags.Model.searchVideo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class youtubeService {

    private static final Logger logger = LoggerFactory.getLogger(youtubeService.class);
    private final WebClient.Builder webClientBuilder;


    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.api.base.url}")
    private String apiBaseUrl;

    @Value("${youtube.api.max.related.videos}")
    private int maxRelatedVideos;

    public Mono<searchVideo> searchVideos(String videoTitle) {
        logger.info("Searching for videos with title: {}", videoTitle);
        return searchVideoIds(videoTitle)
               .flatMap(videoIds -> {
                   if (videoIds.isEmpty()) {
                       logger.warn("No videos found for title: {}", videoTitle);
                       return Mono.just(searchVideo.builder()
                               .primaryVideo(null)
                               .relatedVideos(Collections.emptyList())
                               .build());
                   }

                   String primaryVideoId = videoIds.get(0);
                   List<String> relatedVideoIds = videoIds.subList(1, Math.min(maxRelatedVideos + 1, videoIds.size()));
                    
                   return fetchVideoDetails(primaryVideoId)
                           .flatMap(primaryVideo -> {
                               if (relatedVideoIds.isEmpty()) {
                                   return Mono.just(searchVideo.builder()
                                           .primaryVideo(primaryVideo)
                                           .relatedVideos(Collections.emptyList())
                                           .build());
                               }
                               return Flux.fromIterable(relatedVideoIds)
                                       .flatMap(this::fetchVideoDetails)
                                       .collectList()
                                       .map(relatedVideos -> searchVideo.builder()
                                               .primaryVideo(primaryVideo)
                                               .relatedVideos(relatedVideos)
                                               .build());
                           });
               });
    }

    private Mono<List<String>> searchVideoIds(String videoTitle) {
        logger.debug("Fetching video IDs for: {}", videoTitle);
        return webClientBuilder.baseUrl(apiBaseUrl)
               .build()
               .get()
               .uri(uriBuilder -> uriBuilder
                       .path("/search")
                       .queryParam("part", "snippet")
                       .queryParam("q", videoTitle)
                       .queryParam("type", "video")
                       .queryParam("maxResults", 5)
                       .queryParam("key", apiKey)
                       .build())
               .retrieve()
               .bodyToMono(SearchApiResponse.class)
               .flatMap(searchResponse -> {
                   if(searchResponse == null || searchResponse.items == null || searchResponse.items.isEmpty()) {
                       logger.warn("No search response received");
                       return Mono.just(new ArrayList<String>());
                   }

                   List<String> videoDetails = new ArrayList<String>();
                   for(SearchItem item : searchResponse.items) {
                       videoDetails.add(item.id.videoId);
                   }

                   logger.info("Found {} videos", videoDetails.size());
                   return Mono.just(videoDetails);
               })
               .onErrorResume(e -> {
                   logger.error("Error searching video IDs: ", e);
                   return Mono.just(new ArrayList<String>());
               });
    }

    public Mono<VideoDetails> getVideoDetails(String videoId) {
        logger.debug("Fetching details for video ID: {}", videoId);
        return webClientBuilder.baseUrl(apiBaseUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")
                        .queryParam("part", "snippet")
                        .queryParam("id", videoId)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(VideoApiResponse.class)
                .flatMap(videoResponse -> {
                    if(videoResponse == null || videoResponse.items == null || videoResponse.items.isEmpty()) {
                        logger.warn("No video details found for ID: {}", videoId);
                        return Mono.empty();
                    }

                    VideoSnippet snippet = videoResponse.items.get(0).snippet;
                    List<String> tag = snippet.tags != null ? snippet.tags : Collections.emptyList();
                    String thumbnailUrl = snippet.thumbnails != null ? snippet.thumbnails.getBestThumbnailUrl() : null;
                    return Mono.just(VideoDetails.builder()
                            .id(videoId)
                            .title(snippet.title)
                            .channelTitle(snippet.channelTitle)
                            .description(snippet.description)
                            .tags(tag)
                            .thumbnailUrl(thumbnailUrl)
                            .publishedAt(snippet.publishedAt)
                            .build());
                })
                .onErrorResume(e -> {
                    logger.error("Error fetching video details for ID {}: ", videoId, e);
                    return Mono.empty();
                });
    }

    private Mono<Video> fetchVideoDetails(String videoId) {
        logger.debug("Fetching details for video ID: {}", videoId);
        return webClientBuilder.baseUrl(apiBaseUrl)
               .build()
               .get()
               .uri(uriBuilder -> uriBuilder
                       .path("/videos")
                       .queryParam("part", "snippet")
                       .queryParam("id", videoId)
                       .queryParam("key", apiKey)
                       .build())
               .retrieve()
               .bodyToMono(VideoApiResponse.class)
               .flatMap(videoResponse -> {
                   if(videoResponse == null || videoResponse.items == null || videoResponse.items.isEmpty()) {
                       logger.warn("No video details found for ID: {}", videoId);
                       return Mono.empty();
                   }

                   VideoSnippet snippet = videoResponse.items.get(0).snippet;
                   List<String> tag = snippet.tags != null ? snippet.tags : Collections.emptyList();
                   return Mono.just(Video.builder()
                           .id(videoId)
                           .title(snippet.title)
                           .channelTitle(snippet.channelTitle)
                           .tags(tag)
                           .build());
               })
               .onErrorResume(e -> {
                   logger.error("Error fetching video details for ID {}: ", videoId, e);
                   return Mono.empty();
               });
    }

    @Data
    static class SearchApiResponse {
        List<SearchItem> items;
    }

    @Data
    static class SearchItem {
        SearchId id;
    }

    @Data
    static class SearchId {
        String videoId;
    }

    @Data
    static class VideoApiResponse{

        List<VideoItem> items;
    }

    @Data
    static class VideoItem{
        VideoSnippet snippet;
    }

    @Data
    static class VideoSnippet{
        String title;
        String description;
        String channelTitle;
        String publishedAt;
        Thumbnail thumbnails;
        List<String> tags;
    }

    @Data
    static class Thumbnail{
        ThumbnailDetails defaultThumbnail;
        ThumbnailDetails medium;
        ThumbnailDetails high;
        ThumbnailDetails standard;
        ThumbnailDetails maxres;

        String getBestThumbnailUrl() {
            if (maxres != null) return maxres.url;
            if (standard != null) return standard.url;
            if (high != null) return high.url;
            if (medium != null) return medium.url;
            if (defaultThumbnail != null) return defaultThumbnail.url;
            return null;
        }
    }

    @Data
    static class ThumbnailDetails{
        String url;
    }

}

