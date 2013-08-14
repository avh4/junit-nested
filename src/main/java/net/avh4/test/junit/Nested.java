package net.avh4.test.junit;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;

public class Nested extends Runner {
    private final Runner classRunner;
    private final ArrayList<Runner> childRunners = new ArrayList<>();

    public Nested(final Class<?> testClass) throws InitializationError {
        classRunner = new InnerRunner(testClass);

        for (Class<?> aClass : testClass.getDeclaredClasses()) {
            childRunners.add(new Nested(aClass));
        }
    }

    @Override
    public Description getDescription() {
        Description suiteDescription = classRunner.getDescription();

        for (Runner childRunner : childRunners) {
            suiteDescription.addChild(childRunner.getDescription());
        }

        return suiteDescription;
    }

    @Override
    public void run(RunNotifier notifier) {
        classRunner.run(notifier);

        for (Runner childRunner : childRunners) {
            childRunner.run(notifier);
        }
    }
}
