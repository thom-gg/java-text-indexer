package test;

import main.java.Indexer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IndexerTest {

    @Test
    @DisplayName("Test querying words without indexing data")
    void queryingWordsWithoutIndexingData() {
        Indexer indexer = new Indexer();

        List<String> result = indexer.queryWord("word");
        assertNull(result);

        result = indexer.queryWord("other");
        assertNull(result);
    }

    @Test
    @DisplayName("Test indexing text1.txt then queriying data")
    void indexingText1TXT() {
        Indexer indexer = new Indexer();

        File text1 = new File("./texts/text1.txt");
        assertTrue(text1.exists());

        indexer.indexFile(text1);

        // alteration is in text1.txt
        List<String> result = indexer.queryWord("alteration");
        assertNotNull(result);
        assertEquals(1, result.size());

        // jetbrains is not in text1.txt
        result = indexer.queryWord("jetbrains");
        assertNull(result);
    }

    @Test
    @DisplayName("Test indexing not being case sensitive")
    void indexingCaseSensitive() {
        Indexer indexer = new Indexer();

        File text1 = new File("./texts/text1.txt");
        assertTrue(text1.exists());

        indexer.indexFile(text1);

        // Travelling is in text1.txt
        List<String> result = indexer.queryWord("Travelling");
        assertNotNull(result);
        assertEquals(1,result.size());

        // The indexing is not case-sensitive, so this should return the same results
        result = indexer.queryWord("travelling");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Indexing a folder then querying data")
    void indexingFolder() {
        Indexer indexer = new Indexer();

        File folder = new File("./texts");
        assertTrue(folder.exists());
        assertTrue(folder.isDirectory());

        indexer.indexDirectory(folder);

        // alteration is only in text1.txt
        List<String> result = indexer.queryWord("alteration");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("text1.txt", result.get(0));

        // the word "in" is in all 4 of the texts
        result = indexer.queryWord("in");
        assertNotNull(result);
        assertEquals(5, result.size());
    }


}