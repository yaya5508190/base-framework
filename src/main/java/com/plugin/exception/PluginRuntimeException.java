package com.plugin.exception;

import java.io.Serial;

public class PluginRuntimeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public PluginRuntimeException(String message) {
        super(message);
    }

    public PluginRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
