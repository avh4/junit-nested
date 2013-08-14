package net.avh4.test.junit.test.support;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class IncrementWhenCalled implements TestRule {
    private final Counter counter;

    IncrementWhenCalled(Counter counter) {
        this.counter = counter;
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                counter.increment();
                base.evaluate();
            }
        };
    }
}
