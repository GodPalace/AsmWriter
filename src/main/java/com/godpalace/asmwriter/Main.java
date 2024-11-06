package com.godpalace.asmwriter;

import javafx.application.Application;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static com.godpalace.asmwriter.AppSetup.*;
import static com.godpalace.asmwriter.config.ResourcePaths.CONFIG_PATH;

public class Main {
    // Class variables
    protected static final Toolkit toolkit = Toolkit.getDefaultToolkit();

    static {
        // 设置关闭钩子
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    properties.setProperty("windowHeight", (int) stage.getHeight() + "");
                    properties.setProperty("windowWidth", (int) stage.getWidth() + "");
                    properties.setProperty("isMaximized",
                            (stage.isMaximized() ? "true" : "false"));

                    properties.store(new FileOutputStream(CONFIG_PATH), null);
                } catch (Exception e) {
                    LOGGER.error("Error while saving properties", e);
                }
            }));
        } catch (Exception e) {
            LOGGER.error("Error while adding shutdown hook", e);
        }
    }

    protected static boolean initProperties() {
        try {
            File configFile = new File(CONFIG_PATH);
            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
            }
        } catch (Exception e) {
            LOGGER.error("Error while creating config file", e);
            return false;
        }

        try {
            properties.load(new FileInputStream(CONFIG_PATH));

            settings.windowHeight = Integer.parseInt(properties.getProperty(
                    "windowHeight", toolkit.getScreenSize().height - 80 + ""));
            settings.windowWidth = Integer.parseInt(properties.getProperty(
                    "windowWidth", toolkit.getScreenSize().width - 50 + ""));
            settings.isMaximized = Boolean.parseBoolean(properties.getProperty(
                    "isMaximized", "false"));
        } catch (Exception e) {
            LOGGER.error("Error while loading properties", e);
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        // 启动GUI
        Application.launch(AppSetup.class, args);
    }
}
