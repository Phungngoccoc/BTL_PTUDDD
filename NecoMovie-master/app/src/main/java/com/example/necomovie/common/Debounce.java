package com.example.necomovie.common;

import android.os.Handler;
import android.os.Looper;

public class Debounce {

    private final long delayMs;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable runnable;

    public Debounce(long delayMs, Runnable runnable) {
        this.delayMs = delayMs;
        this.runnable = runnable;
    }

    public void execute() {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(runnable, delayMs);
    }
}