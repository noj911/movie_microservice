package com.movie.streaming.enums;

/**
 * Movie Entity
 */



public enum CategoryEnum {

    ACTION("Action"),
    COMEDY("Comédie"),
    DRAMA("Drame"),
    HORROR("Horreur"),
    SCIENCE_FICTION("Science Fiction"),
    DOCUMENTARY("Documentaire"),
    ANIMATION("Animation"),
    THRILLER("Thriller");

    private final String displayName;

    CategoryEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }


}