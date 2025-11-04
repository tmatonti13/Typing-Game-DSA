public class GameStats {
    private int correctWords = 0;
    private int incorrectWords = 0;
    private int score = 0;

    public void addCorrect() {
        correctWords++;
    }

    public void addIncorrect() {
        incorrectWords++;
    }

    public void addScore(int s) {
        score += s;
    }

    public int getScore() {
        return score;
    }

    public String getEndgameStats(int totalTime) {
        int totalWords = correctWords + incorrectWords;
        double accuracy = totalWords == 0 ? 0 : (correctWords * 100.0) / totalWords;
        double wpm = (score / 10.0) / (totalTime / 60.0);
        return String.format(
            "Time's up!\nFinal Score: %d\nCorrect Words: %d\nIncorrect Words: %d\nAccuracy: %.2f%%\nWPM: %.2f",
            score, correctWords, incorrectWords, accuracy, wpm
        );
    }
}
