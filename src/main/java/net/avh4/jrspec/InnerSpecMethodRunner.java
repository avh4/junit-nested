package net.avh4.jrspec;

import org.junit.Assert;
import org.junit.Before;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

class InnerSpecMethodRunner extends BlockJUnit4ClassRunner {

    private final TestClass outerTestClass;
    private Object outerInstance;

    public InnerSpecMethodRunner(Class<?> testClass, Class<?> outerClass) throws InitializationError {
        super(testClass);
        outerTestClass = new TestClass(outerClass);
    }

    @Override
    protected void collectInitializationErrors(List<Throwable> errors) {
        // Don't call super
    }

    @Override
    protected Object createTest() throws Exception {
        final Object outerClassInstance = createOuterClass();
        outerInstance = outerClassInstance;

        final Class aClass = getTestClass().getJavaClass();
        final Constructor<?>[] constructors = aClass.getConstructors();
        Assert.assertEquals(1, constructors.length);
        return constructors[0].newInstance(outerClassInstance);
    }

    private Object createOuterClass() throws InvocationTargetException, IllegalAccessException, InstantiationException {
        final Constructor<?>[] constructors = outerTestClass.getJavaClass().getConstructors();
        Assert.assertEquals(1, constructors.length);
        return constructors[0].newInstance();
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        super.runChild(method, notifier);
    }

    @Override
    protected Statement withBefores(FrameworkMethod method, Object target,
                                    Statement statement) {
        final Statement innerBefores = super.withBefores(method, target, statement);
        final List<FrameworkMethod> outerBefores = outerTestClass.getAnnotatedMethods(
                Before.class);
        // Warning: this is not quite correct, and is not threadsafe.  We should really be making
        // sure that the outerInstance corresponds to this instance (target) of the test class.
        return outerBefores.isEmpty() ? innerBefores : new RunBefores(innerBefores,
                outerBefores, outerInstance);
    }
}
