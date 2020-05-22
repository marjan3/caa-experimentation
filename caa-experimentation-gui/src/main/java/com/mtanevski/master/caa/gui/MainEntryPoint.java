package com.mtanevski.master.caa.gui;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainEntryPoint {

    public static void main(String[] args) {
        Application.launch(JavaFxSpringApplication.class, args);
    }
}
