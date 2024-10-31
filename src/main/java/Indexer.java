package main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Indexer {
    // We use a Map, so we can get the list of files containing a given word in O(1)
    // The key is the word, a String, and the value is a Set, so we can have a list of files without repeating files
    private Map<String, Set<String>> dataIndexed;

    public Indexer() {
        this.dataIndexed = new HashMap<>();
    }

    private void addOccurency(String word, File file) {
        // We don't want the indexing to be case-sensitive
        word = word.toLowerCase();
        Set<String> set;
        // We get the previous set, add a value inside and put it back
        if (this.dataIndexed.containsKey(word)) {
            set = this.dataIndexed.get(word);
        }
        else {
            set = new HashSet<String>();
        }
        String fileName = file.getName();
        set.add(fileName);
        this.dataIndexed.put(word, set);
    }

    //
    public void indexDirectory(File directory) {
        // This is normally checked before this method is called, but still checking
        if (!directory.isDirectory()) {return;}
        File[] files = directory.listFiles();
        if (files == null) {return;}
        for (File f: files) {
            if (f.isDirectory()) {
                indexDirectory(f);
            }
            else {
                this.indexFile(f);
            }
        }
    }

    public void indexFile(File file) {
        // Checking that file is a .txt file
        String fileName = file.getName();
        String[] parts = fileName.split("\\.");
        if (!parts[parts.length - 1].equals("txt")) {
            System.out.println("Skipping " + fileName + " indexing, as it is not a .txt file");
            return;
        }
        System.out.println("⏳ Indexing file " + file.getName() + "...");
        try {
            // Iterating on each char of the word
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentWord = "";
            int cInt;
            while ((cInt = reader.read()) != -1) {
                char c = (char) cInt;
                if (!Character.isLetter(c)) {
                    // Potential ending of currentWord
                    if (!currentWord.isEmpty()) {
                        this.addOccurency(currentWord, file);
                    }
                    currentWord = "";
                }
                else {
                    // Continuing to build the currentWord
                    currentWord += c;
                }
            }
            // Add the last word
            if (!currentWord.isEmpty()) {
                this.addOccurency(currentWord, file);
            }
        }
        catch (Throwable ex) {
            // This should never happen since we check if file exists before calling this method
            System.out.println("Error, this file does not exist");
            return;
        }
        System.out.println("✅ Done !");
    }

    // Return the list of file names where this word is included
    public List<String> queryWord(String word) {
        // We don't want the indexing to be case-sensitive
        word = word.toLowerCase();

        if (this.dataIndexed.containsKey(word)) {
            Set<String> files = this.dataIndexed.get(word);
            return new ArrayList<>(files);
        }
        return null;



    }
}
