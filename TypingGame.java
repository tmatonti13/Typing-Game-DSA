import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TypingGame extends JFrame {
    private JLabel displayLabel;
    private JLabel scoreBoard;
    private JLabel timerLabel;
    private String typedText = "";
    private int bufferIndex = 0;
    private final int bufferSize = 10;
    private WordManager wordManager;
    private GameStats stats;
    private Timer timer;
    private int totalTime;
    private int timeLeft;

    public TypingGame() {
        String[] options = {"15", "30", "60"};
        String selection = (String) JOptionPane.showInputDialog(
                null,
                "Choose your typing time (seconds):",
                "Select Time",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        totalTime = timeLeft = Integer.parseInt(selection);
        wordManager = new WordManager("assets/dream.txt", bufferSize);
        stats = new GameStats();
        setTitle("Conf Game");
        setSize(800, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        displayLabel = new JLabel("", SwingConstants.CENTER);
        displayLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(displayLabel, BorderLayout.CENTER);
        scoreBoard = new JLabel("Score:" + stats.getScore(), SwingConstants.NORTH_EAST);
        scoreBoard.setFont(new Font("Arial", Font.BOLD, 20));
        add(scoreBoard, BorderLayout.NORTH);
        timerLabel = new JLabel("Time: " + timeLeft, SwingConstants.SOUTH_EAST);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(timerLabel, BorderLayout.SOUTH);
        updateDisplay();
        startTimer();

        addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char typedChar = e.getKeyChar();
                if (typedChar == '\b') {
                    handleBackspace();
                } else {
                    typeCheck(typedChar);
                }
            }
        });
        setFocusable(true);
        setVisible(true);
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);
            if (timeLeft <= 0) {
                timer.stop();
                endGame();
            }
        });
        timer.start();
    }

    private void endGame() {
        String message = stats.getEndgameStats(totalTime);
        JOptionPane.showMessageDialog(this, message);
        System.exit(0);
    }

    private void typeCheck(char typedChar) {
        if (typedChar == ' ') {
            String typed = typedText.trim();
            String currentWord = wordManager.getCurrentWord(bufferIndex);
            if (typed.equals(currentWord)) {
                stats.addCorrect();
            } else {
                stats.addIncorrect();
            }
            stats.addScore(typed.equals(currentWord) ? 10 : 0);
            bufferIndex++;
            if (bufferIndex >= bufferSize / 2) {
                wordManager.scrollBuffer();
                bufferIndex--;
            }
            typedText = "";
            updateScore();
        } else {
            typedText += typedChar;
        }
        updateDisplay();
    }

    private void handleBackspace() {
        if (!typedText.isEmpty()) {
            typedText = typedText.substring(0, typedText.length() - 1);
        }
        updateDisplay();
    }

    private void updateScore() {
        scoreBoard.setText("Score: " + stats.getScore());
    }

    private void updateDisplay() {
        StringBuilder displayText = new StringBuilder("<html>");
        for (int i = 0; i < wordManager.getBufferSize(); i++) {
            String word = wordManager.getWord(i);
            if (i == bufferIndex) {
                displayText.append("<span style='color:black;'>");
                for (int j = 0; j < word.length(); j++) {
                    if (j < typedText.length()) {
                        if (typedText.charAt(j) == word.charAt(j)) {
                            displayText.append("<span style='color:green;'>").append(word.charAt(j)).append("</span>");
                        } else {
                            displayText.append("<span style='color:red;'>").append(word.charAt(j)).append("</span>");
                        }
                    } else {
                        displayText.append("<span style='color:black;'>").append(word.charAt(j)).append("</span>");
                    }
                }
                if (typedText.length() > word.length()) {
                    String extra = typedText.substring(word.length());
                    for (char c : extra.toCharArray()) {
                        displayText.append("<span style='color:red;'>").append(c).append("</span>");
                    }
                }
                displayText.append("<span style='color:black;'>|</span></span> ");
            } else if (i < bufferIndex) {
                displayText.append("<span style='color:gray;'>").append(word).append("</span> ");
            } else {
                displayText.append("<span style='color:black;'>").append(word).append("</span> ");
            }
        }
        displayText.append("</html>");
        displayLabel.setText(displayText.toString());
    }
}
