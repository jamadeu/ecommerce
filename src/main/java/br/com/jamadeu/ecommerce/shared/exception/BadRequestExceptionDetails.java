package br.com.jamadeu.ecommerce.shared.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BadRequestExceptionDetails extends ExceptionDetails {
    @Builder
    public BadRequestExceptionDetails(
            String title, int status, String details, String developerMessage, LocalDateTime timestamp) {
        super(title, status, details, developerMessage, timestamp);
    }
}
