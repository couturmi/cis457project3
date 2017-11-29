import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mitchcout on 11/28/2017.
 */
public class GameGUI extends JPanel {

    private final int BOARD_SIZE = 9;

    private JPanel boardPanel, connectPanel;

    private JButton[] tiles;
    private JLabel connectLabel;
    private JTextField connectTextField;
    private JButton connectButton;

    private ButtonListener m1;

    private Player turn;

    public GameGUI(Player turn) {
        this.turn = turn;

        // create objects
        boardPanel = new JPanel();
        connectPanel = new JPanel();
        connectLabel = new JLabel("Server IP: ");
        connectTextField = new JTextField();
        connectButton = new JButton("Connect");

        // create listener for buttons
        m1 = new ButtonListener();
        connectButton.addActionListener(m1);

        // set JPanel layouts
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        connectPanel.setLayout(new BoxLayout(connectPanel, BoxLayout.X_AXIS));
        boardPanel.setLayout(new GridLayout(3,3));

        // add objects to panels
        connectPanel.add(connectLabel);
        connectPanel.add(connectTextField);
        connectPanel.add(connectButton);

        // create game board
        createBoard();

        // add JPanels to master panel
        add(connectPanel);
        add(boardPanel);
    }

    private void createBoard() {
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
        if((!tiles[0].getText().equals("") && tiles[0].getText().equals(tiles[1].getText()) && tiles[0].getText().equals(tiles[2].getText()))   // row 1
                || (!tiles[3].getText().equals("") && tiles[3].getText().equals(tiles[4].getText()) && tiles[3].getText().equals(tiles[5].getText()))   // row 2
                || (!tiles[6].getText().equals("") && tiles[6].getText().equals(tiles[7].getText()) && tiles[6].getText().equals(tiles[8].getText()))   // row 3
                || (!tiles[0].getText().equals("") && tiles[0].getText().equals(tiles[3].getText()) && tiles[0].getText().equals(tiles[6].getText()))   // col 1
                || (!tiles[1].getText().equals("") && tiles[1].getText().equals(tiles[4].getText()) && tiles[1].getText().equals(tiles[7].getText()))   // col 2
                || (!tiles[2].getText().equals("") && tiles[2].getText().equals(tiles[5].getText()) && tiles[2].getText().equals(tiles[8].getText()))   // col 3
                || (!tiles[0].getText().equals("") && tiles[0].getText().equals(tiles[4].getText()) && tiles[0].getText().equals(tiles[8].getText()))   // diag top-left to bottom-right
                || (!tiles[6].getText().equals("") && tiles[6].getText().equals(tiles[4].getText()) && tiles[6].getText().equals(tiles[2].getText()))   // diag bottom-left to top-right
                ) {
            return true;
        }
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
            // connect button
            if(e.getSource() == connectButton) {

            }
            // game tile buttons
            else {
                for (int i = 0; i < tiles.length; i++) {
                    if (e.getSource() == tiles[i]) {
                        // Only execute if it is the user's turn and the tile has not been selected
                        if (turn == Player.USER && tiles[i].getText().equals("")) {
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
}
