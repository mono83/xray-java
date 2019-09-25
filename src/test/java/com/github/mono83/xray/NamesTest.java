package com.github.mono83.xray;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

public class NamesTest {
    @DataProvider
    public Object[][] dataProvider() {
        return new Object[][]{
                {Names.RAY_ID, String.class, "rayId"},
                {Names.NAME, String.class, "name"},
                {Names.TYPE, String.class, "type"},
                {Names.STATUS, String.class, "status"},
                {Names.STATE, String.class, "state"},
                {Names.SQL, String.class, "sql"},

                {Names.ERROR, Throwable.class, "error"},
                {Names.EXCEPTION, Throwable.class, "error"},

                {Names.ELAPSED, Duration.class, "elapsed"},
                {Names.TTL, Duration.class, "ttl"},

                {Names.ID, long.class, "id"},
                {Names.GATE_ID, long.class, "gateId"},
                {Names.REALM_ID, long.class, "gateId"},
                {Names.COUNT, int.class, "count"},
                {Names.SIZE, int.class, "count"},
        };
    }

    @Test(dataProvider = "dataProvider")
    public void testValues(final Names x, final Class<?> preferred, final String name) {
        Assert.assertEquals(x.getPreferredClass(), preferred);
        Assert.assertEquals(x.getName(), name);
    }
}