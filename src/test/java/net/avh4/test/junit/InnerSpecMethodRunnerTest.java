package net.avh4.test.junit;

import net.avh4.test.junit.test.support.FailingTestExample;
import net.avh4.test.junit.test.support.PassingTestExample;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Runner;

import static org.junit.Assert.assertTrue;

public class InnerSpecMethodRunnerTest extends RunnerTestBase {

    private Runner runnerWithFailingTestExample;

    @Before
    public void setup() throws Exception {
        runner = new InnerSpecMethodRunner(PassingTestExample.Inner1.class);
        runnerWithFailingTestExample =
                new InnerSpecMethodRunner(FailingTestExample.Inner1.class);
        super.setup();
    }

    @Test
    public void shouldRunSuccessfulTest() throws Exception {
        runner.run(notifier);

        assertSuccessfulTestRunForDescription(hasDisplayName("test1_1"));
    }

    @Test
    public void shouldCallInnerBeforeMethods() throws Exception {
        runner.run(notifier);

        assertSuccessfulTestRunForDescription(
                hasDisplayName("passesUnlessInnerBeforesWereNotCalled"));
    }

    @Test
    public void shouldCallOuterBeforeMethods() throws Exception {
        runner.run(notifier);

        assertSuccessfulTestRunForDescription(
                hasDisplayName("passesUnlessOuterBeforesWereNotCalled"));
    }

    @Test
    public void shouldCallOuterBeforeMethodsBeforeInnerBeforeMethods()
            throws Exception {
        runner.run(notifier);

        assertSuccessfulTestRunForDescription(hasDisplayName(
                "passesUnlessInnerBeforesWereCalledBeforeOuterBefores"));
    }

    @Test
    public void shouldCallInnerAfterMethods() throws Exception {
        PassingTestExample.innerAfterWasCalled = false;
        runner.run(notifier);

        assertTrue(PassingTestExample.innerAfterWasCalled);
    }

    @Test
    public void shouldCallOuterAfterMethods() throws Exception {
        PassingTestExample.outerAfterWasCalled = false;
        runner.run(notifier);

        assertTrue(PassingTestExample.outerAfterWasCalled);
    }

    @Test
    public void shouldCallOuterAfterMethodsAfterInnerAfterMethods()
            throws Exception {
        runner.run(notifier);

        assertTrue(
                PassingTestExample.valueOf_innerAfterWasCalled_whenOuterAfterWasCalled);
    }
}
