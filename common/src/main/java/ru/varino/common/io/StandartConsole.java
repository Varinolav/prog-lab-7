package ru.varino.common.io;

import ru.varino.common.communication.ResponseEntity;


public class StandartConsole implements Console {


    @Override
    public void println(String text) {
        System.out.println(text);
    }

    @Override
    public void printerr(String text) {
        System.out.println("$ " + text);
    }

    @Override
    public void printf(String text) {
        System.out.printf(text);
    }

    public void printResponse(ResponseEntity response) {
        System.out.println(response.getBody());
    }
}
