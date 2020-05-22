package com.mtanevski.master.caa.gui.web;

import com.mtanevski.master.caa.gui.DefaultValues;
import javafx.scene.paint.Color;

public class WebUtils {

    public static int toNodeFontSize(Boolean n) {
        return Boolean.parseBoolean(n.toString()) ? DefaultValues.NODES_FONT_SIZE : 0;
    }

    public static int toEdgeFontSize(Boolean n) {
        return Boolean.parseBoolean(n.toString()) ? DefaultValues.EDGES_FONT_SIZE : 0;
    }

    public static String toWeb(Color color) {
        return color.toString().replace("0x", "#");
    }

}
