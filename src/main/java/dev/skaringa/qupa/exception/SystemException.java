package dev.skaringa.qupa.exception;


import dev.skaringa.qupa.api.ErrorCode;
import dev.skaringa.qupa.api.ErrorType;

public class SystemException extends BaseException {
    public SystemException(ErrorCode code, String message) {
        super(code, message);
    }

    public SystemException(ErrorCode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public static SystemException unexpected(String message) {
        return new SystemException(ErrorCode.UNEXPECTED, message);
    }

    public static SystemException unexpected(String message, Throwable cause) {
        return new SystemException(ErrorCode.UNEXPECTED, message, cause);
    }

    @Override
    public ErrorType getType() {
        return ErrorType.SYSTEM;
    }
}
