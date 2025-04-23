package ru.varino.common.utility;

import java.util.Scanner;

/**
 * Класс для работы со сканерами
 */
public class ScannerManager {
    private Scanner currentScanner;

    public Scanner getCurrentScanner() {
        return currentScanner;
    }

    public void setCurrentScanner(Scanner currentScanner) {
        this.currentScanner = currentScanner;
    }
}