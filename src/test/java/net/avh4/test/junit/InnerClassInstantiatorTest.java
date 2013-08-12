package net.avh4.test.junit;

import net.avh4.test.junit.test.support.TopLevelClass;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class InnerClassInstantiatorTest {
    private InnerClassInstantiator subject;

    @Before
    public void setUp() throws Exception {
        subject = new InnerClassInstantiator();
    }

    @Test
    public void shouldInstantiateTopLevelClass() throws Exception {
        TopLevelClass instance = subject.instantiate(TopLevelClass.class);

        assertThat(instance, notNullValue());
    }

    @Test
    public void shouldInstantiateFirstLevelInnerClass() throws Exception {
        TopLevelClass.Inner1 instance =
                subject.instantiate(TopLevelClass.Inner1.class);

        assertThat(instance, notNullValue());
    }

    @Test
    public void shouldInstantiateSecondLevelInnerClass() throws Exception {
        TopLevelClass.Inner1.Inner2 instance =
                subject.instantiate(TopLevelClass.Inner1.Inner2.class);

        assertThat(instance, notNullValue());
    }
}
