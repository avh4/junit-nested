package net.avh4.jrspec.test.support;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class TestForTest {
    private boolean outerSetupWasCalled;

    @Before
    public void setup() throws Exception {
        outerSetupWasCalled = true;
    }

    public class Inner1 {

        private boolean innerSetupWasCalled;
        private boolean valueOf_outerSetupWasCalled_whenInnerSetupWasCalled;

        @Before
        public void setup() throws Exception {
            innerSetupWasCalled = true;
            valueOf_outerSetupWasCalled_whenInnerSetupWasCalled = outerSetupWasCalled;
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
            assertTrue(outerSetupWasCalled);
        }

        @Test
        public void passesUnlessInnerBeforesWereCalledBeforeOuterBefores() throws Exception {
            assertTrue(valueOf_outerSetupWasCalled_whenInnerSetupWasCalled);
        }
    }

    public class Inner2 {

        @Test
        public void test2_1() throws Exception {
            // pass
        }
    }
}
