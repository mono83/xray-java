package com.github.mono83.xray.events.consumers.filters;

import com.github.mono83.xray.Args;
import com.github.mono83.xray.events.LoggingEvent;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class LogLevelAtLeastTest {
    @Test(expectedExceptions = NullPointerException.class)
    public void testFirstArgumentNull() {
        new LogLevelAtLeast(null, event -> {});
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testSecondArgumentNull() {
        new LogLevelAtLeast(LoggingEvent.Level.ERROR, null);
    }

    @Test
    public void testAccept() {
        final AtomicInteger ai = new AtomicInteger();
        LogLevelAtLeast f = new LogLevelAtLeast(LoggingEvent.Level.WARNING, ignore -> ai.incrementAndGet());

        f.accept(new LoggingEvent(LoggingEvent.Level.DEBUG, "foo", "bar", Args.of())); // Must not be incremented
        Assert.assertEquals(ai.get(), 0);
        f.accept(new LoggingEvent(LoggingEvent.Level.INFO, "foo", "bar", Args.of())); // Must not be incremented
        Assert.assertEquals(ai.get(), 0);
        f.accept(new LoggingEvent(LoggingEvent.Level.WARNING, "foo", "bar", Args.of())); // Must be incremented
        Assert.assertEquals(ai.get(), 1);
        f.accept(new LoggingEvent(LoggingEvent.Level.ERROR, "foo", "bar", Args.of())); // Must be incremented
        Assert.assertEquals(ai.get(), 2);
        f.accept(new LoggingEvent(LoggingEvent.Level.EMERGENCY, "foo", "bar", Args.of())); // Must be incremented
        Assert.assertEquals(ai.get(), 3);
        f.accept(null); // Must not be incremented
        Assert.assertEquals(ai.get(), 3);
    }

    @Test
    public void testToString() {
        Assert.assertEquals(new LogLevelAtLeast(LoggingEvent.Level.ERROR, new NamedConsumer()).toString(), "[Log level at least ERROR and then Foo]");
    }

    private final class NamedConsumer implements Consumer<LoggingEvent> {
        @Override
        public void accept(final LoggingEvent o) {
        }

        @Override
        public String toString() {
            return "Foo";
        }
    }
}