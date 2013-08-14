package net.avh4.test.junit;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;

public class Nested extends Runner {
    private final BlockJUnit4ClassRunner outerClassRunner;
    private final Class<?> testClass;
    private final ArrayList<Runner> childRunners = new ArrayList<>();

    public Nested(final Class<?> testClass) throws InitializationError {
        BlockJUnit4ClassRunner outerClassRunner;
        try {
            outerClassRunner = new InnerSpecMethodRunner(testClass);
        } catch (Exception e) {
            outerClassRunner = null;
        }
        this.outerClassRunner = outerClassRunner;
        this.testClass = testClass;

        for (Class<?> aClass : testClass.getDeclaredClasses()) {
            childRunners.add(new Nested(aClass));
        }
    }

    @Override
    public Description getDescription() {
        Description suiteDescription;
        if (outerClassRunner != null) {
            suiteDescription = outerClassRunner.getDescription();
        } else {
            suiteDescription = Description.createSuiteDescription(testClass);
        }
        for (Runner childRunner : childRunners) {
            suiteDescription.addChild(childRunner.getDescription());
        }
        return suiteDescription;
    }

    @Override
    public void run(RunNotifier notifier) {
        if (outerClassRunner != null) {
            outerClassRunner.run(notifier);
        }
        for (Runner childRunner : childRunners) {
            childRunner.run(notifier);
        }
    }
}
