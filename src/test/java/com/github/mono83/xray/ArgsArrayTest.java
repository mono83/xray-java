package com.github.mono83.xray;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Iterator;

public class ArgsArrayTest {
    @Test
    public void testSize() {
        Assert.assertEquals(new ArgsArray().size(), 0);
        Assert.assertEquals(new ArgsArray(new ArgNull("foo")).size(), 1);
        Assert.assertEquals(new ArgsArray(new ArgNull("foo"), new ArgNull("bar")).size(), 2);

        // No deduplication
        Assert.assertEquals(new ArgsArray(new ArgNull("foo"), new ArgNull("foo")).size(), 2);
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(new ArgsArray().isEmpty());
        Assert.assertFalse(new ArgsArray(new ArgNull("foo")).isEmpty());
        Assert.assertFalse(new ArgsArray(new ArgNull("foo"), new ArgNull("bar")).isEmpty());
    }

    @Test
    public void testGet() {
        Assert.assertFalse(new ArgsArray().get("foo").isPresent());
        Assert.assertFalse(new ArgsArray(new ArgNull("foo")).get("bar").isPresent());
        Assert.assertTrue(new ArgsArray(new ArgNull("foo")).get("foo").isPresent());

        ArgNull a = new ArgNull("foo");
        Assert.assertSame(new ArgsArray(new ArgNull("foo"), new ArgNull("bar"), a).get("foo").get(), a);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNullCheck() {
        new ArgsArray(new ArgNull("foo")).get(null);
    }

    @Test
    public void testIterator() {
        ArgsArray args = new ArgsArray(
                new ArgNull("foo"),
                new ArgNull("bar"),
                new ArgNull("baz"),
                new ArgNull("foo")
        );

        Iterator<Arg> i = args.iterator();
        Assert.assertTrue(i.hasNext());
        Assert.assertEquals(i.next(), new ArgNull("foo"));
        Assert.assertTrue(i.hasNext());
        Assert.assertEquals(i.next(), new ArgNull("bar"));
        Assert.assertTrue(i.hasNext());
        Assert.assertEquals(i.next(), new ArgNull("baz"));
        Assert.assertTrue(i.hasNext());
        Assert.assertEquals(i.next(), new ArgNull("foo"));
        Assert.assertFalse(i.hasNext());
    }
}