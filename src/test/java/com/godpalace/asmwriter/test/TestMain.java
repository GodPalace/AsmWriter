package com.godpalace.asmwriter.test;

import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMain {
    public static final Logger LOGGER = LoggerFactory.getLogger(TestMain.class);

    public static void main(String[] args) {
        Application.launch(TestAppSetup.class, args);
    }
}
