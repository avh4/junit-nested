package net.avh4.test.junit;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.AnyOf;
import org.junit.Before;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
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

    protected void assertSuccessfulTestRunForDescription(
            Matcher<Description>... requiredTests) {
        assertThat(startedTests(), hasItems(requiredTests));
        assertThat(finishedTests(), hasItems(requiredTests));
        assertThat(testFailures(), doesNotIncludeFailuresFor(requiredTests));
    }

    protected void assertFailedTestRunForDescription(
            Matcher<Description>... requiredTests) {
        assertThat(startedTests(), hasItems(requiredTests));
        assertThat(finishedTests(), doesNotInclude(requiredTests));
        assertThat(testFailures(), includesFailuresFor(requiredTests));
    }

    private <T> Matcher<Iterable<T>> doesNotInclude(Matcher<T>... items) {
        AnyOf<T> anyOfTheItems = anyOf(items);
        return everyItem(not(anyOfTheItems));
    }

    private Matcher<? super List<Failure>> includesFailuresFor(
            Matcher<Description>... requiredTests) {
        Matcher<Failure>[] requiredFailures = new Matcher[requiredTests.length];
        for (int i = 0; i < requiredTests.length; i++) {
            requiredFailures[i] = hasDescription(requiredTests[i]);
        }
        return hasItems(requiredFailures);
    }

    private Matcher<Iterable<Failure>> doesNotIncludeFailuresFor(
            Matcher<Description>... requiredTests) {
        final Matcher<Description> matchingAnyOfTheRequiredTests =
                anyOf(requiredTests);
        return everyItem(not(hasDescription(matchingAnyOfTheRequiredTests)));
    }

    private List<Description> startedTests() {
        final ArgumentCaptor<Description> captor =
                ArgumentCaptor.forClass(Description.class);
        Mockito.verify(notifier, atLeastOnce())
                .fireTestStarted(captor.capture());
        return captor.getAllValues();
    }

    private List<Description> finishedTests() {
        final ArgumentCaptor<Description> captor =
                ArgumentCaptor.forClass(Description.class);
        Mockito.verify(notifier, atLeastOnce())
                .fireTestFinished(captor.capture());
        return captor.getAllValues();
    }

    private List<Failure> testFailures() {
        final ArgumentCaptor<Failure> captor =
                ArgumentCaptor.forClass(Failure.class);
        Mockito.verify(notifier, atLeast(0)).fireTestFailure(captor.capture());
        return captor.getAllValues();
    }

    protected Matcher<Description> hasChild(
            final Matcher<Description> childMatcher) {
        return new TypeSafeMatcher<Description>() {
            public Matcher<Iterable<? super Description>> childrenMatcher =
                    hasItem(childMatcher);

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
            protected void describeMismatchSafely(Description item,
                                                  org.hamcrest.Description mismatchDescription) {
                appendToDescription(mismatchDescription, item);
            }

            private void appendToDescription(
                    org.hamcrest.Description description, Description item) {
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
                return displayName.equals(item.getDisplayName()
                        .replaceAll("\\(.*\\)$", ""));
            }

            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("a Description with displayName=");
                description.appendValue(displayName);
            }

            @Override
            protected void describeMismatchSafely(Description item,
                                                  org.hamcrest.Description mismatchDescription) {
                mismatchDescription.appendText("displayName was ");
                mismatchDescription.appendValue(item.getDisplayName());
            }
        };
    }

    private Matcher<Failure> hasDescription(
            final Matcher<Description> descriptionMatcher) {
        return new TypeSafeMatcher<Failure>() {
            public Matcher<Description> descriptionFieldMatcher =
                    is(descriptionMatcher);

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
