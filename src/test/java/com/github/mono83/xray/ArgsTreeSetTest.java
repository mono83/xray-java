package com.github.mono83.xray;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class ArgsTreeSetTest {
    @Test
    public void testGet() {
        ArgsTreeSet ats = new ArgsTreeSet(List.of(
                new ArgNull("foo"),
                new ArgNull("bar"),
                new ArgNull("baz"),
                new ArgGeneric<>("foo", new String[]{"value"})
        ), (Iterable<Arg>) null);

        Assert.assertEquals(ats.size(), 3);
        Assert.assertTrue(ats.get("foo").isPresent());
        Assert.assertTrue(ats.get("bar").isPresent());
        Assert.assertTrue(ats.get("baz").isPresent());
        Assert.assertFalse(ats.get("foo2").isPresent());

        Assert.assertNotEquals(ats.get("foo").get(), new ArgNull("foo"));
        Assert.assertEquals(ats.get("bar").get(), new ArgNull("bar"));
        Assert.assertEquals(ats.get("baz").get(), new ArgNull("baz"));
    }
}