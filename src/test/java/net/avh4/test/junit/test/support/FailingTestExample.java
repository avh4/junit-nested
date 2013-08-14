package net.avh4.test.junit.test.support;

import org.junit.Test;

import static org.junit.Assert.fail;


public class FailingTestExample {

    public class Inner1 {

        @Test
        public void alwaysFails() throws Exception {
            fail();
        }

    }
}
