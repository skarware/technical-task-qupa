package dev.skaringa.qupa.api;

import lombok.NonNull;
import lombok.Value;

@Value
public class Error {
    @NonNull
    ErrorType type;

    @NonNull
    ErrorCode code;

    String message;

    public static Error unexpected(String message) {
        return system(ErrorCode.UNEXPECTED, message);
    }

    public static Error system(ErrorCode code, String message) {
        return new Error(ErrorType.SYSTEM, code, message);
    }
}
