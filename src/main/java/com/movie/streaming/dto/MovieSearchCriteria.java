package com.movie.streaming.dto;

import com.movie.streaming.enums.CategoryEnum;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MovieSearchCriteria {
    private Long id;
    private String title;
    private CategoryEnum category;
    private Integer duration;
    private String thumbnailUrl;
    private String synopsis;

}
