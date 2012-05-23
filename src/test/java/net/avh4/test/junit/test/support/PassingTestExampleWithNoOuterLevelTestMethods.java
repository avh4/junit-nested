package net.avh4.test.junit.test.support;

import org.junit.Test;

public class PassingTestExampleWithNoOuterLevelTestMethods {

    public class Inner1 {

        @Test
        public void test1_1() throws Exception {
            // pass
        }
    }

    public class Inner2 {

        @Test
        public void test2_1() throws Exception {
            // pass
        }
    }
}
