package net.avh4.jrspec;

import net.avh4.jrspec.test.support.TestForTest;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class JrSpecTest extends RunnerTestBase {

    @Before
    public void setup() throws Exception {
        runner = new JrSpec(TestForTest.class);
        super.setup();
    }

    @Test
    public void shouldDescribeSuite() throws Exception {
        assertThat(description,
                hasDisplayName("net.avh4.jrspec.test.support.TestForTest"));
    }

    @Test
    public void shouldDescribeFirstInnerSuite() throws Exception {
        assertThat(description, hasChild(hasDisplayName(
                "net.avh4.jrspec.test.support.TestForTest$Inner1")));
    }

    @Test
    public void shouldDescribeManyInnerSuites() throws Exception {
        assertThat(description, hasChild(hasDisplayName(
                "net.avh4.jrspec.test.support.TestForTest$Inner2")));
    }

    @Test
    public void shouldDescribeFirstTestMethodInFirstInnerSuite()
            throws Exception {
        assertThat(description, hasChild(hasChild(hasDisplayName("test1_1"))));
    }

    @Test
    public void shouldRunSuite() throws Exception {
        runner.run(notifier);

        assertSuccessfulTestRunForDescription(
                hasDisplayName("net.avh4.jrspec.test.support.TestForTest"));
    }

    @Test
    public void shouldRunInnerSuites() throws Exception {
        runner.run(notifier);

        assertSuccessfulTestRunForDescription(hasDisplayName(
                "net.avh4.jrspec.test.support.TestForTest$Inner1"),
                hasDisplayName(
                        "net.avh4.jrspec.test.support.TestForTest$Inner2"));
    }

    @Test
    public void shouldRunInnerSuiteTestMethods() throws Exception {
        runner.run(notifier);

        assertSuccessfulTestRunForDescription(hasDisplayName("test1_1"),
                hasDisplayName("test2_1"));
    }

}
