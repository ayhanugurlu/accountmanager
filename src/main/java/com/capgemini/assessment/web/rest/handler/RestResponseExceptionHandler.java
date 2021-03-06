package com.capgemini.assessment.web.rest.handler;

import com.capgemini.assessment.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * Created by silayugurlu on 5/26/18.
 */
@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${error.code.header.key}")
    private String errorCodeHeaderKey;

    @Autowired
    private Tracer tracer;


    @ExceptionHandler({AccountNotFoundException.class, CustomerNotFoundException.class, CustomerAlreadyExistException.class, InsufficientBalanceException.class})
    public ResponseEntity<ErrResponse> handleTemplateException(HttpServletRequest request, TemplateException e) {

        String errorMessage = e.getErrors().stream().collect(Collectors.joining(", "));
        String[] eCodes = {e.getErrorCode()};

        logger.error(errorMessage);
        ErrResponse response = new ErrResponse(tracer.getCurrentSpan().getTraceId(), errorMessage);


        return ResponseEntity
                .status(e.getHttpStatus())
                .header(errorCodeHeaderKey, eCodes)
                .body(response);
    }


}
