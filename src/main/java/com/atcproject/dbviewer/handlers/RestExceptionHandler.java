package com.atcproject.dbviewer.handlers;

import com.atcproject.dbviewer.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@Component("error")
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse("Server error", ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<?> handleNotFoundError(EntityNotFoundException ex, final HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse("Entity not found", ex.getLocalizedMessage());
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DBViewerException.class)
    public final ResponseEntity<?> handleInternalServerError(DBViewerException ex, final HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(ex.getCode(), ex.getMess(), ex.getDescription());
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof DBVEntityNotFound) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity(error, status);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> dataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorResponse error = new ErrorResponse(Constants.ERR_DATA_INTEGRITY, "Data Integrity Error",
                null != ex.getRootCause() ? ex.getRootCause().getLocalizedMessage() : ex.getLocalizedMessage());
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<?> handleConflict(
            RuntimeException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(Constants.ERR_WRONG_STATE, "Data Integrity Error", ex.getLocalizedMessage());
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        ErrorResponse error = new ErrorResponse(Constants.ERR_WRONG_INPUT_PARAMS, "Wrong input parameters", errorList.toString());
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}
