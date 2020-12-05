package com.atcproject.dbviewer.handlers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class DBViewerException extends RuntimeException {
    private final String code;
    private final String mess;
    private final String description;
}
