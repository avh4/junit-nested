package net.avh4.test.junit;

import net.avh4.test.junit.test.support.PassingTestExample;
import net.avh4.test.junit.test.support.PassingTestExampleWithNoOuterLevelTestMethods;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class NestedTest extends RunnerTestBase {

    @Before
    public void setup() throws Exception {
        runner = new Nested(PassingTestExample.class);
        super.setup();
    }

    @Test
    public void shouldDescribeSuite() throws Exception {
        assertThat(description,
                hasDisplayName(PassingTestExample.class.getName()));
    }

    @Test
    public void shouldDescribeFirstInnerSuite() throws Exception {
        assertThat(description, hasChild(hasDisplayName("Inner1")));
    }

    @Test
    public void shouldDescribeManyInnerSuites() throws Exception {
        assertThat(description, hasChild(hasDisplayName("Inner2")));
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

    @Test
    public void shouldDescribeOuterTestMethods() throws Exception {
        assertThat(description, hasChild(hasDisplayName("test_1")));
    }

    @Test
    public void shouldRunOuterTestMethods() throws Exception {
        runner.run(notifier);

        assertSuccessfulTestRunForDescription(hasDisplayName("test_1"));
    }

    @Test
    public void shouldRunInnerSuitesWhenNoOuterTestMethodsExist()
            throws Exception {
        runner = new Nested(PassingTestExampleWithNoOuterLevelTestMethods
                .class);
        runner.run(notifier);

        assertSuccessfulTestRunForDescription(hasDisplayName("test1_1"),
                hasDisplayName("test2_1"));
    }

    @Test
    public void shouldDescribeSelfWhenNoOuterTestMethodsExist()
            throws Exception {
        runner = new Nested(PassingTestExampleWithNoOuterLevelTestMethods
                .class);
        runner.run(notifier);
        description = runner.getDescription();

        assertThat(description, hasDisplayName(
                PassingTestExampleWithNoOuterLevelTestMethods.class.getName()));
    }
}
