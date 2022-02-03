package com.aro.cron;

public class WrongCronException extends Exception {
    public WrongCronException(String message) {
        super(message);
    }
}
