package com.godpalace.asmwriter.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Terminal extends JPanel {
    private JTabbedPane pane;

    private final Executor executor;

    private void initGui() {
        pane = new JTabbedPane();
        pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        pane.setTabPlacement(JTabbedPane.TOP);
        pane.setBackground(new Color(50, 50, 50));

        this.add(pane);
    }

    public Terminal() {
        super();
        initGui();

        executor = Executors.newCachedThreadPool();
    }

    public void addProcess(String title, Process process) {
        try {
            TerminalTab tab = new TerminalTab(process);

            pane.addTab(title, tab);
            pane.setSelectedIndex(pane.getTabCount() - 1);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    public void removeProcess(Process process) {
        for (int i = 0; i < pane.getTabCount(); i++) {
            TerminalTab tab = (TerminalTab) pane.getComponentAt(i);

            if (tab.process == process) {
                pane.remove(i);
                break;
            }
        }
    }

    public Process getSelectedProcess() {
        TerminalTab tab = (TerminalTab) pane.getSelectedComponent();
        return tab.process;
    }

    private class TerminalTab extends JPanel {
        private StyledDocument document;
        private final Font font;

        private final StringBuilder input;
        private final Process process;
        private final Executor pool;

        private final Reader in;
        private final Writer out;
        private final Reader err;
        private JTextPane textPane;

        private void append(String text, Color color) {
            try {
                Style style = textPane.addStyle("color", null);
                StyleConstants.setForeground(style, color);

                document.insertString(document.getLength(), text, style);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

        private void initGui() {
            textPane = new JTextPane();

            document = textPane.getStyledDocument();
            textPane.setFont(font);

            textPane.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        pool.execute(() -> {
                            try {
                                out.write(input.toString() + "\n");
                            } catch (IOException ex) {
                                append("输入错误: " + ex.getMessage() + "\n", Color.RED);
                            }
                        });

                        input.replace(0, input.length(), "");
                        input.setLength(0);
                    } else {
                        input.append(e.getKeyChar());
                    }
                }
            });

            this.add(textPane);
        }

        public TerminalTab(Process process) throws UnsupportedEncodingException {
            super();

            font = new Font("微软雅黑", Font.PLAIN, 14);

            pool = Executors.newFixedThreadPool(1);
            initGui();

            this.input = new StringBuilder();
            this.process = process;

            this.in = new InputStreamReader(process.getInputStream(), "GBK");
            this.out = new OutputStreamWriter(process.getOutputStream(), "GBK");
            this.err = new InputStreamReader(process.getErrorStream(), "GBK");

            executor.execute(() -> {
                try {
                    while (true) {
                        append(readLine(in) + "\n", Color.BLACK);
                    }
                } catch (Exception ex) {
                    append("错误: " + ex.getMessage() + "\n", Color.RED);
                }
            });

            executor.execute(() -> {
                try {
                    while (true) {
                        append(readLine(err) + "\n", Color.RED);
                    }
                } catch (Exception ex) {
                    append("错误: " + ex.getMessage() + "\n", Color.RED);
                }
            });
        }

        private String readLine(Reader reader) throws IOException {
            StringBuilder sb = new StringBuilder();

            char[] c = new char[1];
            while (reader.read(c) != -1) {
                if (c[0] == '\n') return sb.toString();
                sb.append(c[0]);
            }

            return sb.toString();
        }
    }
}
