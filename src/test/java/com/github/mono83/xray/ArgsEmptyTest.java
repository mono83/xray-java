package com.github.mono83.xray;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ArgsEmptyTest {
    @Test
    public void testSize() {
        Assert.assertEquals(new ArgsEmpty().size(), 0);
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(new ArgsEmpty().isEmpty());
    }

    @Test
    public void testGet() {
        Assert.assertFalse(new ArgsEmpty().get("foo").isPresent());
        Assert.assertFalse(new ArgsEmpty().get("bar").isPresent());
    }

    @Test
    public void testIterator() {
        Assert.assertFalse(new ArgsEmpty().iterator().hasNext());
    }
}