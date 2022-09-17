package pl.adamsiedlecki.ohm.gateway.exceptions;

public class ResponseFromWrongStationException extends RuntimeException{

    public ResponseFromWrongStationException(String message) {
        super(message);
    }
}
