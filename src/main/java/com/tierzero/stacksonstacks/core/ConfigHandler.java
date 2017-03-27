package com.tierzero.stacksonstacks.core;

import java.io.File;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

	private static Configuration config;
	
	private static final String CATEGORY_NAME_APPEARANCE = "Appearance";
    public static String defaultTextureName;
    public static boolean useIngotBlockTexture;

	private static final String CATEGORY_NAME_DEBUG = "Debug";
	public static boolean printOnRegistration;
	
    public static void initConfig(File suggestedConfigurationFile) {
        config = new Configuration(suggestedConfigurationFile);
        loadConfig();
    }
    
    private static void loadConfig() {
        config.load();
  
        String[] validTextureNames = { "sos:default_ingot_texture" };
        config.addCustomCategoryComment(CATEGORY_NAME_APPEARANCE, "Change how the ingots look");
        defaultTextureName = config.getString("Ingot Texture", CATEGORY_NAME_APPEARANCE, "sos:default_ingot_texture", "The texture that ingots use to render(Textures belonging to other mods require that mod to work!)", validTextureNames);

        useIngotBlockTexture = config.getBoolean("useIngotBlockTexture", CATEGORY_NAME_APPEARANCE, true, "Use the texture of the block version of the ingot if one exists (ex: gold ingot uses gold block texture)");
        
        config.addCustomCategoryComment(CATEGORY_NAME_DEBUG, "Enables some debug features of the mod, should not be used!");
        printOnRegistration = config.getBoolean("printOnRegistration", CATEGORY_NAME_DEBUG, false, "Causes a text containing the registered item's type and name to print when it is registered");
        config.save();
    }
}
