package main.java;

import java.io.File;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// This class takes care of the interaction with the user in the console
public class ConsoleManager {
    private Indexer indexer;
    private String currentPath;
    private Scanner in;

    public ConsoleManager(Indexer indexer) {
        this.indexer = indexer;
        this.currentPath = Paths.get("").toAbsolutePath().toString();
        this.in = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println();
            System.out.println("What do you want to do ?");
            System.out.println("(1) Index new files / folders (recursively)");
            System.out.println("(2) Query indexed data");
            System.out.println("(3) Exit (you will have to index files again)");
            System.out.println("Enter a number");


            int result;
            try {
                result = in.nextInt();
            }
            catch (InputMismatchException ex) {
                System.out.println("❌ Invalid input - please enter a number");
                continue;
            }
            finally {
                // Consuming the ENTER key pressed
                in.nextLine();
            }

            switch (result) {
                case 1:
                    promptIndexFiles();
                    break;
                case 2:
                    promptQueryData();
                    break;
                case 3:
                    System.out.println("Exiting the application");
                    return;
                default:
                    System.out.println("❌ Invalid input - please enter one of the numbers specified above");
            }

        }
    }



    private void promptQueryData() {
        System.out.println("You chose querying indexed data !");
        System.out.println("Enter one or more words you want to query");

        String s = in.nextLine();

        String[] words = s.split(" ");

        for (String word: words) {
            List<String> files = indexer.queryWord(word);
            if (files == null || files.isEmpty()) {
                System.out.println("There is no files with the word \""+ word +"\" in the indexed data");
                continue;
            }
            System.out.println();
            System.out.println("\"" + word + "\" is present in " + files.size() + " files in the indexed data:");
            for (String file: files) {
                System.out.print(file + "\t");
            }
            System.out.println();
        }

    }

    private void promptIndexFiles() {
        System.out.println("You chose indexing new files / folders !");
        System.out.println("Enter relative path to files / folders to be indexed, separated by a space");
        System.out.print("Current path: " + currentPath + "\\ > ");
        String s = in.nextLine();

        String[] paths = s.split(" ");
        for (String path : paths) {
            File f = new File(path);
            if (f.exists()) {
                if (f.isDirectory()) {
                    indexer.indexDirectory(f);
                }
                else {
                    indexer.indexFile(f);
                }
            }
            else {
                System.out.println("Skipping " + path + " because it doesnt exist");
            }
        }

    }


}
