package com.github.mono83.xray;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ArgExceptionTest {
    @Test
    public void testStandardMethods() {
        RuntimeException foo = new RuntimeException("This is exception");
        ArgException a = new ArgException("foo", foo);

        Assert.assertEquals(a.getKey(), "foo");
        Assert.assertEquals(a.size(), 1);
        Assert.assertEquals(a.getValue(0), "This is exception");
        Assert.assertEquals(a.getValue(1300), "This is exception"); // Last one
        Assert.assertEquals(a.getValue(-1), "This is exception"); // Last one
        Assert.assertSame(a.getException(), foo);
        Assert.assertFalse(a.isEmpty());
    }

    @Test(dependsOnMethods = "testStandardMethods", expectedExceptions = NullPointerException.class)
    public void testMandatoryConstructorKey() {
        new ArgException(null, new RuntimeException());
    }

    @Test(dependsOnMethods = "testStandardMethods", expectedExceptions = NullPointerException.class)
    public void testMandatoryConstructorValue() {
        new ArgException("foo", null);
    }

    @Test(dependsOnMethods = "testStandardMethods", expectedExceptions = NullPointerException.class)
    public void testEmpty() {
        new ArgException("foo", null);
    }

    @Test(dependsOnMethods = "testStandardMethods")
    public void testNullMessage() {
        ArgException a = new ArgException("foo", new RuntimeException((String) null));
        Assert.assertEquals(a.getKey(), "foo");
        Assert.assertEquals(a.size(), 1);
        Assert.assertEquals(a.getValue(0), "java.lang.RuntimeException");
    }

    @Test(dependsOnMethods = "testStandardMethods")
    public void testEquals() {
        RuntimeException foo = new RuntimeException("This is exception");
        ArgException a = new ArgException("foo", foo);
        ArgException b = new ArgException("bar", foo);
        ArgException c = new ArgException("foo", foo);
        ArgException d = new ArgException("foo", new RuntimeException("This is exception"));

        // Equality
        Assert.assertEquals(a, a);
        Assert.assertEquals(a, c);
        Assert.assertEquals(c, a);
        Assert.assertNotEquals(a, b);
        Assert.assertNotEquals(a, d);
        Assert.assertFalse(a.equals(null));
    }

    @Test(dependsOnMethods = "testEquals")
    public void testStaticOf() {
        RuntimeException foo = new RuntimeException("This is exception");

        Assert.assertEquals(ArgException.of("foo", foo), new ArgException("foo", foo));
        Assert.assertEquals(ArgException.of("foo", null), new ArgNull("foo"));
    }
}