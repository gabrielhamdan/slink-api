package com.hamdan.slinkapi.infra.exception;

import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class ApiAssert {

    public static <T, E extends Throwable> void notEquals(T t1, T t2) throws E {
        notEquals(t1, t2, () -> new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, EApiErrorMessage.UNEXPECTED_ERR));
    }

    public static <T, E extends Throwable> void notEquals(T t1, T t2, Supplier<E> e) throws E {
        if (t1.equals(t2))
            throw e.get();
    }

    public static <T, E extends Throwable> void equals(T t1, T t2) throws E {
        equals(t1, t2, () -> new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, EApiErrorMessage.UNEXPECTED_ERR));
    }

    public static <T, E extends Throwable> void equals(T t1, T t2, Supplier<E> e) throws E {
        if (!t1.equals(t2))
            throw e.get();
    }

    public static <E extends Throwable> void isTrue(boolean assertion) throws E {
        isTrue(assertion, () -> new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, EApiErrorMessage.UNEXPECTED_ERR));
    }

    public static <E extends Throwable> void isTrue(boolean assertion, Supplier<E> e) throws E {
        if (!assertion)
            throw e.get();
    }

    public static <T, E extends Throwable> void notNull(T t) throws E {
        notNull(t, () -> new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, EApiErrorMessage.UNEXPECTED_ERR));
    }

    public static <T, E extends Throwable> void notNull(T t, Supplier<E> e) throws E {
        if (t == null)
            throw e.get();
    }

    public static <T, E extends Throwable> void isNull(T t) throws E {
        isNull(t, () -> new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, EApiErrorMessage.UNEXPECTED_ERR));
    }

    public static <T, E extends Throwable> void isNull(T t, Supplier<E> e) throws E {
        if (t == null)
            throw e.get();
    }

}
