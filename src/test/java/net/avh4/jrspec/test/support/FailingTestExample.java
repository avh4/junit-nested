package net.avh4.jrspec.test.support;

import org.junit.Test;

import static junit.framework.Assert.fail;

public class FailingTestExample {

    public class Inner1 {

        @Test
        public void alwaysFails() throws Exception {
            fail();
        }

    }
}
