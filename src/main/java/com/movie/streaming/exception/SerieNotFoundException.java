package com.movie.streaming.exception;

public class SerieNotFoundException extends BusinessException {
    public SerieNotFoundException(String id) {
        super(String.format("La série avec l'ID %s n'a pas été trouvée", id));
    }

}
