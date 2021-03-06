package net.avh4.test.junit;

import net.avh4.test.junit.test.support.PassingTestExample;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class InnerRunnerTest extends RunnerTestBase {

    @Before
    public void setup() throws Exception {
        runner = new InnerRunner(PassingTestExample.Inner1.class);
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

    @Test
    public void shouldCallInnerRules() throws Exception {
        PassingTestExample.innerRuleCounter.reset();
        runner.run(notifier);

        assertThat(PassingTestExample.innerRuleCounter.count(),
                Matchers.greaterThan(0));
    }

    @Test
    public void shouldCallOuterRules() throws Exception {
        PassingTestExample.outerRuleCounter.reset();
        runner.run(notifier);

        assertThat(PassingTestExample.outerRuleCounter.count(),
                Matchers.greaterThan(0));
    }

    @Test
    public void shouldCallOuterRulesAroundInnerRules() throws Exception {
        runner.run(notifier);

        int change =
                PassingTestExample.valueOf_innerRuleCounter_whenOuterRuleEnded -
                        PassingTestExample.valueOf_innerRuleCounter_whenOuterRuleStarted;
        assertThat(change, is(1));
    }
}
