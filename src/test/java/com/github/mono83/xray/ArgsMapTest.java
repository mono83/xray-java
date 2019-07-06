package com.github.mono83.xray;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Iterator;

public class ArgsMapTest {
    @Test
    public void testSize() {
        Assert.assertEquals(new ArgsMap().size(), 0);
        Assert.assertEquals(new ArgsMap(new ArgNull("foo")).size(), 1);
        Assert.assertEquals(new ArgsMap(new ArgNull("foo"), new ArgNull("bar")).size(), 2);

        // Deduplication is present
        Assert.assertEquals(new ArgsMap(new ArgNull("foo"), new ArgNull("foo")).size(), 1);
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(new ArgsMap().isEmpty());
        Assert.assertFalse(new ArgsMap(new ArgNull("foo")).isEmpty());
        Assert.assertFalse(new ArgsMap(new ArgNull("foo"), new ArgNull("bar")).isEmpty());
    }

    @Test
    public void testGet() {
        Assert.assertFalse(new ArgsMap().get("foo").isPresent());
        Assert.assertFalse(new ArgsMap(new ArgNull("foo")).get("bar").isPresent());
        Assert.assertTrue(new ArgsMap(new ArgNull("foo")).get("foo").isPresent());

        ArgNull a = new ArgNull("foo");
        Assert.assertSame(new ArgsMap(new ArgNull("foo"), new ArgNull("bar"), a).get("foo").get(), a);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testNullCheck() {
        new ArgsMap(new ArgNull("foo")).get(null);
    }

    @Test
    public void testIterator() {
        ArgsMap args = new ArgsMap(
                new ArgNull("foo"),
                new ArgNull("bar"),
                new ArgNull("baz"),
                new ArgNull("foo")
        );

        Iterator<Arg> i = args.iterator();
        Assert.assertTrue(i.hasNext());
        Assert.assertEquals(i.next(), new ArgNull("bar"));
        Assert.assertTrue(i.hasNext());
        Assert.assertEquals(i.next(), new ArgNull("baz"));
        Assert.assertTrue(i.hasNext());
        Assert.assertEquals(i.next(), new ArgNull("foo"));
        Assert.assertFalse(i.hasNext());
    }
}