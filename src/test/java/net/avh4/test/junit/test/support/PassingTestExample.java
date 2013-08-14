package net.avh4.test.junit.test.support;

import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PassingTestExample {
    public static boolean outerBeforeWasCalled;
    public static boolean outerAfterWasCalled;
    public static boolean innerAfterWasCalled;
    public static boolean valueOf_innerAfterWasCalled_whenOuterAfterWasCalled;
    public static Counter innerRuleCounter = new Counter();
    public static Counter outerRuleCounter = new Counter();
    public static int valueOf_innerRuleCounter_whenOuterRuleStarted;
    public static int valueOf_innerRuleCounter_whenOuterRuleEnded;

    @Rule
    public TestRule outerRule = new IncrementWhenCalled(outerRuleCounter);
    @Rule
    public TestRule checkInnerRuleState = new TestRule() {
        @Override
        public Statement apply(final Statement base, Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    valueOf_innerRuleCounter_whenOuterRuleStarted =
                            innerRuleCounter.count();
                    base.evaluate();
                    valueOf_innerRuleCounter_whenOuterRuleEnded =
                            innerRuleCounter.count();
                }
            };
        }
    };

    @Before
    public void setUp() throws Exception {
        outerBeforeWasCalled = true;
    }

    @After
    public void tearDown() throws Exception {
        outerAfterWasCalled = true;
        valueOf_innerAfterWasCalled_whenOuterAfterWasCalled =
                innerAfterWasCalled;
    }

    public class Inner1 {

        private boolean innerSetupWasCalled;
        private boolean valueOf_outerSetupWasCalled_whenInnerSetupWasCalled;

        @Rule
        public TestRule innerRule = new IncrementWhenCalled(innerRuleCounter);

        @Before
        public void setup() throws Exception {
            innerSetupWasCalled = true;
            valueOf_outerSetupWasCalled_whenInnerSetupWasCalled =
                    outerBeforeWasCalled;
        }

        @After
        public void tearDown() throws Exception {
            innerAfterWasCalled = true;
        }

        @Test
        public void test1_1() throws Exception {
            // pass
        }

        @Test
        public void passesUnlessInnerBeforesWereNotCalled() throws Exception {
            assertTrue(innerSetupWasCalled);
        }

        @Test
        public void passesUnlessOuterBeforesWereNotCalled() throws Exception {
            assertTrue(outerBeforeWasCalled);
        }

        @Test
        public void passesUnlessInnerBeforesWereCalledBeforeOuterBefores()
                throws Exception {
            assertTrue(valueOf_outerSetupWasCalled_whenInnerSetupWasCalled);
        }

        @Test
        @Ignore
        public void ignored() throws Exception {
            fail();
        }

    }

    public class Inner2 {

        @Test
        public void test2_1() throws Exception {
            // pass
        }
    }

    @Test
    public void test_1() throws Exception {
        //pass
    }

}
