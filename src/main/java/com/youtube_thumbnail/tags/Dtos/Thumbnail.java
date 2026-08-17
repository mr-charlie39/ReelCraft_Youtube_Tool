package com.youtube_thumbnail.tags.Dtos;

import com.youtube_thumbnail.tags.Service.youtubeService;
import lombok.Data;

public class Thumbnail {
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
