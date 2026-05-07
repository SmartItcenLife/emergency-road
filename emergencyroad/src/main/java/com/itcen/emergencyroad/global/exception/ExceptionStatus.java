package com.itcen.emergencyroad.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionStatus {

    /* 400 BAD_REQUEST */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "Invalid email"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Invalid password"),
    IMAGE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "Image limit exceeded"),

    /* 401 UNAUTHORIZED */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    BLACKLISTED_TOKEN(HttpStatus.UNAUTHORIZED, "Blacklisted token"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    ACCESS_TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "Access token required"),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Token not found"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Expired token"),
    PREMATURE_TOKEN(HttpStatus.UNAUTHORIZED, "Premature token"),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "Wrong password"),

    /* 403 FORBIDDEN */
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),

    /* 404 NOT_FOUND */
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post not found"),

    /* 409 CONFLICT */
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "Duplicated email"),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "Duplicated nickname"),

    /* 500 INTERNAL_SERVER_ERROR */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed");

    private final HttpStatus status;
    private final String message;

    ExceptionStatus(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}