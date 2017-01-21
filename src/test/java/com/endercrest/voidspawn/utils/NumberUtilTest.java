package com.endercrest.voidspawn.utils;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Thomas Cordua-von Specht on 1/21/2017.
 *
 * Test file for {@link NumberUtil}.
 */
public class NumberUtilTest {

    @Test
    public void isInteger() throws Exception {
        assertThat(NumberUtil.isInteger("1"), is(true));
        assertThat(NumberUtil.isInteger("33"), is(true));
        assertThat(NumberUtil.isInteger("-1"), is(true));
        assertThat(NumberUtil.isInteger("0.0"), is(false));
        assertThat(NumberUtil.isInteger("hello"), is(false));
        assertThat(NumberUtil.isInteger("hello 33"), is(false));
    }

}