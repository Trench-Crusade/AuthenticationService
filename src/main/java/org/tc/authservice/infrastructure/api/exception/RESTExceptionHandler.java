package org.tc.authservice.infrastructure.api.exception;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.tc.authservice.shared.exceptions.access.TCAccessDeniedException;
import org.tc.authservice.shared.exceptions.api.TCApiException;
import org.tc.authservice.shared.exceptions.general.TCGeneralException;
import org.tc.authservice.shared.exceptions.general.detailed.TCIllegalStateException;
import org.tc.authservice.shared.exceptions.general.detailed.TCUtilityClassException;
import org.tc.authservice.shared.exceptions.out.TCOutboundException;
import org.tc.exceptions.persistence.TCPersistenceException;
import org.tc.exceptions.persistence.detailed.TCEntityNotFoundException;

@Slf4j
@Hidden
@RestControllerAdvice(basePackages = {
        "org.tc.authservice.infrastructure.api.controller",
}
)
public class RESTExceptionHandler
        extends ResponseEntityExceptionHandler {


    @ExceptionHandler(TCAccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    protected ResponseEntity<Object> handleAccessException(final TCAccessDeniedException ex ,final WebRequest request) {
        String bodyOfResponse = generateErrorMessage("Access Denied!",ex);
        log.error(bodyOfResponse);
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(TCApiException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleApiException(final TCApiException ex ,final WebRequest request) {
        String bodyOfResponse = generateErrorMessage("Api error!",ex);
        log.error(bodyOfResponse);
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({TCIllegalStateException.class, TCUtilityClassException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleGeneralException(final TCGeneralException ex , final WebRequest request) {
        String bodyOfResponse = generateErrorMessage("General error",ex);
        log.error(bodyOfResponse);
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(TCOutboundException.class)
    @ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
    protected ResponseEntity<Object> handleOutException(final TCOutboundException ex ,final WebRequest request) {
        String bodyOfResponse = generateErrorMessage("External process error",ex);
        log.error(bodyOfResponse);
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.FAILED_DEPENDENCY, request);
    }

    @ExceptionHandler(TCPersistenceException.class)
    @ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
    protected ResponseEntity<Object> handlePersistenceException(final TCPersistenceException ex , final WebRequest request) {
        String bodyOfResponse = generateErrorMessage("Persistence error",ex);
        log.error(bodyOfResponse);
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.FAILED_DEPENDENCY, request);
    }

    @ExceptionHandler(TCEntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handlePersistenceException(final TCEntityNotFoundException ex , final WebRequest request) {
        String bodyOfResponse = generateErrorMessage("Entity not found",ex);
        log.error(bodyOfResponse);
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    private static String generateErrorMessage(String preamble, Exception ex){
        return preamble+"\nException type: "+ex.getClass().getSimpleName()+"\nException message: "+ex.getMessage();
    }

}