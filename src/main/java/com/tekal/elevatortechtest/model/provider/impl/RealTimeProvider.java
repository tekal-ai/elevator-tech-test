package com.tekal.elevatortechtest.model.provider.impl;

import com.tekal.elevatortechtest.model.provider.TimeProvider;

public class RealTimeProvider implements TimeProvider {
    @Override
    public void sleep(long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }
}