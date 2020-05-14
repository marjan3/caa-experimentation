package com.mtanevski.master.caa.lib.impl.agents;

import com.mtanevski.master.caa.lib.CaaAgent;
import org.junit.Test;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class CaaAgentFactoryTest {

    @Test
    public void shouldGetCaaAgentFromType() {
        CaaAgent originalCaaAgent = CaaAgentFactory.getAgent(CaaAgentType.ORIGINAL);
        CaaAgent untraveledEdgeCaaAgent = CaaAgentFactory.getAgent(CaaAgentType.UNTRAVELED_EDGE);

        assertThat(originalCaaAgent, instanceOf(OriginalCaaAgent.class));
        assertThat(untraveledEdgeCaaAgent, instanceOf(UntraveledEdgeCaaAgent.class));
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldNotGetCaaAgentFromNullType() {
        CaaAgentFactory.getAgent(null);
    }
}