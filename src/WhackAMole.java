import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WhackAMole {
    private static final int BOARD_WIDTH = 400;
    private static final int BOARD_HEIGHT = 450;
    private static final int BOARD_SIZE = 3;
    private static final int TILE_SIZE = 70;
    private static final int INITIAL_DELAY = 700;
    private static final Logger logger = Logger.getLogger(WhackAMole.class.getName());
    private final JButton[] board = new JButton[BOARD_SIZE * BOARD_SIZE];
    private final Random random = new Random();
    private final Timer setMoleTimer;
    private final Timer setPlantTimer;
    private final JLabel textLabel;
    private int score;
    private int currentDelay = INITIAL_DELAY;
    private JButton currentMoleTile;
    private JButton currentPlantTile;

    public WhackAMole() {


        JFrame frame = new JFrame("Whack A Mole - Mario Edition");

        ImageIcon image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assetsFiles/icon.png")));
        ImageIcon moleImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assetsFiles/monty.png")));
        ImageIcon moleIcon = new ImageIcon(moleImg.getImage().getScaledInstance(TILE_SIZE, TILE_SIZE, Image.SCALE_SMOOTH));
        ImageIcon plantIMG = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assetsFiles/piranha.png")));
        ImageIcon plantIcon = new ImageIcon(plantIMG.getImage().getScaledInstance(TILE_SIZE, TILE_SIZE, Image.SCALE_SMOOTH));

        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(image.getImage());

        frame.setLayout(new BorderLayout());

        JPanel textPanel = new JPanel();
        frame.add(textPanel, BorderLayout.NORTH);
        JPanel boardPanel = new JPanel();
        frame.add(boardPanel);

        textLabel = new JLabel();
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Score: 0");
        textLabel.setOpaque(false);

        textPanel.setLayout(new BorderLayout());
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        textPanel.add(textLabel);

        boardPanel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        score = 0;
        for (int i = 0; i < board.length; i++) {
            JButton tile = getTile(plantIcon);

            board[i] = tile;
            boardPanel.add(tile);

            tile.addActionListener(new ActionListener() {
                                       @Override
                                       public void actionPerformed(ActionEvent e) {

                                           JButton title = (JButton) e.getSource();
                                           if (title == currentMoleTile) {
                                               playWinFile();
                                               score += 10;
                                               textLabel.setText("Score: " + score);

                                           } else if (title == currentPlantTile) {
                                               playLoseFile();
                                               setMoleTimer.stop();
                                               setPlantTimer.stop();
                                               textLabel.setText("Game Over: " + score);
                                               disableBoard();

                                           }

                                       }
                                       private void playWinFile() {

                                           playFile("C:/Users/chris/Desktop/Github/JavaGame/src/assetsFiles/audio/win.wav");
                                       }

                                       private void playLoseFile() {

                                           playFile("C:/Users/chris/Desktop/Github/JavaGame/src/assetsFiles/audio/lose.wav");
                                       }

                                       private void disableBoard() {
                                           for (JButton tile : board) {
                                               tile.setEnabled(false);
                                           }
                                       }
                                   }
            );


        }

        setMoleTimer = new Timer(INITIAL_DELAY, e -> {
            if (currentMoleTile != null) {
                currentMoleTile.setIcon(null);
                currentMoleTile = null;
            }

            int num;
            do {
                num = random.nextInt(board.length);
            } while (currentPlantTile == board[num]);

            currentMoleTile = board[num];
            currentMoleTile.setIcon(moleIcon);
        });

        setPlantTimer = new Timer(INITIAL_DELAY, e -> {
            if (currentPlantTile != null) {
                currentPlantTile.setIcon(null);
                currentPlantTile = null;
            }

            int num;
            do {
                num = random.nextInt(board.length);
            } while (currentMoleTile == board[num]);

            currentPlantTile = board[num];
            currentPlantTile.setIcon(plantIcon);
        });

        JButton incrementBtn = new JButton("+300ms");
        incrementBtn.addActionListener(e -> {
            currentDelay = Math.max(300, currentDelay - 300);
            updateTimersDelay();
        });

        JButton decrementBtn = new JButton("-300ms");
        decrementBtn.addActionListener(e -> {
            currentDelay += 300;
            updateTimersDelay();
        });

        JButton restartBtn = getRestartBtn();

        styledBtn(incrementBtn);
        styledBtn(decrementBtn);
        styledBtn(restartBtn);
        textPanel.add(incrementBtn, BorderLayout.WEST);
        textPanel.add(decrementBtn, BorderLayout.EAST);
        textPanel.add(restartBtn, BorderLayout.NORTH);

        setPlantTimer.start();
        setMoleTimer.start();

        frame.setVisible(true);
    }





    private void playFile(String fileName) {
        try {
//
            File audioFile = new File(fileName).getAbsoluteFile();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
//
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
//
            clip.start();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error playing sound", ex);
        }
    }

    private JButton getRestartBtn() {
        JButton restartBtn = new JButton("Restart Game");
        restartBtn.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }

            private void restartGame() {
                setMoleTimer.stop();
                setPlantTimer.stop();
                currentMoleTile = null;
                currentPlantTile = null;
                score = 0;
                textLabel.setText("Score: " + score);

// resetTiles();
                enableBoard();
                setPlantTimer.start();
                setMoleTimer.start();

            }

            // private void resetTiles() {
            //     for(JButton title)
            // }

            private void enableBoard() {
                for (JButton tile : board) {
                    tile.setEnabled(true);
                    tile.setIcon(null);
                }
            }
        }));
        return restartBtn;
    }

    private void updateTimersDelay() {
        setMoleTimer.setDelay(currentDelay);
        setPlantTimer.setDelay(currentDelay);
    }

    private void styledBtn(JButton button) {
        button.setBackground(new Color(255, 255, 255));
        button.setFocusable(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 20));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(114, 114, 114));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(255, 255, 255, 255));
            }
        });
    }

    private JButton getTile(ImageIcon ignoredPlantIcon) {
        JButton tile = new JButton();

        tile.setBackground(new Color(255, 255, 255));
        tile.setFocusable(false);

        tile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tile.setBackground(new Color(211, 211, 211));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tile.setBackground(new Color(255, 255, 255));
            }
        });
        return tile;
    }


}
