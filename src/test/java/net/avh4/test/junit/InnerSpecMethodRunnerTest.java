package net.avh4.test.junit;

import net.avh4.test.junit.test.support.FailingTestExample;
import net.avh4.test.junit.test.support.PassingTestExample;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Runner;

public class InnerSpecMethodRunnerTest extends RunnerTestBase {

    private Runner runnerWithFailingTestExample;

    @Before
    public void setup() throws Exception {
        runner = new InnerSpecMethodRunner(PassingTestExample.Inner1.class,
                PassingTestExample.class);
        runnerWithFailingTestExample =
                new InnerSpecMethodRunner(FailingTestExample.Inner1.class,
                        FailingTestExample.class);
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
    public void shouldCallOuterClassBeforeMethods() throws Exception {
        runner.run(notifier);

        assertSuccessfulTestRunForDescription(
                hasDisplayName("passesUnlessOuterBeforesWereNotCalled"));
    }

    @Test
    public void shouldCallOuterClassBeforeMethodsBeforeInnerClassBeforeMethods()
            throws Exception {
        runner.run(notifier);

        assertSuccessfulTestRunForDescription(hasDisplayName(
                "passesUnlessInnerBeforesWereCalledBeforeOuterBefores"));
    }
}
