package app.jdev.library.service;

import app.jdev.library.controller.Action;

import java.time.LocalDateTime;

public class LogService {

    public static void log(Action action, String message) {
        System.out.println(LocalDateTime.now() + " " + action + " --- " + message);
    }

}
