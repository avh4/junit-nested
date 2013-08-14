package net.avh4.test.junit;

import org.junit.Before;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Field;
import java.util.List;

class InnerSpecMethodRunner extends BlockJUnit4ClassRunner {

    private final InnerClassInstantiator instantiator =
            new InnerClassInstantiator();
    private Object test;

    public InnerSpecMethodRunner(Class<?> testClass)
            throws InitializationError {
        super(testClass);
    }

    @Override
    protected void collectInitializationErrors(List<Throwable> errors) {
        // Don't call super
    }

    @Override
    protected Object createTest() throws Exception {
        if (test == null) {
            final Class aClass = getTestClass().getJavaClass();
            test = instantiator.instantiate(aClass);
        }
        return test;
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        super.runChild(method, notifier);
    }

    @Override
    protected Statement methodBlock(FrameworkMethod method) {
        Statement statement = super.methodBlock(method);
        assert test != null;

        Object outerInstance;
        try {
            Field f = test.getClass().getDeclaredField("this$0");
            f.setAccessible(true);
            outerInstance = f.get(test);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        final List<FrameworkMethod> outerBefores =
                new TestClass(outerInstance.getClass())
                        .getAnnotatedMethods(Before.class);

        if (!outerBefores.isEmpty()) {
            statement = new RunBefores(statement, outerBefores, outerInstance);
        }

        return statement;
    }
}
