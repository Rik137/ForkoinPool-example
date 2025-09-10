package example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Logger;

public class CrawlTask extends RecursiveTask<Void> {
    private static final Logger logger = Logger.getLogger(CrawlTask.class.getName());
    private static final String DOMAIN = "https://sendel.ru";  // Базовый домен
    private final String url;
    private final Set<String> visited;
    private final BufferedWriter writer;
    private final int depth;

    public CrawlTask(String url, Set<String> visited, BufferedWriter writer, int depth) {
        this.url = url;
        this.visited = visited;
        this.writer = writer;
        this.depth = depth;
    }

    @Override
    protected Void compute() {
        if (visited.contains(url)) {
            logger.info("Страница уже была посещена, пропускаем: " + url);
            return null;
        }

        try {
            synchronized (visited) {
                visited.add(url);
            }
            logger.info("Посещаем: " + url);
            Thread.sleep(100 + (int) (Math.random() * 50));  // Задержка 100-150 мс
            Document doc = Jsoup.connect(url).get();
            List<Element> links = doc.select("a[href]");

            synchronized (writer) {

                String tabIndentation = "\t".repeat(depth);
                writer.write(tabIndentation + url + "\n");
                logger.info("Записываем в файл: " + url);
            }

            List<CrawlTask> subTasks = new ArrayList<>();

            for (Element link : links) {
                String linkHref = link.absUrl("href");
                if (linkHref.contains(url) && !linkHref.contains("#") && !visited.contains(linkHref)) {
                    logger.info("Обрабатываем дочернюю ссылку: " + linkHref);
                    CrawlTask subTask = new CrawlTask(linkHref, visited, writer, depth + 1);
                    subTasks.add(subTask); // Добавляем дочернюю задачу в список
                    subTask.fork(); // Запуск дочерней задачи
                }
            }

            for (CrawlTask subTask : subTasks) {
                subTask.join();
            }
        } catch (IOException | InterruptedException e) {
            logger.warning("Ошибка при обработке страницы " + url + ": " + e.getMessage());
        }
        return null;
    }
}