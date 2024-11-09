package com.godpalace.asmwriter;

import com.godpalace.asmwriter.Annotations.JavaFXApplication;
import com.godpalace.asmwriter.settings.GuiSettings;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

@JavaFXApplication
public class AppSetup extends Application {
    // Class Objects
    protected static final Class<AppSetup> clazz = AppSetup.class;
    protected static final GuiSettings settings = new GuiSettings();
    protected static final Logger LOGGER = LoggerFactory.getLogger(clazz);

    // Static Resources
    protected final Image icon = new Image("/png/AsmWriterIcon.png");

    protected static final Properties properties = new Properties();

    // Gui Objects
    protected static Stage stage;

    // Methods

    @Override
    public void init() throws Exception {
        super.init();
        LOGGER.info("Initializing AsmWriter...");

        // 初始化配置
        if (!Main.initProperties()) {
            LOGGER.error("Failed to initialize properties.");
            Platform.exit();
        }
    }

    @Override
    public void start(Stage STAGE) throws Exception {
        LOGGER.info("Starting AsmWriter...");

        // 全局变量
        stage = STAGE;
        AnchorPane pane = new AnchorPane();
        Scene scene;

        // 初始化GUI设置
        stage.setTitle("AsmWriter");
        stage.getIcons().add(icon);

        stage.setWidth(settings.windowWidth);
        stage.setHeight(settings.windowHeight);
        stage.setMaximized(settings.isMaximized);

        stage.setMinWidth(600);
        stage.setMinHeight(400);

        // 初始化组件
        LOGGER.info("Initializing components...");

        // 场景设置
        scene = new Scene(pane);
        stage.setScene(scene);

        // 注册快捷键
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            switch (e.getCode()) {
                // 全屏切换
                case F11 -> stage.setFullScreen(!stage.isFullScreen());
            }

            e.consume();
        });

        // 显示GUI
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        LOGGER.info("Stopping AsmWriter...");
    }
}
