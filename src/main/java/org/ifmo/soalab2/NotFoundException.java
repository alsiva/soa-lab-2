package org.ifmo.soalab2;

public class NotFoundException extends ApiException {
    public NotFoundException(int code, String msg) {
        super(code, msg);
    }
}
