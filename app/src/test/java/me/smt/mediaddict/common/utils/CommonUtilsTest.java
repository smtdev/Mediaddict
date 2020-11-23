package me.smt.mediaddict.common.utils;

import org.junit.Assert;
import org.junit.Test;

import me.smt.mediaddict.common.CommonUtils;

public class CommonUtilsTest {

    @Test
    public void parseMinutesToHour() {
        final String expected = "1h 10min";

        final String actual = CommonUtils.parseMinutesToHour(70);

        Assert.assertEquals(actual, expected);
    }
}