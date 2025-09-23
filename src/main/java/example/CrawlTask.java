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
    private static final String DOMAIN = "https://sendel.ru";
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
            logger.info("The page has already been visited, skip: " + url);
            return null;
        }

        try {
            synchronized (visited) {
                visited.add(url);
            }
            logger.info("Visit: " + url);
            Thread.sleep(100 + (int) (Math.random() * 50));
            Document doc = Jsoup.connect(url).get();
            List<Element> links = doc.select("a[href]");

            synchronized (writer) {

                String tabIndentation = "\t".repeat(depth);
                writer.write(tabIndentation + url + "\n");
                logger.info("Write to a file: " + url);
            }

            List<CrawlTask> subTasks = new ArrayList<>();

            for (Element link : links) {
                String linkHref = link.absUrl("href");
                if (linkHref.contains(url) && !linkHref.contains("#") && !visited.contains(linkHref)) {
                    logger.info("Handling the child link: " + linkHref);
                    CrawlTask subTask = new CrawlTask(linkHref, visited, writer, depth + 1);
                    subTasks.add(subTask);
                    subTask.fork(); 
                }
            }

            for (CrawlTask subTask : subTasks) {
                subTask.join();
            }
        } catch (IOException | InterruptedException e) {
            logger.warning("Page processing error " + url + ": " + e.getMessage());
        }
        return null;
    }
}
