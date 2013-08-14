package net.avh4.test.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
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
            return statement;
        }
        statement = withOuterBefores(statement, outerInstance);
        statement = withOuterAfters(statement, outerInstance);
        statement = withOuterRules(statement, outerInstance);

        return statement;
    }

    private Statement withOuterRules(Statement statement,
                                     Object outerInstance) {
        List<TestRule> fieldRules = new TestClass(outerInstance.getClass())
                .getAnnotatedFieldValues(outerInstance, Rule.class,
                        TestRule.class);
        if (!fieldRules.isEmpty()) {
            statement = new RunRules(statement, fieldRules, null);
            // TODO find an example where the Rule needs a Description
        }
        return statement;
    }

    private Statement withOuterAfters(Statement statement,
                                      Object outerInstance) {
        final List<FrameworkMethod> outerAfters =
                new TestClass(outerInstance.getClass())
                        .getAnnotatedMethods(After.class);

        if (!outerAfters.isEmpty()) {
            statement = new RunAfters(statement, outerAfters, outerInstance);
        }
        return statement;
    }

    private Statement withOuterBefores(Statement statement,
                                       Object outerInstance) {
        final List<FrameworkMethod> outerBefores =
                new TestClass(outerInstance.getClass())
                        .getAnnotatedMethods(Before.class);

        if (!outerBefores.isEmpty()) {
            statement = new RunBefores(statement, outerBefores, outerInstance);
        }
        return statement;
    }
}
