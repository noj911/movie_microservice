package com.movie.streaming.enums;

public enum QualiteStream {
    SD(0.5f),
    HD(1.0f),
    FHD(2.0f),
    UHD(4.0f);

    private final float scaleFactor;

    QualiteStream(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }
}
