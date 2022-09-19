package com.mybank.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.mybank.dto.ErrorDTO;

@Provider
public class MyBankExceptionHandler implements ExceptionMapper<MyBankException> {

    @Override
    public Response toResponse(MyBankException exception) {
        ErrorDTO errorData = new ErrorDTO(exception.code, exception.getMessage());
        Status status = Status.BAD_REQUEST;
        if (exception instanceof SecurityException) {
            status = Status.UNAUTHORIZED;
        }
        return Response.status(status).entity(errorData).build();
    }
}
