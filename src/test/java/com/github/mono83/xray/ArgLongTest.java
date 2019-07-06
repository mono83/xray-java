package com.github.mono83.xray;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ArgLongTest {
    @Test
    public void testStandardMethods() {
        ArgLong a = new ArgLong("foo", 18, -42);

        Assert.assertEquals(a.getKey(), "foo");
        Assert.assertEquals(a.size(), 2);
        Assert.assertEquals(a.getValue(0), "18");
        Assert.assertEquals(a.getValue(1), "-42");
        Assert.assertEquals(a.getValue(1300), "-42"); // Last one
        Assert.assertEquals(a.getValue(-1), "-42"); // Last one
        Assert.assertEquals(a.get(0), 18);
        Assert.assertEquals(a.get(1), -42L);
        Assert.assertFalse(a.isEmpty());
    }

    @Test(dependsOnMethods = "testStandardMethods")
    public void testEmpty() {
        ArgLong a = new ArgLong("bar");
        Assert.assertEquals(a.getKey(), "bar");
        Assert.assertEquals(a.size(), 0);
        Assert.assertEquals(a.getValue(0), "");
        Assert.assertEquals(a.getValue(1), "");
        Assert.assertEquals(a.getValue(-1), "");
        Assert.assertTrue(a.isEmpty());
    }

    @Test(dependsOnMethods = "testEmpty", expectedExceptions = IndexOutOfBoundsException.class)
    public void testOutOfBoundsGet() {
        ArgLong a = new ArgLong("foo");
        a.get(0);
    }

    @Test(dependsOnMethods = "testStandardMethods")
    public void testEqualsAndHashCode() {
        ArgLong a = new ArgLong("foo", 8);
        ArgLong b = new ArgLong("bar");
        ArgLong c = new ArgLong("foo", 8);
        ArgLong d = new ArgLong("foo", 9);
        ArgLong e = new ArgLong("foo", 9, 8);

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
        Assert.assertEquals(a.hashCode(), 3149794);
        Assert.assertEquals(b.hashCode(), 3017231);
        Assert.assertEquals(c.hashCode(), 3149794);
        Assert.assertEquals(d.hashCode(), 3149795);
        Assert.assertEquals(e.hashCode(), 3151003);
    }

    @Test(dependsOnMethods = "testEqualsAndHashCode")
    public void testStaticOfLongs() {
        Assert.assertEquals(ArgLong.of("foo", 10L), new ArgLong("foo", 10L));
        Assert.assertEquals(ArgLong.of("bar", -1L, 25L), new ArgLong("bar", -1L, 25L));
        Assert.assertEquals(ArgLong.of("baz"), new ArgNull("baz"));
        Assert.assertEquals(ArgLong.of("xxx", new long[0]), new ArgNull("xxx"));
    }

    @Test(dependsOnMethods = "testEqualsAndHashCode")
    public void testStaticOfInts() {
        Assert.assertEquals(ArgLong.of("foo", 10), new ArgLong("foo", 10L));
        Assert.assertEquals(ArgLong.of("bar", -8, 25), new ArgLong("bar", -8L, 25L));
        Assert.assertEquals(ArgLong.of("baz"), new ArgNull("baz"));
        Assert.assertEquals(ArgLong.of("xxx", new int[0]), new ArgNull("xxx"));
    }
}