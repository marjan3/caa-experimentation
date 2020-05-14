package com.mtanevski.master.caa.lib.impl.edges;

import com.mtanevski.master.caa.lib.CaaEdge;

public class SimpleCaaEdge extends CaaEdge {

    public SimpleCaaEdge(String from, String to) {
        super(from, to, 0, 0);
    }

    public static CaaEdge of(String from, String to) {
        return new SimpleCaaEdge(from, to);
    }

}
