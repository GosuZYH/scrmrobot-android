package com.scrm.robot.exception;

public class AccessibilityNodeNotFoundException extends BaseException {
    public AccessibilityNodeNotFoundException() {
        super();
    }

    public AccessibilityNodeNotFoundException(String message) {
        super(message);
    }

    public AccessibilityNodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessibilityNodeNotFoundException(Throwable cause) {
        super(cause);
    }
}
