package com.godpalace.asmwriter.test;

import javafx.application.Application;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMain {
    public static final Logger LOGGER = LoggerFactory.getLogger(TestMain.class);

    @Test
    public void test() {
        Application.launch(TestAppSetup.class);
    }
}
