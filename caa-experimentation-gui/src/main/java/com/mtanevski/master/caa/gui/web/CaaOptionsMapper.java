package com.mtanevski.master.caa.gui.web;

import com.google.gson.JsonObject;
import com.mtanevski.master.caa.gui.models.UserPreferences;

import static com.mtanevski.master.caa.gui.web.WebUtils.toWeb;


public class CaaOptionsMapper {

    public static String toJson(UserPreferences preferences) {
        JsonObject root = new JsonObject();
        root.addProperty("happyNodesColor", toWeb(preferences.getHappyVertexColor()));
        root.addProperty("sadNodesColor", toWeb(preferences.getSadVertexColor()));
        root.addProperty("startNodeColor", toWeb(preferences.getStartingVertexColor()));
        return root.toString();
    }
}
