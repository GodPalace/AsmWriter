package com.godpalace.asmwriter;

import com.godpalace.asmwriter.other.error.ErrorListener;
import com.godpalace.asmwriter.other.error.ErrorManager;
import com.godpalace.asmwriter.settings.GuiSettings;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Objects;
import java.util.Properties;

import static com.godpalace.asmwriter.other.ResourcePaths.CONFIG_PATH;

public class Main {
    // Class Objects
    private static final Class<Main> clazz = Main.class;
    private static final ErrorManager error = new ErrorManager();
    private static final GuiSettings settings = new GuiSettings();

    // Static Resources
    private static final ImageIcon icon = new ImageIcon(Objects.requireNonNull(
            clazz.getResource("/png/AsmWriterIcon.png")));

    private static final Properties properties = new Properties();

    // Gui Objects
    private static JFrame f;
    private static Container c;

    // Static Initializers
    static {
        error.addErrorListener(new DoError());
    }

    static {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    properties.setProperty("windowHeight", settings.windowHeight + "");
                    properties.setProperty("windowWidth", settings.windowWidth + "");
                    properties.setProperty("isMaximized", settings.isMaximized + "");

                    properties.store(new java.io.FileOutputStream(CONFIG_PATH), null);
                } catch (Exception e) {
                    error.setLastError(e);
                }
            }));
        } catch (Exception e) {
            error.setLastError(e);
        }
    }

    // Static Methods
    private static boolean initProperties() {
        try {
            File configFile = new File(CONFIG_PATH);
            if (!configFile.exists()) configFile.createNewFile();
        } catch (Exception e) {
            error.setLastError(e);
            return false;
        }

        try {
            properties.load(new FileInputStream(CONFIG_PATH));

            settings.windowHeight = Integer.parseInt(properties.getProperty(
                    "windowHeight", "500"));
            settings.windowWidth = Integer.parseInt(properties.getProperty(
                    "windowWidth", "500"));
            settings.isMaximized = Boolean.parseBoolean(properties.getProperty(
                    "isMaximized", "false"));
        } catch (Exception e) {
            error.setLastError(e);
            return false;
        }

        return true;
    }

    private static Container initGui() {
        f = new JFrame("AsmWriter");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setIconImage(icon.getImage());
        f.setLocationRelativeTo(null);

        if (settings.isMaximized) {
            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            f.setSize(settings.windowWidth, settings.windowHeight);
        }

        return f.getContentPane();
    }

    public static void main(String[] args) {
        if (!initProperties()) {
            JOptionPane.showMessageDialog(null, "错误: 无法初始化配置文件",
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        c = initGui();

        f.setVisible(true);
    }

    static class DoError implements ErrorListener {
        @Override
        public void error(ErrorManager manager) {
            manager.printLastError();
        }
    }
}
