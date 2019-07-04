package com.github.mono83;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ReplacerColonTest {
    @Test
    public void testEmptiesAndNulls() {
        Assert.assertEquals(new ReplacerColon().apply(null, null), "");
        Assert.assertEquals(new ReplacerColon().apply("x", null), "x");
        Assert.assertEquals(new ReplacerColon().apply(null, Args.of()), "");
        Assert.assertEquals(new ReplacerColon().apply("y", Args.of()), "y");
    }

    @Test(dependsOnMethods = "testEmptiesAndNulls")
    public void testNoPlaceholders() {
        Assert.assertEquals(
                new ReplacerColon().apply("This is the string without placeholders", Args.of(ArgGeneric.of("foo", "bar"))),
                "This is the string without placeholders"
        );
    }

    @Test(dependsOnMethods = "testEmptiesAndNulls")
    public void testFullMatch() {
        Assert.assertEquals(
                new ReplacerColon().apply(":foo", Args.of(ArgGeneric.of("foo", "bar"))),
                "bar"
        );
    }

    @Test(dependsOnMethods = "testEmptiesAndNulls")
    public void testEasyMatch() {
        Assert.assertEquals(
                new ReplacerColon().apply("Placeholder :foo inside", Args.of(ArgGeneric.of("foo", "bar"))),
                "Placeholder bar inside"
        );
    }

    @Test(dependsOnMethods = "testEasyMatch")
    public void testStandardMatch() {
        Assert.assertEquals(
                new ReplacerColon().apply(
                        ":id is assigned to :name, with result :result. Here :unknown",
                        Args.of(
                                Names.ID.of(8),
                                Names.NAME.of("user"),
                                ArgGeneric.of("result", "success")
                        )
                ),
                "8 is assigned to user, with result success. Here <!unknown!>"
        );
    }

    @Test(dependsOnMethods = "testStandardMatch")
    public void testMultiplePlaceholders() {
        Assert.assertEquals(
                new ReplacerColon().apply(
                        "There is :name, :name and :name",
                        Args.of(Names.NAME.of("one", "two"))
                ),
                "There is one, two and two"
        );
    }
}