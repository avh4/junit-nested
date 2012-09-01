package example;

import net.avh4.test.junit.Nested;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.PriorityQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;

@RunWith(Nested.class)
public class QueueTest {

    protected PriorityQueue<String> subject;

    @Before
    public void setup() throws Exception {
        subject = new PriorityQueue<String>();
    }

    @Test
    public void shouldStartEmpty() throws Exception {
        assertThat(subject.isEmpty(), is(true));
    }

    public class WhenEmpty {
        @Before
        public void setup() throws Exception {
            subject.clear();
        }

        @Test
        public void shouldHaveZeroItems() throws Exception {
            assertThat(subject.size(), is(0));
        }

        @Test
        @Ignore
        public void shouldKneelWhenTheQueenPasses() throws Exception {
            fail("This test should be ignored");
        }

        @Test(expected = NullPointerException.class)
        public void shouldThrowWhenAddingNull() throws Exception {
            subject.add(null);
        }
    }

    public class WithOneItem {
        @Before
        public void setup() throws Exception {
            subject.add("Item");
        }

        @Test
        public void shouldHaveOneItem() throws Exception {
            assertThat(subject.size(), is(1));
        }

        public class Clear {
            @Before
            public void setup() throws Exception {
                subject.clear();
            }

            @Test
            public void shouldEmptyTheQueue() throws Exception {
                assertThat(subject.size(), is(0));
            }
        }
    }
}
