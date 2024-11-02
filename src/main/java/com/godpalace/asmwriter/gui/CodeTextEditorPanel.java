package com.godpalace.asmwriter.gui;

import javax.swing.*;
import java.awt.*;

public class CodeTextEditorPanel extends JComponent {


    public CodeTextEditorPanel(String context) {
    }

    public CodeTextEditorPanel() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    static class ColorText {
        public String text;
        public Color color;

        public ColorText(String text, Color color) {
            this.text = text;
            this.color = color;
        }

        public ColorText(String text) {
            this.text = text;
            this.color = Color.BLACK;
        }

        public ColorText() {
        }
    }
}
