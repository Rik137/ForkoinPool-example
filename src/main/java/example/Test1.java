package example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;

public class Test1 {
    public static final String url = "https://sendel.ru/";  // Начальная страница
    public static final String file = "ForkJoinPoolExample/File/site_map.txt";  // Путь к файлу
    private static final Logger logger = Logger.getLogger(Test1.class.getName());

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            CrawlTask crawlTask = new CrawlTask(url, new HashSet<>(), writer, 1);

            forkJoinPool.invoke(crawlTask);

        } catch (IOException e) {
            logger.severe("Ошибка при работе с файлом: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Ошибка при выполнении задачи: " + e.getMessage());
        }
    }
}


































