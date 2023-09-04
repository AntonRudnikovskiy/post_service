package faang.school.postservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@WebAppConfiguration
class GlobalExceptionHandlerTest {
    @InjectMocks
    private GlobalExceptionHandler handler;
    @Mock
    HttpServletRequest webRequest;

    @BeforeEach
    void setUp() {
        when(webRequest.getRequestURI()).thenReturn("/testUrl");
    }

    @Test
    void handleMethodArgumentNotValidException() {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();

        MethodParameter parameter = Mockito.mock(MethodParameter.class);
        when(parameter.getExecutable()).thenReturn(method);

        FieldError error = Mockito.mock(FieldError.class);
        when(error.getField()).thenReturn("field");
        when(error.getDefaultMessage()).thenReturn("Field error message");

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.getAllErrors()).thenReturn(List.of(error));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter,
                bindingResult);

        Map<String, String> errorResponseMap = handler.handleMethodArgumentNotValidException(exception);

        assertAll(() -> {
            assertEquals(1, errorResponseMap.size());
            assertEquals("field", errorResponseMap.keySet().stream().findFirst().orElse(""));
            assertEquals("Field error message", errorResponseMap.get("field"));
        });
    }

    @Test
    void handleIllegalArgumentException() {
        IllegalArgumentException exception = mock(IllegalArgumentException.class);
        when(exception.getMessage()).thenReturn("IllegalArgumentException");
    }

    @Test
    void handleConstraintViolationException_shouldMatchAllFields() {
        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        when(exception.getMessage()).thenReturn("Constraint violation exception");

        ErrorResponse response = handler.handleConstraintViolationException(exception, webRequest);

        assertAll(() -> {
            assertEquals("/testUrl", response.getUrl());
            assertEquals("Constraint violation exception", response.getError());
            assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        });
    }

    @Test
    void handleDataValidationException() {
        DataValidationException exception = mock(DataValidationException.class);
        when(exception.getMessage()).thenReturn("Data validation exception");

        ErrorResponse response = handler.handleDataValidationException(exception, webRequest);

        assertAll(() -> {
            assertEquals("/testUrl", response.getUrl());
            assertEquals("Data validation exception", response.getError());
            assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        });
    }

    @Test
    void handleEntityNotFoundException() {
        EntityNotFoundException exception = mock(EntityNotFoundException.class);
        when(exception.getMessage()).thenReturn("Entity not found exception");

        ErrorResponse response = handler.handleEntityNotFoundException(exception, webRequest);

        assertAll(() -> {
            assertEquals("/testUrl", response.getUrl());
            assertEquals("Entity not found exception", response.getError());
            assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        });
    }

    @Test
    void handleRuntimeException() {
        RuntimeException exception = mock(RuntimeException.class);
        when(exception.getMessage()).thenReturn("Internal server error");

        ErrorResponse response = handler.handleRuntimeException(exception, webRequest);

        assertAll(() -> {
            assertEquals("/testUrl", response.getUrl());
            assertEquals("Internal server error", response.getError());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        });
    }

    @Test
    void handleException() {
        Exception exception = mock(Exception.class);
        when(exception.getMessage()).thenReturn("Internal server error");

        ErrorResponse response = handler.handleException(exception, webRequest);

        assertAll(() -> {
            assertEquals("/testUrl", response.getUrl());
            assertEquals("Internal server error", response.getError());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        });
    }
}