package dev.skaringa.qupa.exception;

import dev.skaringa.qupa.api.ErrorCode;
import dev.skaringa.qupa.api.ErrorType;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class BaseException extends RuntimeException {
    @NonNull
    private final ErrorCode code;

    protected BaseException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    protected BaseException(ErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public abstract ErrorType getType();
}
