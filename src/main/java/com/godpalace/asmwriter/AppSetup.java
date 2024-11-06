package com.godpalace.asmwriter;

import com.godpalace.asmwriter.settings.GuiSettings;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;

public class AppSetup extends Application {
    // Class Objects
    protected static final Class<AppSetup> clazz = AppSetup.class;
    protected static final GuiSettings settings = new GuiSettings();
    protected static final Logger LOGGER = LoggerFactory.getLogger("AppSetup");

    // Static Resources
    protected final Image icon = new Image(
            Objects.requireNonNull(clazz.getResourceAsStream("/png/AsmWriterIcon.png")));

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
        // 初始化GUI设置
        LOGGER.info("Starting AsmWriter...");
        stage = STAGE;
        stage.setTitle("AsmWriter");
        stage.getIcons().add(icon);
        stage.setWidth(settings.windowWidth);
        stage.setHeight(settings.windowHeight);
        stage.setMaximized(settings.isMaximized);

        // 初始化组件
        LOGGER.info("Initializing components...");

        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);
        stage.setScene(scene);

        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        LOGGER.info("Stopping AsmWriter...");
    }
}
