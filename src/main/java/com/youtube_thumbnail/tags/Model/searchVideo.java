package com.youtube_thumbnail.tags.Model;


import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Getter
@Setter
public class searchVideo {

    private Video primaryVideo;
    private List<Video> relatedVideos;

}
