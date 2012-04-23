package net.avh4.jrspec;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class JrSpec extends Runner {
    private final TestClass testClass;
    private final ParentRunner<Class<?>> innerClassRunner;
    private Description suiteDescription;
    //    private final ParentRunner<Method> methodRunner;

    public JrSpec(final Class<?> testClass) throws InitializationError {
        this.testClass = new TestClass(testClass);
        innerClassRunner = new InnerClassRunner(testClass);
//        methodRunner = new MethodParentRunner(testClass);
    }

    @Override
    public Description getDescription() {
        suiteDescription = Description
                .createSuiteDescription(testClass.getName());
        for (Description childDescription : innerClassRunner.getDescription()
                .getChildren()) {
            suiteDescription.addChild(childDescription);
        }
        return suiteDescription;
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.fireTestStarted(suiteDescription);
        innerClassRunner.run(notifier);
        notifier.fireTestFinished(suiteDescription);
    }

    private static class InnerClassRunner extends ParentRunner<Class<?>> {
        private final Class<?> testClass;

        public InnerClassRunner(Class<?> testClass) throws InitializationError {
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
            final Description childDescription = describeChild(child);
            notifier.fireTestStarted(childDescription);
            createRunnerForChild(child).run(notifier);
            notifier.fireTestFinished(childDescription);
        }

        private InnerSpecMethodRunner createRunnerForChild(Class<?> child) {
            try {
                return new InnerSpecMethodRunner(child, testClass);
            } catch (InitializationError initializationError) {
                throw new RuntimeException(initializationError);
            }
        }

    }

    private static class MethodParentRunner extends ParentRunner<Method> {
        private final Class<?> testClass;

        public MethodParentRunner(Class<?> testClass)
                throws InitializationError {
            super(testClass);
            this.testClass = testClass;
        }

        @Override
        protected List<Method> getChildren() {
            return Arrays.asList(testClass.getDeclaredMethods());
        }

        @Override
        protected Description describeChild(Method child) {
            return Description
                    .createTestDescription(testClass, child.getName());
        }

        @Override
        protected void runChild(Method child, RunNotifier notifier) {
        }
    }
}
