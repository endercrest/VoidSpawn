package com.endercrest.voidspawn.utils;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Thomas Cordua-von Specht on 1/21/2017.
 *
 * Test file for {@link WorldName}.
 */
public class WorldNameTest {
    @Test
    public void configSafe() throws Exception {
        assertThat(WorldName.configSafe("Hello"), is("Hello"));
        assertThat(WorldName.configSafe("Hello World"), is("Hello_World"));
        assertThat(WorldName.configSafe("Hello_World"), is("Hello_World"));
        assertThat(WorldName.configSafe("Hello  World"), is("Hello__World"));
        assertThat(WorldName.configSafe(" Hello"), is("_Hello"));
        assertThat(WorldName.configSafe("Hello "), is("Hello_"));
    }

}