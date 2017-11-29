import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mitchcout on 11/28/2017.
 */
public class GameGUI extends JPanel {

    private final int BOARD_SIZE = 9;

    private JPanel boardPanel;

    private JButton[] tiles;

    private ButtonListener m1;

    public GameGUI() {
        // create objects
        boardPanel = new JPanel();

        // set JPanel layouts
        boardPanel.setLayout(new GridLayout(3,3));

        // create game board
        createBoard();

        // add JPanels to master panel
        add(boardPanel);
    }

    private void createBoard() {
        // create listener for buttons
        m1 = new ButtonListener();

        // create game board
        tiles = new JButton[BOARD_SIZE];
        for(int i = 0; i < BOARD_SIZE; i++){
            tiles[i] = new JButton("");
            tiles[i].setPreferredSize(new Dimension(100,100));
            tiles[i].addActionListener(m1);
            boardPanel.add(tiles[i]);
        }
    }

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            for(int i = 0; i < tiles.length; i++) {
                if (e.getSource() == tiles[i]) {

                }
            }
        }
    }
}
