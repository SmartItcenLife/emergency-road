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
    AUTHENTICATION_FAIL(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),

    /* 403 FORBIDDEN */
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),
    USER_POST_FORBIDDEN(HttpStatus.FORBIDDEN, "본인이 작성한 게시글 외에는 수정할 수 없습니다."),

    /* 404 NOT_FOUND */
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),

    /* 409 CONFLICT */
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "Duplicated email"),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "이미 사용중인 닉네임입니다."),
    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "이미 사용중인 아이디입니다."),

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
