package io.dami.market.interfaces.advice;

public record ErrorResponse(int status, String errorCode, String message)
{}
