package com.github.mono83.xray;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ArgNullTest {
    @Test
    public void testStandardMethods() {
        ArgNull a = new ArgNull("foo");
        Assert.assertEquals(a.getName(), "foo");
        Assert.assertEquals(a.size(), 0);
        Assert.assertEquals(a.getValue(0), "");
        Assert.assertEquals(a.getValue(1), "");
        Assert.assertEquals(a.getValue(-1), "");
        Assert.assertTrue(a.isEmpty());
    }

    @Test(expectedExceptions = NullPointerException.class, dependsOnMethods = "testStandardMethods")
    public void testNullConstructor() {
        new ArgNull(null);
    }

    @Test(dependsOnMethods = "testStandardMethods")
    public void testEqualsAndHashCode() {
        ArgNull a = new ArgNull("foo");
        ArgNull b = new ArgNull("bar");
        ArgNull c = new ArgNull("foo");

        // Equality
        Assert.assertEquals(a, a);
        Assert.assertEquals(a, c);
        Assert.assertEquals(c, a);
        Assert.assertNotEquals(a, b);
        Assert.assertNotEquals(b, a);
        Assert.assertNotEquals(b, c);
        Assert.assertNotEquals(c, b);
        Assert.assertFalse(a.equals(null));

        // Hash code
        Assert.assertEquals(a.hashCode(), a.getName().hashCode());
        Assert.assertEquals(b.hashCode(), b.getName().hashCode());
        Assert.assertEquals(c.hashCode(), c.getName().hashCode());
    }
}