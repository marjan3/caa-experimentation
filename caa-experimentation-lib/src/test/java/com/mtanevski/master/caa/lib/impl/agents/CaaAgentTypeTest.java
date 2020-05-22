package com.mtanevski.master.caa.lib.impl.agents;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

public class CaaAgentTypeTest {

    @Test
    public void shouldListEnumValues() {
        List<CaaAgentType> caaAgentTypes = CaaAgentType.listValues();

        assertThat(caaAgentTypes, hasItems(CaaAgentType.values()));
    }

    @Test
    public void shouldListEnumValuesAsStringValues() {
        List<String> caaAgentTypes = CaaAgentType.stringValues();

        assertThat(caaAgentTypes, hasItems("ORIGINAL", "UNTRAVELED_EDGE"));
    }
}