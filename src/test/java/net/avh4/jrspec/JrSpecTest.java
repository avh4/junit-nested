package net.avh4.jrspec;

import net.avh4.jrspec.test.support.PassingTestExample;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class JrSpecTest extends RunnerTestBase {

    @Before
    public void setup() throws Exception {
        runner = new JrSpec(PassingTestExample.class);
        super.setup();
    }

    @Test
    public void shouldDescribeSuite() throws Exception {
        assertThat(description,
                hasDisplayName(PassingTestExample.class.getName()));
    }

    @Test
    public void shouldDescribeFirstInnerSuite() throws Exception {
        assertThat(description, hasChild(
                hasDisplayName(PassingTestExample.Inner1.class.getName())));
    }

    @Test
    public void shouldDescribeManyInnerSuites() throws Exception {
        assertThat(description, hasChild(
                hasDisplayName(PassingTestExample.Inner2.class.getName())));
    }

    @Test
    public void shouldDescribeFirstTestMethodInFirstInnerSuite()
            throws Exception {
        assertThat(description, hasChild(hasChild(hasDisplayName("test1_1"))));
    }

    @Test
    public void shouldRunInnerSuiteTestMethods() throws Exception {
        runner.run(notifier);

        assertSuccessfulTestRunForDescription(hasDisplayName("test1_1"),
                hasDisplayName("test2_1"));
    }

}
