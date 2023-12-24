import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.Random;

public class WhackAMole {
    private static final int BOARD_WIDTH = 400;
    private static final int BOARD_HEIGHT = 450;
    private static final int BOARD_SIZE = 3;
    private static final int TILE_SIZE = 70;
    private final JButton[] board = new JButton[BOARD_SIZE * BOARD_SIZE];
    private final Random random = new Random();
    Timer setMoleTimer;
    private JButton currentMoleTile;
    private JButton currentPlantTile;
    private Timer setPlantTimer;


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

        JLabel textLabel = new JLabel();
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Score: 0");
        textLabel.setOpaque(false);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);

        boardPanel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        for (int i = 0; i < board.length; i++) {
            JButton tile = getTile(plantIcon);
            board[i] = tile;
            boardPanel.add(tile);
        }

        Timer setMoleTimer = new Timer(800, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
            }
        });


        Timer setPlantTimer = new Timer(800, new ActionListener() {
    public void actionPerformed(ActionEvent e) {
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
    }
});

        setPlantTimer.start();
        setMoleTimer.start();
        frame.setVisible(true);
    }

    private static JButton getTile(ImageIcon plantIcon) {
        JButton tile = new JButton();
        tile.setBackground(new Color(255, 255, 255));
        tile.setFocusable(false);
//        tile.setIcon(plantIcon);
        tile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle button click
            }
        });
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
