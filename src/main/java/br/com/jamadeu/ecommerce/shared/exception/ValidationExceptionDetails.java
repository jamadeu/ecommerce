package br.com.jamadeu.ecommerce.shared.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ValidationExceptionDetails extends ExceptionDetails {
    private final String fields;
    private final String fieldsMessage;

    @Builder
    public ValidationExceptionDetails(
            String title,
            int status,
            String details,
            String developerMessage,
            LocalDateTime timestamp,
            String fields,
            String fieldsMessage) {
        super(title, status, details, developerMessage, timestamp);
        this.fields = fields;
        this.fieldsMessage = fieldsMessage;
    }
}
