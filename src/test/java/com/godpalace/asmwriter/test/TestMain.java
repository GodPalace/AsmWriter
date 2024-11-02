package com.godpalace.asmwriter.test;

import com.godpalace.asmwriter.gui.Terminal;

import javax.swing.*;

public class TestMain {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Test");
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Terminal terminal = new Terminal();
        frame.setContentPane(terminal);

        Process process = Runtime.getRuntime().exec("ping 127.0.0.1");
        terminal.addProcess("DoPing", process);

        frame.setVisible(true);
    }
}
