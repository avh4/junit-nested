package net.avh4.jrspec;

import org.junit.Assert;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

class InnerSpecMethodRunner extends BlockJUnit4ClassRunner {

    private final Class<?> outerClass;

    public InnerSpecMethodRunner(Class<?> testClass, Class<?> outerClass) throws InitializationError {
        super(testClass);
        this.outerClass = outerClass;
    }

    @Override
    protected void collectInitializationErrors(List<Throwable> errors) {
        // Don't call super
    }

    @Override
    protected Object createTest() throws Exception {
        final Object outerClassInstance = createOuterClass();

        final Class aClass = getTestClass().getJavaClass();
        final Constructor<?>[] constructors = aClass.getConstructors();
        Assert.assertEquals(1, constructors.length);
        return constructors[0].newInstance(outerClassInstance);
    }

    private Object createOuterClass() throws InvocationTargetException, IllegalAccessException, InstantiationException {
        final Constructor<?>[] constructors = outerClass.getConstructors();
        Assert.assertEquals(1, constructors.length);
        return constructors[0].newInstance();
    }
}
