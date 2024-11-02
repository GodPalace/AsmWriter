package com.godpalace.asmwriter;

import com.godpalace.asmwriter.error.ErrorListener;
import com.godpalace.asmwriter.error.ErrorManager;
import com.godpalace.asmwriter.settings.GuiSettings;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Objects;
import java.util.Properties;

import static com.godpalace.asmwriter.config.ResourcePaths.CONFIG_PATH;

public class Main {
    // Class Objects
    private static final Class<Main> clazz = Main.class;
    private static final ErrorManager error = new ErrorManager();
    private static final GuiSettings settings = new GuiSettings();
    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();

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
                    properties.setProperty("windowHeight", f.getHeight() + "");
                    properties.setProperty("windowWidth", f.getWidth() + "");
                    properties.setProperty("isMaximized", (f.getExtendedState() == JFrame.MAXIMIZED_BOTH ? "true" : "false"));

                    properties.store(new FileOutputStream(CONFIG_PATH), null);
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
            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
            }
        } catch (Exception e) {
            error.setLastError(e);
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
            error.setLastError(e);
            return false;
        }

        return true;
    }

    private static Container initGui() {
        f = new JFrame("AsmWriter");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setIconImage(icon.getImage());

        // 设置窗口大小
        f.setSize(settings.windowWidth, settings.windowHeight);
        if (settings.isMaximized) {
            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        f.setLocationRelativeTo(null);

        return f.getContentPane();
    }

    private static JMenuBar initMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("文件");
        JMenuItem openItem = new JMenuItem("打开");
        JMenuItem saveItem = new JMenuItem("保存");
        JMenuItem saveAsItem = new JMenuItem("另存为");
        JMenuItem exitItem = new JMenuItem("退出");

        openItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(".asm");
                }

                @Override
                public String getDescription() {
                    return "汇编文件 (*.asm)";
                }
            });

            int result = fileChooser.showOpenDialog(f);
            if (result == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();

                try {
                } catch (Exception e1) {
                    error.setLastError(e1);
                }
            }
        });

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        return menuBar;
    }

    public static void main(String[] args) {
        if (!initProperties()) {
            JOptionPane.showMessageDialog(null, "错误: 无法初始化配置文件",
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        c = initGui();
        f.setJMenuBar(initMenu());

        f.setVisible(true);
    }

    static class DoError implements ErrorListener {
        @Override
        public void error(ErrorManager manager) {
            manager.printLastError();
        }
    }
}
