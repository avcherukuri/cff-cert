package com.company.notification.exception;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.company.notification.dto.ValidationErrorResponse;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleValidation_returns400WithFieldErrors() throws NoSuchMethodException {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(
                List.of(new FieldError("request", "emailEnabled", "must not be null")));
        when(bindingResult.getGlobalErrors()).thenReturn(List.of());
        // Constructed directly (not mocked): MethodArgumentNotValidException is effectively
        // final-shaped for byte-buddy inline mocking and not worth fighting with a mock here.
        MethodParameter parameter = new MethodParameter(this.getClass().getDeclaredMethod("dummyTarget", String.class), 0);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<ValidationErrorResponse> response = handler.handleValidation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().fieldErrors()).hasSize(1);
        assertThat(response.getBody().fieldErrors().get(0).field()).isEqualTo("emailEnabled");
    }

    @SuppressWarnings("unused")
    private void dummyTarget(String arg) {
        // used only as a reflection target to construct a real MethodParameter above
    }

    @Test
    void handleOptimisticLockConflict_returns409() {
        var ex = new OptimisticLockConflictException("please retry", new RuntimeException());

        ResponseEntity<ValidationErrorResponse> response = handler.handleOptimisticLockConflict(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().message()).isEqualTo("please retry");
    }

    @Test
    void handleUnexpected_returns500WithoutLeakingInternalMessage() {
        ResponseEntity<ValidationErrorResponse> response = handler.handleUnexpected(new RuntimeException("secret internal detail"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().message()).isEqualTo("An unexpected error occurred");
        assertThat(response.getBody().message()).doesNotContain("secret internal detail");
    }

    @Test
    void handleMalformedRequestBody_returns400() {
        var ex = new HttpMessageNotReadableException("JSON parse error", mock(HttpInputMessage.class));

        ResponseEntity<ValidationErrorResponse> response = handler.handleMalformedRequestBody(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().message()).doesNotContain("JSON parse error");
    }

    @Test
    void handleUnauthenticated_returns401() {
        var ex = new IllegalStateException("No authenticated AuthenticatedUserPrincipal in the security context");

        ResponseEntity<ValidationErrorResponse> response = handler.handleUnauthenticated(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().message()).isEqualTo("Authentication is required");
    }
}
