import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;


/**
 * Created by mitchcout on 11/28/2017.
 */
public class GameGUI extends JPanel {

    private final int BOARD_SIZE = 9;
    private final String TURN_USER_TEXT = "YOUR TURN";
    private final String TURN_OPPONENT_TEXT = "OPPONENT'S TURN";
    private final String WINNER_TEXT = "YOU WIN!!!";
    private final String LOSER_TEXT = "YOU LOSE :(";
    private final String CONNECT_BUTTON_TEXT = "Connect";
    private final String DISCONNECT_BUTTON_TEXT = "Disconnect";

    private JPanel boardPanel, connectPanel, turnPanel, namePanel, chatPanel;

    private JButton[] tiles;
    private JLabel connectLabel, turnLabel, nameLabel;
    private JTextField connectTextField, nameTextField, chatTextField;
    protected JTextArea chatTextArea;
    private JButton connectButton;
    protected JScrollPane scrollPane;

    private ButtonListener m1;
    private KeyPress k1;

    private Player turn;

    private boolean gameInProgress;

    public GameGUI() {
        gameInProgress = false;

        // create objects
        boardPanel = new JPanel();
        connectPanel = new JPanel();
        turnPanel = new JPanel();
        namePanel = new JPanel();
        chatPanel = new JPanel();
        connectLabel = new JLabel("Server IP: ");
        nameLabel = new JLabel("Name: ");
        turnLabel = new JLabel("");
        nameTextField = new JTextField();
        connectTextField = new JTextField();
        chatTextField = new JTextField();
        chatTextArea = new JTextArea();
        chatTextArea.setLineWrap(true);
        connectButton = new JButton(CONNECT_BUTTON_TEXT);
        scrollPane = new JScrollPane(chatTextArea);

        // create listener for buttons
        m1 = new ButtonListener();
        k1 = new KeyPress();
        connectButton.addActionListener(m1);
        chatTextField.addKeyListener(k1);


        // alter views
        turnLabel.setFont(new Font("Arial", Font.BOLD, 28));
        chatTextArea.setPreferredSize(new Dimension(300,120));

        // set JPanel layouts
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        connectPanel.setLayout(new BoxLayout(connectPanel, BoxLayout.X_AXIS));
        boardPanel.setLayout(new GridLayout(3,3));
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        // add objects to panels
        namePanel.add(nameLabel);
        namePanel.add(nameTextField);
        connectPanel.add(connectLabel);
        connectPanel.add(connectTextField);
        connectPanel.add(connectButton);
        turnPanel.add(turnLabel);
        chatPanel.add(scrollPane);
        chatPanel.add(chatTextField);

        // create game board
        createBoard();

        // add JPanels to master panel
        add(namePanel);
        add(connectPanel);
        add(boardPanel);
        add(turnPanel);
        add(chatPanel);
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
            tiles[i].setEnabled(false);
            boardPanel.add(tiles[i]);
        }
    }

    public void enableBoard() {
        gameInProgress = true;

        // enable game board tiles
        if(tiles != null) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                tiles[i].setEnabled(true);
            }
        }

        // update connect panel
        connectButton.setText(DISCONNECT_BUTTON_TEXT);
        nameTextField.setEnabled(false);
        connectTextField.setEnabled(false);
        chatTextField.setEnabled(true);
    }

    private void disableBoard() {
        gameInProgress = false;

        // disable game board tiles
        disableTiles(true);

        // update connect panel
        connectButton.setText(CONNECT_BUTTON_TEXT);
        nameTextField.setEnabled(true);
        connectTextField.setEnabled(true);
        chatTextField.setEnabled(false);
    }

    private void disableTiles(boolean clearText) {
        if(tiles != null) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                tiles[i].setEnabled(false);
                if(clearText) {
                    tiles[i].setText("");
                }
            }
        }
    }

    public void selectStartingTurn(Player turn) {
        this.turn = turn;
        setTurnLabel(turn);
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

    public void updateOpponentsMove(Move opponentMove) {
        int buttonId = opponentMove.getTileId();
        // update button
        tiles[buttonId].setText("X");
        tiles[buttonId].setForeground(Color.RED);

        // check if opponent has won
        if(opponentMove.isUserWon()) {
            updateWinner(Player.OPPONENT);
        } else {
            // update turn
            turn = Player.USER;
            setTurnLabel(Player.USER);
        }
    }

    private void setTurnLabel(Player playerTurn) {
        if(playerTurn == Player.USER) {
            turnLabel.setText(TURN_USER_TEXT);
            turnLabel.setForeground(Color.BLUE);

        } else if(playerTurn == Player.OPPONENT) {
            turnLabel.setText(TURN_OPPONENT_TEXT);
            turnLabel.setForeground(Color.RED);
        }
    }

    private void updateWinner(Player player) {
        if(player == Player.OPPONENT){
            turnLabel.setText(LOSER_TEXT);
            turnLabel.setForeground(Color.MAGENTA);
        } else if(player == Player.USER) {
            turnLabel.setText(WINNER_TEXT);
            turnLabel.setForeground(Color.ORANGE);
        }
        disableTiles(false);
    }

    private class KeyPress extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                FTPClient.sendChat(chatTextField.getText());
                chatTextField.setText("");
            }
        }  
    }
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // connect button
            if(e.getSource() == connectButton) {
                if(!gameInProgress) {
                    try {
                        FTPClient.connectToServer(connectTextField.getText(), nameTextField.getText());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        return;
                    }
                } else {
                    try {
                        FTPClient.disconnect();
                        disableBoard();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        return;
                    }
                }
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

                            // update turn
                            if(!userWon) {
                                turn = Player.OPPONENT;
                                setTurnLabel(Player.OPPONENT);
                            }
                        }
                    }
                }
            }
        }
    }
}
