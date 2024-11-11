package com.godpalace.asmwriter;

import com.godpalace.asmwriter.gui.animations.StageOpacityTransition;
import com.godpalace.asmwriter.annotations.JavaFXApplication;
import com.godpalace.asmwriter.settings.GuiSettings;
import com.leewyatt.rxcontrols.controls.RXFillButton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
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

    // Objects
    protected final double titleButtonSize = 35;
    protected final double dragLength = 3;

    private double mouseOffsetX = 0, mouseOffsetY = 0;
    private boolean isDraggingWindow = false;

    private double mouseX = 0, mouseY = 0, oldWidth = 0, oldHeight = 0;
    private long lastClickTime = 0;
    private ResizeType curResizeType = ResizeType.NONE;

    /* !Methods! */

    // 程序关闭时的清理工作
    protected void onClosedClearUp() {
    }

    @Override
    public void init() throws Exception {
        super.init();
        LOGGER.info("Initializing AsmWriter...");

        // 初始化配置
        if (!Main.initProperties()) {
            LOGGER.error("Failed to initialize properties.");
            Platform.exit();
        }

        // 获取任务栏高度
    }

    @Override
    public void start(Stage STAGE) throws Exception {
        LOGGER.info("Starting AsmWriter...");

        // 全局变量
        AnchorPane pane = createTitleBarPane(new AnchorPane());
        Scene scene = new Scene(pane);
        stage = STAGE;

        // 窗口标题栏初始化
        pane.setBackground(new Background(new BackgroundFill(
                Color.rgb(60, 63, 65), null, null)));

        // 窗口拖动事件
        stage.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            isDraggingWindow = dragLength <= e.getY() && e.getY() < titleButtonSize &&
                    e.getX() < stage.getWidth() - titleButtonSize * 3;

            if (isDraggingWindow) {
                mouseOffsetX = e.getX();
                mouseOffsetY = e.getY();
                scene.setCursor(Cursor.MOVE);
            }
        });
        stage.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            if (isDraggingWindow) {
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    stage.setX(e.getScreenX() - mouseOffsetX);
                    stage.setY(e.getScreenY() - mouseOffsetY);
                }
            }
        });
        stage.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            if (isDraggingWindow) {
                scene.setCursor(Cursor.DEFAULT);
                isDraggingWindow = false;
            }
        });

        // 窗口大小调整事件
        stage.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            if (e.getX() < dragLength &&
                    e.getY() > dragLength && e.getY() < stage.getHeight() - dragLength) {
                // 左
                scene.setCursor(Cursor.W_RESIZE);
                curResizeType = ResizeType.LEFT;
            } else if (e.getX() > stage.getWidth() - dragLength &&
                    e.getY() > dragLength && e.getY() < stage.getHeight() - dragLength) {
                // 右
                scene.setCursor(Cursor.E_RESIZE);
                curResizeType = ResizeType.RIGHT;
            } else if (e.getX() > dragLength && e.getX() < stage.getWidth() - dragLength &&
                    e.getY() < dragLength) {
                // 上
                scene.setCursor(Cursor.N_RESIZE);
                curResizeType = ResizeType.TOP;
            } else if (e.getX() > dragLength && e.getX() < stage.getWidth() - dragLength &&
                    e.getY() > stage.getHeight() - dragLength) {
                // 下
                scene.setCursor(Cursor.S_RESIZE);
                curResizeType = ResizeType.BOTTOM;
            } else if (e.getX() < dragLength && e.getY() < dragLength) {
                // 左上
                scene.setCursor(Cursor.NW_RESIZE);
                curResizeType = ResizeType.TOP_LEFT;
            } else if (e.getX() > stage.getWidth() - dragLength && e.getY() < dragLength) {
                // 右上
                scene.setCursor(Cursor.NE_RESIZE);
                curResizeType = ResizeType.TOP_RIGHT;
            } else if (e.getX() < dragLength && e.getY() > stage.getHeight() - dragLength) {
                // 左下
                scene.setCursor(Cursor.SW_RESIZE);
                curResizeType = ResizeType.BOTTOM_LEFT;
            } else if (e.getX() > stage.getWidth() - dragLength &&
                    e.getY() > stage.getHeight() - dragLength) {
                // 右下
                scene.setCursor(Cursor.SE_RESIZE);
                curResizeType = ResizeType.BOTTOM_RIGHT;
            } else {
                scene.setCursor(Cursor.DEFAULT);
                curResizeType = ResizeType.NONE;
            }
        });
        stage.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (curResizeType == ResizeType.NONE) return;

            mouseX = e.getScreenX();
            mouseY = e.getScreenY();

            oldWidth = stage.getWidth();
            oldHeight = stage.getHeight();
        });
        stage.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            if (curResizeType == ResizeType.NONE) return;

            double deltaX = e.getScreenX() - mouseX;  // 鼠标移动距离X
            double deltaY = e.getScreenY() - mouseY;  // 鼠标移动距离Y

            if (System.currentTimeMillis() - lastClickTime >= 10) {
                if (curResizeType == ResizeType.TOP) {
                    if (oldHeight - deltaY >= stage.getMinHeight()) {
                        stage.setY(e.getScreenY());
                        stage.setHeight(oldHeight - deltaY);
                    }
                } else if (curResizeType == ResizeType.BOTTOM) {
                    stage.setHeight(oldHeight + deltaY);
                } else if (curResizeType == ResizeType.LEFT) {
                    if (oldWidth - deltaX >= stage.getMinWidth()) {
                        stage.setX(e.getScreenX());
                        stage.setWidth(oldWidth - deltaX);
                    }
                } else if (curResizeType == ResizeType.RIGHT) {
                    stage.setWidth(oldWidth + deltaX);
                } else if (curResizeType == ResizeType.TOP_LEFT) {
                    if (oldWidth - deltaX >= stage.getMinWidth()) {
                        stage.setX(e.getScreenX());
                        stage.setWidth(oldWidth - deltaX);
                    }

                    if (oldHeight - deltaY >= stage.getMinHeight()) {
                        stage.setY(e.getScreenY());
                        stage.setHeight(oldHeight - deltaY);
                    }
                } else if (curResizeType == ResizeType.TOP_RIGHT) {
                    stage.setY(e.getScreenY());
                    stage.setHeight(oldHeight - deltaY);
                    stage.setWidth(oldWidth + deltaX);
                } else if (curResizeType == ResizeType.BOTTOM_LEFT) {
                    stage.setX(e.getScreenX());
                    stage.setWidth(oldWidth - deltaX);
                    stage.setHeight(oldHeight + deltaY);
                } else if (curResizeType == ResizeType.BOTTOM_RIGHT) {
                    stage.setWidth(oldWidth + deltaX);
                    stage.setHeight(oldHeight + deltaY);
                }

                if (stage.getWidth() < stage.getMinWidth()) stage.setWidth(stage.getMinWidth());
                if (stage.getHeight() < stage.getMinHeight()) stage.setHeight(stage.getMinHeight());

                lastClickTime = System.currentTimeMillis();
            }
        });
        stage.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> curResizeType = ResizeType.NONE);

        // 初始化GUI设置
        stage.setTitle("AsmWriter");
        stage.getIcons().add(icon);
        stage.initStyle(StageStyle.TRANSPARENT);

        stage.setWidth(settings.windowWidth);
        stage.setHeight(settings.windowHeight);
        stage.setMaximized(settings.isMaximized);

        stage.setMinWidth(600);
        stage.setMinHeight(400);

        // 初始化组件
        LOGGER.info("Initializing components...");

        // 场景设置
        stage.setScene(scene);

        // 注册快捷键

        // 显示GUI
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        LOGGER.info("Stopping AsmWriter...");
    }

    protected RXFillButton createTitleBarButton(String text, String tooltipText) {
        RXFillButton button = new RXFillButton(text);

        button.setFillType(RXFillButton.FillType.TOP_TO_BOTTOM);
        button.setHoverTextFill(Color.WHITE);
        button.setTextFill(Color.GRAY);
        button.setBorder(new Border(new BorderStroke(
                Color.rgb(60, 63, 65),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT)));
        button.setBackground(new Background(new BackgroundFill(
                Color.rgb(60, 63, 65), null, null)));
        button.setStyle("-rx-fill: GRAY;");

        button.setMaxSize(titleButtonSize, titleButtonSize);
        button.setAnimationTime(Duration.millis(85));

        // 设置提示信息
        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setShowDelay(Duration.millis(1000));
        button.setTooltip(tooltip);

        return button;
    }

    protected RXFillButton createCloseButton() {
        RXFillButton button = new RXFillButton("x");

        button.setFillType(RXFillButton.FillType.TOP_TO_BOTTOM);
        button.setHoverTextFill(Color.WHITE);
        button.setTextFill(Color.RED);
        button.setBorder(new Border(new BorderStroke(
                Color.rgb(60, 63, 65),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT)));
        button.setBackground(new Background(new BackgroundFill(
                Color.rgb(60, 63, 65), null, null)));
        button.setStyle("-rx-fill: RED;");

        button.setMaxSize(titleButtonSize, titleButtonSize);
        button.setAnimationTime(Duration.millis(85));

        // 设置提示信息
        Tooltip tooltip = new Tooltip("关闭窗口");
        tooltip.setShowDelay(Duration.millis(1000));
        button.setTooltip(tooltip);

        return button;
    }

    protected AnchorPane createTitleBarPane(AnchorPane pane) {
        // 窗口标题栏初始化
        RXFillButton minimizeButton = createTitleBarButton("-", "最小化");
        RXFillButton maximizeButton = createTitleBarButton("□", "最大化");
        RXFillButton closeButton = createCloseButton();

        AnchorPane.setRightAnchor(minimizeButton, titleButtonSize * 2);
        AnchorPane.setRightAnchor(maximizeButton, titleButtonSize);
        AnchorPane.setRightAnchor(closeButton, 0.0);

        minimizeButton.setOnMouseClicked(e -> stage.setIconified(true));
        closeButton.setOnMouseClicked(e -> {
            // 窗口关闭时的渐淡动画
            StageOpacityTransition transition = new StageOpacityTransition(
                    stage, stage.getOpacity(), 0.0, Duration.millis(125));

            // 渐淡动画结束后关闭窗口
            transition.setOnFinished(event -> {
                onClosedClearUp();
                stage.close();
            });

            // 开始播放动画
            transition.play();
        });
        maximizeButton.setOnMouseClicked(e -> {
            stage.setMaximized(!stage.isMaximized());
            if (stage.isMaximized()) maximizeButton.setText("=");
            else maximizeButton.setText("□");
        });

        pane.getChildren().addAll(minimizeButton, maximizeButton, closeButton);
        return pane;
    }

    private enum ResizeType {
        NONE, TOP, BOTTOM, LEFT, RIGHT, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
}
