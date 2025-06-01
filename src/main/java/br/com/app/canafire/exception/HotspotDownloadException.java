package br.com.app.canafire.exception;

public class HotspotDownloadException extends RuntimeException {
    public HotspotDownloadException(String message) {
        super(message);
    }

    public HotspotDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}