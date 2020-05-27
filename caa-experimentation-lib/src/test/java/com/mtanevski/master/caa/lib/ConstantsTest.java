package com.mtanevski.master.caa.lib;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConstantsTest {

    @Test
    public void shouldHaveCorrectConstantValues() {
        assertEquals(0.0, Constants.EDGES_TRAVERSAL_WEIGHT, 0);
        assertEquals(0.0, Constants.EDGES_WEIGHT, 0);
        assertEquals(1.5, Constants.HAPPY_INCREMENT, 0);
        assertEquals(-0.5, Constants.SAD_INCREMENT, 0);
        assertEquals(-1.0, Constants.TRAVERSAL_INCREMENT, 0);
    }

}