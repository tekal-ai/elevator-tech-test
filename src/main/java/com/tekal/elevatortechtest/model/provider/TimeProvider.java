package com.tekal.elevatortechtest.model.provider;

public interface TimeProvider {
    void sleep(long milliseconds) throws InterruptedException;
}