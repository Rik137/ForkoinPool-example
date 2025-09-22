# ForkJoin Web Crawler Example

This project is a simple educational implementation of a **multithreaded web crawler** in Java using the **Fork/Join framework** and [Jsoup](https://jsoup.org/).  
It recursively traverses links within a given domain and writes the discovered URLs into a text file in a tree-like structure.

---

## Features

- Uses **ForkJoinPool** to perform recursive crawling in parallel.
- Extracts links from HTML pages with **Jsoup**.
- Saves results into a text file with hierarchical indentation.
- Includes a small **random delay** (100–150 ms) between requests to avoid overloading the target server.
- Logs visited pages, skipped URLs, and errors with `java.util.logging`.

---

## Project Structure

src/example/  
│  
├── CrawlTask.java # RecursiveTask handling crawling logic  
├── Test1.java # Entry point (starts the crawler)

---

## How It Works

1. `Test1` launches the crawler with a **ForkJoinPool** starting from a base URL.
2. `CrawlTask`:
   - Connects to the URL via Jsoup.
   - Extracts all `<a href>` links.
   - Writes the current URL into the output file with indentation equal to its depth in the tree.
   - Creates and forks subtasks for child links.
   - Joins subtasks before completing execution.

---

## Example Output

If the crawler is launched at `https://sendel.ru/`, the output file (`site_map.txt`) may look like this:

https://sendel.ru/
    https://sendel.ru/about
    https://sendel.ru/contact
        https://sendel.ru/contact/form
    https://sendel.ru/blog
        https://sendel.ru/blog/post1
        https://sendel.ru/blog/post2

---

## Requirements

- Java 11 or higher
- [Jsoup library](https://jsoup.org/download)

Add Jsoup to your project (for example, via Maven):

```xml
<dependency>
  <groupId>org.jsoup</groupId>
  <artifactId>jsoup</artifactId>
  <version>1.15.4</version>
</dependency>
Or download the JAR manually and add it to your classpath.
Running the Project
Clone or download the repository.
Ensure Jsoup is available on your classpath.
Compile and run:
javac -cp jsoup-1.15.4.jar src/example/*.java
java -cp .:jsoup-1.15.4.jar src/example/Test1
Check the output file:
ForkJoinPoolExample/File/site_map.txt
Notes
The crawler is limited to the base domain (https://sendel.ru in this example).
It skips duplicate URLs and fragment links (#anchor).
This project is intended for educational purposes. Do not run it against websites without permission.
License
This project is distributed for learning purposes only.
No license is attached; feel free to modify and extend it.
