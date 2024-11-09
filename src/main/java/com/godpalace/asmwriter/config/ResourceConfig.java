package com.godpalace.asmwriter.config;

import java.io.File;

public class ResourceConfig {
    private static final char sep = File.separatorChar;

    public static final String CONFIG_PATH =
            System.getProperty("user.home") + sep + ".AsmWriter" + sep + "config.properties";
}
