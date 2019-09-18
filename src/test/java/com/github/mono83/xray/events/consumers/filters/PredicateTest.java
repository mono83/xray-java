package com.github.mono83.xray.events.consumers.filters;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class PredicateTest {
    @Test(expectedExceptions = NullPointerException.class)
    public void testFirstArgumentNull() {
        new Predicate<>(null, o -> {});
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testSecondArgumentNull() {
        new Predicate<>(o -> true, null);
    }

    @Test
    public void testAccept() {
        final AtomicInteger ai = new AtomicInteger();
        Predicate<Boolean> f = new Predicate<>(value -> value, ignore -> ai.incrementAndGet());

        f.accept(false); // Must not be incremented
        Assert.assertEquals(ai.get(), 0);
        f.accept(true); // Must be incremented
        Assert.assertEquals(ai.get(), 1);
        f.accept(null); // Must not be incremented
        Assert.assertEquals(ai.get(), 1);
    }

    @Test
    public void testToString() {
        Assert.assertEquals(new Predicate<>(new NamedPredicate(), new NamedConsumer()).toString(), "[Match Bar and then Foo]");
    }

    private final class NamedConsumer implements Consumer<Object> {
        @Override
        public void accept(final Object o) {
        }

        @Override
        public String toString() {
            return "Foo";
        }
    }

    private final class NamedPredicate implements java.util.function.Predicate<Object> {
        @Override
        public boolean test(final Object o) {
            return false;
        }

        @Override
        public String toString() {
            return "Bar";
        }
    }
}