package com.godpalace.asmwriter.settings;

public class GuiSettings {
    public int windowWidth;
    public int windowHeight;
    public boolean isMaximized;

    public GuiSettings() {
    }

    public GuiSettings(int windowWidth, int windowHeight, boolean isMaximized) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.isMaximized = isMaximized;
    }
}
