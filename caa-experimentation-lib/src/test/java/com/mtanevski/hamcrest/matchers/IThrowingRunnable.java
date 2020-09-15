package com.mtanevski.hamcrest.matchers;

@FunctionalInterface
public interface IThrowingRunnable<E extends Throwable> {
    void run() throws E;
}
