package com.github.mono83.xray;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Iterator;

public class ArgsCascadeTest {
    @Test
    public void testIterator() {
        ArgsArray argsBase = new ArgsArray(
                new ArgNull("foo"),
                new ArgNull("bar"),
                new ArgNull("baz")
        );

        ArgsCascade args = new ArgsCascade(
                new ArgNull("foo"),
                argsBase
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

    @Test
    public void testIteratorSingle() {
        ArgsCascade args = new ArgsCascade(
                new ArgNull("foo"),
                null
        );
        Iterator<Arg> i = args.iterator();
        Assert.assertTrue(i.hasNext());
        Assert.assertEquals(i.next(), new ArgNull("foo"));
        Assert.assertFalse(i.hasNext());
    }
}