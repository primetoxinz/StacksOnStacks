package com.primetoxinz.stacksonstacks.core;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {
	
	private static Configuration config;
	
	private static final ConfigCategory CATEGORY_APPEARANCE = new ConfigCategory("Appearance");
    public static String defaultTextureName;
    public static boolean useIngotBlockTexture;
	
    public static void initConfig(File suggestedConfigurationFile) {
        config = new Configuration(suggestedConfigurationFile);
        loadConfig();
    }
    

    private static void loadConfig() {
        config.load();
  
        String[] validTextureNames = { "sos:default_ingot_texture" };
        config.addCustomCategoryComment(CATEGORY_APPEARANCE.getName(), "Change how the ingots look");
        defaultTextureName = config.getString("Ingot Texture", CATEGORY_APPEARANCE.getName(), "sos:default_ingot_texture", "The texture that ingots use to render(Textures belonging to other mods require that mod to work!)", validTextureNames);

        useIngotBlockTexture = config.getBoolean("useIngotBlockTexture", CATEGORY_APPEARANCE.getName(), true, "Use the texture of the block version of the ingot if one exists (ex: gold ingot uses gold block texture)");
        config.save();
    }
}
