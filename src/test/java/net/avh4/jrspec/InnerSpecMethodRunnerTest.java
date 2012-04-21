package net.avh4.jrspec;

import net.avh4.jrspec.test.support.TestForTest;
import org.junit.Before;
import org.junit.Test;

public class InnerSpecMethodRunnerTest extends RunnerTestBase {

    @Before
    public void setup() throws Exception {
        runner = new InnerSpecMethodRunner(TestForTest.Inner1.class, TestForTest.class);
        super.setup();
    }

    @Test
    public void shouldRunSuccessfulTest() throws Exception {
        runner.run(notifier);

        assertSuccessfulTestRunForDescription(
                hasDisplayName("test1_1")
        );
    }
}
