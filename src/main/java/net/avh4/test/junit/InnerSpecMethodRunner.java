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
    private final TestClass outerTestClass;

    public InnerSpecMethodRunner(Class<?> testClass, Class<?> outerClass)
            throws InitializationError {
        super(testClass);
        outerTestClass = new TestClass(outerClass);
    }

    @Override
    protected void collectInitializationErrors(List<Throwable> errors) {
        // Don't call super
    }

    @Override
    protected Object createTest() throws Exception {
        final Class aClass = getTestClass().getJavaClass();
        return instantiator.instantiate(aClass);
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        super.runChild(method, notifier);
    }

    @Override
    protected Statement withBefores(FrameworkMethod method, Object target,
                                    Statement statement) {
        final Statement innerBefores =
                super.withBefores(method, target, statement);
        final List<FrameworkMethod> outerBefores =
                outerTestClass.getAnnotatedMethods(Before.class);
        Object outerInstance;
        try {
            Field f = target.getClass().getDeclaredField("this$0");
            f.setAccessible(true);
            outerInstance = f.get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        // Warning: this is not quite correct, and is not threadsafe.  We should really be making
        // sure that the outerInstance corresponds to this instance (target) of the test class.
        return outerBefores.isEmpty() ? innerBefores :
                new RunBefores(innerBefores, outerBefores, outerInstance);
    }
}
