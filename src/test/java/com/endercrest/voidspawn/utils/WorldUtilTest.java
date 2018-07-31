package com.endercrest.voidspawn.utils;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Thomas Cordua-von Specht on 1/21/2017.
 *
 * Test file for {@link WorldUtil}.
 */
public class WorldUtilTest {
    @Test
    public void configSafe() throws Exception {
        assertThat(WorldUtil.configSafe("Hello"), is("Hello"));
        assertThat(WorldUtil.configSafe("Hello World"), is("Hello_World"));
        assertThat(WorldUtil.configSafe("Hello_World"), is("Hello_World"));
        assertThat(WorldUtil.configSafe("Hello  World"), is("Hello__World"));
        assertThat(WorldUtil.configSafe(" Hello"), is("_Hello"));
        assertThat(WorldUtil.configSafe("Hello "), is("Hello_"));
    }

}