package com.github.mono83.xray.events;

import com.github.mono83.xray.Args;
import com.github.mono83.xray.Names;
import com.github.mono83.xray.generators.Incremental;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class RayTest {
    @Test
    public void testStaticConcat() {
        String a = "";
        String b = "foo";

        Assert.assertSame(Ray.concat(a, ""), a);
        Assert.assertNull(Ray.concat(a, null));
        Assert.assertSame(Ray.concat(a, b), b);
        Assert.assertSame(Ray.concat(b, a), b);
        Assert.assertEquals(Ray.concat(" foo ", " bar "), "foo.bar");
    }

    @Test
    public void testCreate() {
        Incremental gen = new Incremental();
        Ray r = new Ray("foo", "bar", gen);

        Assert.assertNull(r.parent);
        Assert.assertSame(r.rayIdGenerator, gen);
        Assert.assertEquals(r.id, "ROOT");
        Assert.assertEquals(r.name, "foo");
        Assert.assertEquals(r.metricPrefix, "bar");
        Assert.assertNotNull(r.args);
        Assert.assertEquals(r.args.size(), 0);
        Assert.assertSame(r.getArgs(), r.args);
    }

    @Test(dependsOnMethods = "testCreate")
    public void testComposeMetricName() {
        Ray r = new Ray("foo", "bar", new Incremental());

        Assert.assertEquals(r.composeMetricName(""), "bar");
        Assert.assertEquals(r.composeMetricName("baz"), "bar.baz");
    }

    @Test(dependsOnMethods = "testCreate")
    public void testWithArgs() {
        Ray r = new Ray("foo", "bar", new Incremental());
        Ray r2 = r.with(Names.ID.of(55));

        Assert.assertSame(r.with(), r);
        Assert.assertNotSame(r2, r);
        Assert.assertSame(r2.parent, r);
        Assert.assertSame(r.id, r2.id);
        Assert.assertSame(r.name, r2.name);
        Assert.assertSame(r.metricPrefix, r2.metricPrefix);
        Assert.assertSame(r.rayIdGenerator, r2.rayIdGenerator);
        Assert.assertEquals(r2.getArgs().size(), 1);
        Assert.assertTrue(r2.getArgs().get("id").isPresent());
    }

    @Test(dependsOnMethods = "testCreate")
    public void withStringName() {
        Ray r = new Ray("foo", "bar", new Incremental());
        Ray r2 = r.with("new");

        Assert.assertSame(r.with((String) null), r);
        Assert.assertSame(r.with(""), r);
        Assert.assertSame(r2.parent, r);
        Assert.assertSame(r.id, r2.id);
        Assert.assertEquals(r2.name, "new");
        Assert.assertSame(r.metricPrefix, r2.metricPrefix);
        Assert.assertSame(r.rayIdGenerator, r2.rayIdGenerator);
        Assert.assertSame(r.args, r2.args);
    }

    @Test(dependsOnMethods = "testCreate")
    public void withSuppliedName() {
        Ray r = new Ray("foo", "bar", new Incremental());
        Ray r2 = r.with(()-> "new");

        Assert.assertSame(r.with((String) null), r);
        Assert.assertSame(r.with(""), r);
        Assert.assertSame(r2.parent, r);
        Assert.assertSame(r.id, r2.id);
        Assert.assertEquals(r2.name, "new");
        Assert.assertSame(r.metricPrefix, r2.metricPrefix);
        Assert.assertSame(r.rayIdGenerator, r2.rayIdGenerator);
        Assert.assertSame(r.args, r2.args);
    }

    @Test
    public void withNameAndMetricPrefix() {
        Ray r = new Ray("foo", "bar", new Incremental());
        Ray r2 = r.with("new", "baz");

        Assert.assertSame(r.with((String) null), r);
        Assert.assertSame(r.with(""), r);
        Assert.assertSame(r2.parent, r);
        Assert.assertSame(r.id, r2.id);
        Assert.assertEquals(r2.name, "new");
        Assert.assertEquals(r2.metricPrefix, "bar.baz");
        Assert.assertSame(r.rayIdGenerator, r2.rayIdGenerator);
        Assert.assertSame(r.args, r2.args);
    }

    @Test(dependsOnMethods = "testWithArgs")
    public void testFork() {
        Ray r = new Ray("foo", "bar", new Incremental());
        Ray r2 = r.fork();
        Assert.assertSame(r.with(), r);
        Assert.assertNotSame(r2, r);
        Assert.assertSame(r2.parent, r);
        Assert.assertEquals(r.id, "ROOT");
        Assert.assertEquals(r2.id, "1");
        Assert.assertSame(r.name, r2.name);
        Assert.assertSame(r.metricPrefix, r2.metricPrefix);
        Assert.assertSame(r.rayIdGenerator, r2.rayIdGenerator);
        Assert.assertSame(r.args, r2.args);

        Assert.assertEquals(r.fork().id, "2");
    }

    @Test(dependsOnMethods = "testCreate")
    public void testHandleLogs() {
        Ray r = new Ray("foo", "bar", new Incremental());

        Assert.assertNull(r.consumersLog);
        r.handleLogs(null);
        Assert.assertNull(r.consumersLog);

        Consumer<LoggingEvent> a = (x) -> {};
        Consumer<LoggingEvent> b = (x) -> {};
        r.handleLogs(a);
        Assert.assertEquals(r.consumersLog.length, 1);
        Assert.assertSame(r.consumersLog[0], a);
        r.handleLogs(b);
        Assert.assertEquals(r.consumersLog.length, 2);
        Assert.assertSame(r.consumersLog[0], a);
        Assert.assertSame(r.consumersLog[1], b);
    }

    @Test(dependsOnMethods = "testCreate")
    public void testHandleMetrics() {
        Ray r = new Ray("foo", "bar", new Incremental());

        Assert.assertNull(r.consumersMetric);
        r.handleLogs(null);
        Assert.assertNull(r.consumersMetric);

        Consumer<MetricEvent> a = (x) -> {};
        Consumer<MetricEvent> b = (x) -> {};
        r.handleMetrics(a);
        Assert.assertEquals(r.consumersMetric.length, 1);
        Assert.assertSame(r.consumersMetric[0], a);
        r.handleMetrics(b);
        Assert.assertEquals(r.consumersMetric.length, 2);
        Assert.assertSame(r.consumersMetric[0], a);
        Assert.assertSame(r.consumersMetric[1], b);
    }

    @Test(dependsOnMethods = {"testHandleLogs", "testHandleMetrics"})
    public void testEmit() {
        LoggingEvent e1 = new LoggingEvent(LoggingEvent.Level.ALERT, "foo", "", Args.of());
        MetricEvent e2 = new MetricEvent(MetricEvent.Type.GAUGE, "bar", 12, Args.of());

        AtomicInteger ai = new AtomicInteger();

        Consumer<LoggingEvent> a = (x) -> {
            ai.incrementAndGet();
            Assert.assertSame(x, e1);
        };
        Consumer<MetricEvent> b = (x) -> {
            ai.incrementAndGet();
            Assert.assertSame(x, e2);
        };

        Ray r = new Ray("foo", "bar", new Incremental());
        r.emit(e1);
        r.emit(e2);
        r.handleLogs(a);
        r.handleMetrics(b);
        r.emit(e1);
        r.emit(e2);
        r.emit((LoggingEvent) null);
        r.emit((MetricEvent) null);

        Assert.assertEquals(ai.get(), 2, "Expected two invocations");

        r.fork().emit(e1);
        r.with("name").emit(e1);
        r.with("name", "xxx").emit(e1);
        r.fork().emit(e2);
        r.with("name").emit(e2);
        r.with("name", "xxx").emit(e2);

        Assert.assertEquals(ai.get(), 8, "Expected eight invocations");
    }
}