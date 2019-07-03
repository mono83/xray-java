package com.github.mono83;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class ArgGenericTest {
    @Test
    public void testStandardMethods() {
        ArgGeneric<Duration> a = new ArgGeneric<>("foo", new Duration[]{
                Duration.ofMinutes(1),
                Duration.ofSeconds(5)
        });

        Assert.assertEquals(a.getKey(), "foo");
        Assert.assertEquals(a.size(), 2);
        Assert.assertEquals(a.getValue(0), "PT1M");
        Assert.assertEquals(a.getValue(1), "PT5S");
        Assert.assertEquals(a.getValue(1300), "PT5S"); // Last one
        Assert.assertEquals(a.getValue(-1), "PT5S"); // Last one
        Assert.assertEquals(a.get(0), Duration.ofMinutes(1));
        Assert.assertEquals(a.get(1), Duration.ofSeconds(5));
        Assert.assertFalse(a.isEmpty());
    }

    @Test(dependsOnMethods = "testStandardMethods")
    public void testEmpty() {
        ArgGeneric<String> a = new ArgGeneric<>("bar", new String[0]);
        Assert.assertEquals(a.getKey(), "bar");
        Assert.assertEquals(a.size(), 0);
        Assert.assertEquals(a.getValue(0), "");
        Assert.assertEquals(a.getValue(1), "");
        Assert.assertEquals(a.getValue(-1), "");
        Assert.assertTrue(a.isEmpty());
    }

    @Test(dependsOnMethods = "testEmpty", expectedExceptions = IndexOutOfBoundsException.class)
    public void testOutOfBoundsGet() {
        ArgGeneric<String> a = new ArgGeneric<>("bar", new String[0]);
        a.get(0);
    }

    @Test(dependsOnMethods = "testStandardMethods")
    public void testEqualsAndHashCode() {
        ArgGeneric<Duration> a = new ArgGeneric<>("foo", new Duration[]{Duration.ofMinutes(3)});
        ArgGeneric<Duration> b = new ArgGeneric<>("bar", new Duration[0]);
        ArgGeneric<Duration> c = new ArgGeneric<>("foo", new Duration[]{Duration.ofMinutes(3)});
        ArgGeneric<Duration> d = new ArgGeneric<>("foo", new Duration[]{Duration.ofSeconds(3)});
        ArgGeneric<Duration> e = new ArgGeneric<>("foo", new Duration[]{Duration.ofMinutes(3), Duration.ofSeconds(3)});

        // Equality
        Assert.assertEquals(a, a);
        Assert.assertEquals(a, c);
        Assert.assertEquals(c, a);
        Assert.assertNotEquals(a, b);
        Assert.assertNotEquals(b, a);
        Assert.assertNotEquals(b, c);
        Assert.assertNotEquals(c, b);
        Assert.assertNotEquals(a, d);
        Assert.assertNotEquals(a, e);
        Assert.assertFalse(a.equals(null));

        // Hash code
        Assert.assertEquals(a.hashCode(), 3149966);
        Assert.assertEquals(b.hashCode(), 3017231);
        Assert.assertEquals(c.hashCode(), 3149966);
        Assert.assertEquals(d.hashCode(), 3149789);
        Assert.assertEquals(e.hashCode(), 3156299);
    }

    @Test(dependsOnMethods = "testEqualsAndHashCode")
    public void testStaticOfGenerics() {
        Object o1 = new Object();
        Object o2 = new Object();

        Assert.assertEquals(ArgGeneric.of("foo", new Object[]{o1}), new ArgGeneric<>("foo", new Object[]{o1}));
        Assert.assertEquals(ArgGeneric.of("bar", new Object[]{o1, o2}), new ArgGeneric<>("bar", new Object[]{o1, o2}));
        Assert.assertEquals(ArgGeneric.of("baz", (Object[]) null), new ArgNull("baz"));
        Assert.assertEquals(ArgGeneric.of("xxx", new Object[0]), new ArgNull("xxx"));
    }

    @Test(dependsOnMethods = "testEqualsAndHashCode")
    public void testStaticOfStrings() {
        Assert.assertEquals(ArgGeneric.of("foo", "a"), new ArgGeneric<>("foo", new String[]{"a"}));
        Assert.assertEquals(ArgGeneric.of("bar", "a", "b"), new ArgGeneric<>("bar", new String[]{"a", "b"}));
        Assert.assertEquals(ArgGeneric.of("baz", (String[]) null), new ArgNull("baz"));
        Assert.assertEquals(ArgGeneric.of("xxx", new String[0]), new ArgNull("xxx"));
    }

    @Test(dependsOnMethods = "testEqualsAndHashCode")
    public void testStaticOfEnums() {
        Assert.assertEquals(ArgGeneric.of("foo", TestEnum.TRUE), new ArgGeneric<>("foo", new TestEnum[]{TestEnum.TRUE}));
        Assert.assertEquals(ArgGeneric.of("bar", TestEnum.TRUE, TestEnum.FALSE), new ArgGeneric<>("bar", new TestEnum[]{TestEnum.TRUE, TestEnum.FALSE}));
        Assert.assertEquals(ArgGeneric.of("baz", (TestEnum[]) null), new ArgNull("baz"));
        Assert.assertEquals(ArgGeneric.of("xxx", new TestEnum[0]), new ArgNull("xxx"));
    }

    private enum TestEnum {
        TRUE, FALSE;
    }
}