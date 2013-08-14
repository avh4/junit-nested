[![Build Status](https://secure.travis-ci.org/avh4/junit-nested.png?branch=master)](http://travis-ci.org/avh4/junit-nested)

## Nested tests for JUnit

Add the following dependencies to your `pom.xml`:

```xml
  <dependency>
    <groupId>net.avh4.test</groupId>
    <artifactId>junit-nested</artifactId>
    <version>1.0.1</version>
    <scope>test</scope>
  </dependency>
```

Now write nested tests as shown below.  Your test class can have non-static
inner classes, each of which may contain `@Test` methods.  The inner classes
themselves can also have inner classes, nested to any depth (though you may
want to restructure your tests if you find yourself needing more than two
levels).  `@Before` and `@After` blocks at all levels will be executed as you
would expect (for example, Outer-Before, Inner-Before, Test, Inner-After,
Outer-After).  `@Rule`s at all levels will also be applied in the appropriate
order.

```java
import net.avh4.test.junit.Nested;

@RunWith(Nested.class)
public class QueueTest {
    private Queue<String> subject;

    @Before
    public void setUp() {
        subject = new Queue<String>();
    }

    @Test
    public void push_shouldIncreaseCount() {
        subject.push("Item");
        assertThat(subject.count(), is(1));
    }

    public class WhenEmpty {
        @Test(expected = NoSuchElementException.class)
        public void pop_shouldThrow() {
            subject.pop();
        }
    }

    public class WithOneItem {
        @Before
        public void setUp() {
            subject.push("First");
        }

        @Test
        public void pop_shouldReturnTheItem() {
            assertThat(subject.pop(), is("First"));
        }
    }
}
```

## What works

* `@Before` methods at all levels
* `@After` methods at all levels
* `@Rule` fields at all levels
* `@Test(expected)` for expected exceptions
* `@Test(timeout)` for test timeouts
* `@Ignore` for pending tests

## What doesn't work (yet)

* `@BeforeClass` methods for outer classes
* `@AfterClass` methods for outer classes
* `@Rule` methods (however, @Rule fields do work) for outer classes
* `@RunWith` on inner classes

## License

junit-nested is licensed under the [Eclipse Public License version
1.0](http://opensource.org/licenses/EPL-1.0).  If you have need of a
different license, please contact me.

## References

Below are some resources I investigated before writing junit-nested, which
offer some different approaches to behavioral testing in Java (and other
languages).

* http://jdave.org/ - too different from JUnit
* http://www.malethan.com/article/rspec_for_java_jspec.html - blue sky
* http://igorpolevoy.blogspot.com/2011/04/what-good-for-ruby-is-good-for-java.html - nothing special
* http://www.easyb.org/specexmpls.html  - requires groovy, plus external runner
    * http://www.javaworld.com/javaworld/jw-09-2008/jw-09-easyb.html
* http://www.videosurf.com/video/beyond-test-driven-development-behaviour-driven-development-103819366?vlt - to watch
* https://github.com/thoughtbot/shoulda-context - ruby
    * http://stackoverflow.com/questions/8740765/run-the-same-tests-in-various-context-with-shoulda-context-in-a-rails-app
* http://rspec.info/ - ruby

