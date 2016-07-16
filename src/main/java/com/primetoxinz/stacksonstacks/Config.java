package com.primetoxinz.stacksonstacks;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Created by tyler on 7/16/16.
 */
public class Config extends Configuration {
    public Config(File suggestedConfigurationFile) {
        super(suggestedConfigurationFile);
    }
    public static int textureVersion = 0;
    public static boolean useIngotBlockTexture;
    public void pre() {
        load();
        String CAT = "appearance";
        String DESC = "Change look of ingot texture";
        String NAME = "texture version";
        textureVersion = getInt(NAME,CAT,2,1,2,DESC);

        DESC = "Use the texture of the compressed block version of the ingot ex: gold ingot uses gold block texture";
        NAME = "useIngotBlockTexture";
        useIngotBlockTexture = getBoolean(NAME,CAT,true,DESC);
        save();
    }
}
