package com.youtube_thumbnail.tags.Dtos;


import lombok.Data;

import java.util.List;

public class VideoSnippet {
    String title;
    String description;
    String channelTitle;
    String publishedAt;
    Thumbnail thumbnails;
    List<String> tags;
}
