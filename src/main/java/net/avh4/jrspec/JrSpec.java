package net.avh4.jrspec;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import java.util.Arrays;
import java.util.List;

public class JrSpec extends Runner {
    private final BlockJUnit4ClassRunner outerClassRunner;
    private final ParentRunner<Class<?>> innerClassesRunner;
    private final Class<?> testClass;

    public JrSpec(final Class<?> testClass) throws InitializationError {
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
        private final Class<?> testClass;

        public InnerClassesRunner(Class<?> testClass)
                throws InitializationError {
            super(testClass);
            this.testClass = testClass;
        }

        @Override
        protected List<Class<?>> getChildren() {
            return Arrays.asList(testClass.getDeclaredClasses());
        }

        @Override
        protected Description describeChild(Class<?> child) {
            return createRunnerForChild(child).getDescription();
        }

        @Override
        protected void runChild(Class<?> child, RunNotifier notifier) {
            createRunnerForChild(child).run(notifier);
        }

        private InnerSpecMethodRunner createRunnerForChild(Class<?> child) {
            try {
                return new InnerSpecMethodRunner(child, testClass);
            } catch (InitializationError initializationError) {
                throw new RuntimeException(initializationError);
            }
        }
    }
}
