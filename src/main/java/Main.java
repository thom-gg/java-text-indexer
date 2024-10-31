package main.java;

public class Main {
    public static void main(String[] args) {
        Indexer indexer = new Indexer();
        ConsoleManager consoleManager = new ConsoleManager(indexer);

        consoleManager.start();

    }
}