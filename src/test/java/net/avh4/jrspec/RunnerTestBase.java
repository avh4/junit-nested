package net.avh4.jrspec;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.internal.verification.VerificationModeFactory.atLeast;

public abstract class RunnerTestBase {
    protected Runner runner;
    protected Description description;
    protected RunNotifier notifier;

    @Before
    public void setup() throws Exception {
        description = runner.getDescription();
        notifier = Mockito.mock(RunNotifier.class);
    }

    protected void assertSuccessfulTestRunForDescription(Matcher<Description>... requiredTests) {
        final Matcher<Iterable<Description>> includesAllRequiredTests = hasItems(requiredTests);
        final Matcher<Description> matchingAnyOfTheRequiredTests = anyOf(requiredTests);
        final Matcher<Iterable<Failure>> excludesAllRequiredTests
                = everyItem(not(hasDescription(matchingAnyOfTheRequiredTests)));
        assertThat(startedTests(), includesAllRequiredTests);
        assertThat(finishedTests(), includesAllRequiredTests);
        assertThat(testFailures(), excludesAllRequiredTests);
    }

    private List<Description> startedTests() {
        final ArgumentCaptor<Description> captor = ArgumentCaptor.forClass(Description.class);
        Mockito.verify(notifier, atLeastOnce()).fireTestStarted(captor.capture());
        return captor.getAllValues();
    }

    private List<Description> finishedTests() {
        final ArgumentCaptor<Description> captor = ArgumentCaptor.forClass(Description.class);
        Mockito.verify(notifier, atLeastOnce()).fireTestFinished(captor.capture());
        return captor.getAllValues();
    }

    private List<Failure> testFailures() {
        final ArgumentCaptor<Failure> captor = ArgumentCaptor.forClass(Failure.class);
        Mockito.verify(notifier, atLeast(0)).fireTestFailure(captor.capture());
        return captor.getAllValues();
    }

    protected Matcher<Description> hasChild(final Matcher<Description> childMatcher) {
        return new TypeSafeMatcher<Description>() {
            public Matcher<Iterable<Description>> childrenMatcher = hasItem(childMatcher);

            @Override
            protected boolean matchesSafely(Description item) {
                return childrenMatcher.matches(item.getChildren());
            }

            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("a Description with a child matching ");
                description.appendDescriptionOf(childMatcher);
            }

            @Override
            protected void describeMismatchSafely(Description item, org.hamcrest.Description mismatchDescription) {
                appendToDescription(mismatchDescription, item);
            }

            private void appendToDescription(org.hamcrest.Description description, Description item) {
                description.appendText(item.getDisplayName());
                description.appendText("{");
                for (Description child : item.getChildren()) {
                    appendToDescription(description, child);
                }
                description.appendText("}");
            }
        };
    }

    protected Matcher<Description> hasDisplayName(final String displayName) {
        return new TypeSafeMatcher<Description>() {
            @Override
            protected boolean matchesSafely(Description item) {
                return displayName.equals(item.getDisplayName().replaceAll("\\(.*\\)$", ""));
            }

            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("a Description with displayName=");
                description.appendValue(displayName);
            }

            @Override
            protected void describeMismatchSafely(Description item, org.hamcrest.Description mismatchDescription) {
                mismatchDescription.appendText("displayName was ");
                mismatchDescription.appendValue(item.getDisplayName());
            }
        };
    }

    private Matcher<Failure> hasDescription(final Matcher<Description> descriptionMatcher) {
        return new TypeSafeMatcher<Failure>() {
            public Matcher<Description> descriptionFieldMatcher = is(descriptionMatcher);

            @Override
            protected boolean matchesSafely(Failure item) {
                return descriptionFieldMatcher.matches(item.getDescription());
            }

            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("a Failure with Description matching ");
                description.appendDescriptionOf(descriptionMatcher);
            }
        };
    }
}
