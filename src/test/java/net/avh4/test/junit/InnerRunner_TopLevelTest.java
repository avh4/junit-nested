package net.avh4.test.junit;

import net.avh4.test.junit.test.support.PassingTestExample;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class InnerRunner_TopLevelTest extends RunnerTestBase {

    @Before
    public void setup() throws Exception {
        runner = new InnerRunner(PassingTestExample.class);
        super.setup();
    }

    @Test
    public void shouldRunSuccessfulTest() throws Exception {
        runner.run(notifier);

        assertSuccessfulTestRunForDescription(hasDisplayName("test_1"));
    }

    @Test
    public void shouldCallBeforeMethods() throws Exception {
        PassingTestExample.outerBeforeWasCalled = false;
        runner.run(notifier);

        assertTrue(PassingTestExample.outerBeforeWasCalled);
    }

    @Test
    public void shouldCallAfterMethods() throws Exception {
        PassingTestExample.outerAfterWasCalled = false;
        runner.run(notifier);

        assertTrue(PassingTestExample.outerAfterWasCalled);
    }

    @Test
    public void shouldCallRules() throws Exception {
        PassingTestExample.outerRuleCounter.reset();
        runner.run(notifier);

        assertThat(PassingTestExample.outerRuleCounter.count(),
                Matchers.greaterThan(0));
    }
}
