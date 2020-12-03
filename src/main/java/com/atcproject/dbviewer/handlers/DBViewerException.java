package com.atcproject.dbviewer.handlers;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DBViewerException extends RuntimeException {
    private final String message;

}
