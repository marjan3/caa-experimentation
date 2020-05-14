package hamcrest.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;

public final class FailWithMatcher<E extends Throwable>
        extends TypeSafeMatcher<IThrowingRunnable<E>> {

    private final Matcher<? super E> matcher;

    private FailWithMatcher(final Matcher<? super E> matcher) {
        this.matcher = matcher;
    }

    public static <E extends Throwable> Matcher<IThrowingRunnable<E>> failsWith(
            Class<E> throwableType) {
        return new FailWithMatcher<>(instanceOf(throwableType));
    }

    public static <E extends Throwable> Matcher<IThrowingRunnable<E>> failsWith(
            Class<E> throwableType,
            Matcher<? super E> throwableMatcher) {
        Matcher<E> matcher = allOf(instanceOf(throwableType), throwableMatcher);
        return new FailWithMatcher<>(matcher);
    }

    @Override
    protected boolean matchesSafely(IThrowingRunnable<E> runnable) {
        try {
            runnable.run();
            return false;
        } catch (final Throwable ex) {
            return matcher.matches(ex);
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("fails with ").appendDescriptionOf(matcher);
    }

}
