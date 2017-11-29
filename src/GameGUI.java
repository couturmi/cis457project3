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

    private Player turn;

    public GameGUI(Player turn) {
        this.turn = turn;

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
            tiles[i].setFont(new Font("Arial", Font.BOLD, 40));
            tiles[i].setFocusPainted(false);
            tiles[i].setBackground(Color.WHITE);
            tiles[i].addActionListener(m1);
            boardPanel.add(tiles[i]);
        }
    }

    private boolean checkIfPlayerWon() {

        return false;
    }

    private void updateWinner(Player player) {

    }

    public void updateOpponentsMove(Move opponentMove) {
        int buttonId = opponentMove.getTileId();
        // update button
        tiles[buttonId].setText("X");
        tiles[buttonId].setForeground(Color.RED);
        tiles[buttonId].setEnabled(false);

        if(opponentMove.isUserWon()) {
            updateWinner(Player.OPPONENT);
        }
    }

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            for(int i = 0; i < tiles.length; i++) {
                if (e.getSource() == tiles[i]) {
                    // Only execute if it is the user's turn and the tile has not been selected
                    if(turn == Player.USER && tiles[i].getText().equals("")) {
                        // update button
                        tiles[i].setText("O");
                        tiles[i].setForeground(Color.BLUE);

                        // check if user has won the game
                        boolean userWon = false;
                        if (checkIfPlayerWon()) {
                            userWon = true;
                            updateWinner(Player.USER);
                        }

                        // send move data to other player
                        Move move = new Move(i, userWon);
                        FTPClient.sendMoveToServer(move);
                    }
                }
            }
        }
    }
}
