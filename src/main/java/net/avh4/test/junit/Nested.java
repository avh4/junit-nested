package net.avh4.test.junit;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Nested extends Runner {
    private final BlockJUnit4ClassRunner outerClassRunner;
    private final ParentRunner<Class<?>> innerClassesRunner;
    private final Class<?> testClass;

    public Nested(final Class<?> testClass) throws InitializationError {
        BlockJUnit4ClassRunner outerClassRunner;
        try {
            outerClassRunner = new BlockJUnit4ClassRunner(testClass);
        } catch (Exception e) {
            outerClassRunner = null;
        }
        this.outerClassRunner = outerClassRunner;
        innerClassesRunner = new InnerClassesRunner(testClass);
        this.testClass = testClass;
    }

    @Override
    public Description getDescription() {
        Description suiteDescription;
        if (outerClassRunner != null) {
            suiteDescription = outerClassRunner.getDescription();
        } else {
            suiteDescription = Description.createSuiteDescription(testClass);
        }
        for (Description childDescription : innerClassesRunner.getDescription()
                .getChildren()) {
            suiteDescription.addChild(childDescription);
        }
        return suiteDescription;
    }

    @Override
    public void run(RunNotifier notifier) {
        if (outerClassRunner != null) {
            outerClassRunner.run(notifier);
        }
        innerClassesRunner.run(notifier);
    }

    private static class InnerClassesRunner extends ParentRunner<Class<?>> {
        private final HashMap<Class<?>, InnerSpecMethodRunner> runners =
                new HashMap<>();
        private final List<Class<?>> classChildren;

        public InnerClassesRunner(Class<?> testClass)
                throws InitializationError {
            super(testClass);

            classChildren = Arrays.asList(testClass.getDeclaredClasses());
            for (Class<?> aClass : classChildren) {
                runners.put(aClass, new InnerSpecMethodRunner(aClass));
            }
        }

        @Override
        protected List<Class<?>> getChildren() {
            return classChildren;
        }

        @Override
        protected Description describeChild(Class<?> child) {
            return runners.get(child).getDescription();
        }

        @Override
        protected void runChild(Class<?> child, RunNotifier notifier) {
            runners.get(child).run(notifier);
        }
    }
}
