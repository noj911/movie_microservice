package com.movie.streaming.dto;

import com.movie.streaming.enums.CategoryEnum;
import lombok.Data;

import java.util.List;

@Data
public class MovieDto {
    private Long id;
    private String title;
    private CategoryEnum category;
    private Integer duration;
    private String thumbnailUrl;
    private String synopsis;

}
