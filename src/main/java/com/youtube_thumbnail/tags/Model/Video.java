package com.youtube_thumbnail.tags.Model;

import lombok.*;

import java.util.List;
 import java.util.stream.Collectors;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Video {

    private String id;
    private String channelTitle;
    private String title;
    private List<String> tags;

}
