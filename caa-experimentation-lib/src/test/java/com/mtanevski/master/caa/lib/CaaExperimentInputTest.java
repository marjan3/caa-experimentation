package com.mtanevski.master.caa.lib;

import com.mtanevski.master.caa.lib.impl.agents.CaaAgentType;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

public class CaaExperimentInputTest {

    @Test
    public void shouldBuildExperimentFromIncrementDecrementAndAgentType() {
        // when
        CaaExperimentInput experiment = CaaExperimentInput.create(CaaAgentType.UNTRAVELED_EDGE, 1.0, -1.0);

        // then
        assertNotNull(experiment);
        assertThat(experiment.getAgentType(), equalTo(CaaAgentType.UNTRAVELED_EDGE));
        assertThat(experiment.getDecrement(), equalTo(-1.0));
        assertThat(experiment.getIncrement(), equalTo(1.0));
    }
}
