package example;

import net.avh4.jrspec.JrSpec;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.PriorityQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(JrSpec.class)
public class QueueTest {

    protected PriorityQueue<String> subject;

    @Before
    public void setup() throws Exception {
        subject = new PriorityQueue<String>();
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
