package com.atcproject.dbviewer.handlers;

import com.atcproject.dbviewer.Constants;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
    private final String description;

    public ErrorResponse(String message, String description) {
        this.code = Constants.ERR_DEFAULT;
        this.message = message;
        this.description = description;
    }

    public ErrorResponse(String code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
