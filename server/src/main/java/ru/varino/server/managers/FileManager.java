package ru.varino.server.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.varino.common.exceptions.PermissionDeniedException;
import ru.varino.common.io.Console;


import java.io.*;

/**
 * Класс для работы с файлом
 */
public class FileManager {
    private final Console console;
    private String fileName;
    private static final Logger logger = LoggerFactory.getLogger(FileManager.class.getSimpleName());

    public FileManager(Console console, String fileName) {
        this.console = console;
        this.fileName = fileName;
    }

    /**
     * Записать коллекцию в файл
     *
     * @param text     json
     * @throws PermissionDeniedException выбрасывается при отсутствии прав
     */
    public void write(String text) throws PermissionDeniedException {
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName))) {
            var filePath = new File(fileName);
            if (!filePath.canWrite()) throw new PermissionDeniedException("Чтение");

            char[] chars = text.toCharArray();
            outputStreamWriter.write(chars, 0, chars.length);
        } catch (IOException e) {
            console.println("Ошибка при записи файла");
        }
    }

    /**
     * Прочитать коллекцию из файла
     *
     * @return String - прочитанная коллекция из файла
     */
    public String read() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            var filePath = new File(fileName);
            if (!filePath.exists()) throw new FileNotFoundException();
            if (!filePath.canRead()) throw new PermissionDeniedException("Чтение");
            if (!filePath.canWrite()) console.println("Нет прав на запись, вы не сможете использовать команду save");
            StringBuilder out = new StringBuilder();
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                out.append(s);
            }
            return out.toString();
        } catch (FileNotFoundException e) {
            logger.error("Файл не найден. Коллекция пуста");
            return "";
        } catch (PermissionDeniedException e) {
            logger.error(e.getMessage() + ". Коллекция пуста");
            return "";
        } catch (IOException e) {
            logger.error("Json-файл не найден. Коллекция пуста");
            return "";
        }
    }
}
