import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class WordManager {
    private List<String> wordBank = new ArrayList<>();
    private List<String> wordBuffer = new ArrayList<>();
    private final int bufferSize;

    public WordManager(String filepath, int bufferSize) {
        this.bufferSize = bufferSize;
        loadWords(filepath);
        initBuffer();
    }

    private void loadWords(String filename) {
        try {
            Path path = Paths.get(filename);
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] splitWords = line.trim().split("\\s+");
                wordBank.addAll(Arrays.asList(splitWords));
            }
        } catch (Exception e) {
            System.err.println("Could not load file: " + filename);
        }
    }

    private void initBuffer() {
        Random rand = new Random();
        wordBuffer.clear();
        for (int i = 0; i < bufferSize; i++) {
            wordBuffer.add(wordBank.get(rand.nextInt(wordBank.size())));
        }
    }

    public void scrollBuffer() {
        Random rand = new Random();
        wordBuffer.remove(0);
        wordBuffer.add(wordBank.get(rand.nextInt(wordBank.size())));
    }

    public String getWord(int index) {
        return wordBuffer.get(index);
    }

    public String getCurrentWord(int index) {
        return wordBuffer.get(index);
    }

    public int getBufferSize() {
        return wordBuffer.size();
    }
}
