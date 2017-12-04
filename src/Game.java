import java.io.*;
import java.net.*;
import java.util.*;


public final class Game {
	private final BOARD_SIZE = 9;

	private int board[BOARD_SIZE];

	public Game() {
		initBoard();
	}
	private void initBoard() {
		for(int i=0; i < BOARD_SIZE; i++) {
			this.board[i] = " ";
		}
	}

	// Adds a move to the board only if it is an empty space
	// Returns true if the move is added
	// Else return false because the space is already taken
	public boolean addMove(int move, int player) {
		if (board[move] == " ") {
			if (player == 1) {
				board[move] = "X";
				return true;
			}
			else {
				board[move] = "O";
				return true
			}
		}
		else {
			return false;
		}
	}

	// Checks all 8 win conditions to see if there is a winner
	public boolean isWin(int player) {
		char piece;
		if (player == 1) {
			piece = "X";
		}
		else {
			piece = "O";
		}
		if (board[0] == piece && board[1] == piece && board[2] == piece) {
			return true;
		}
		else if (board[0] == piece && board[3] == piece && board[6] == piece) {
			return true;
		}
		else if (board[0] == piece && board[4] == piece && board[8] == piece) {
			return true;
		}
		else if (board[1] == piece && board[4] == piece && board[7] == piece) {
			return true;
		}
		else if (board[2] == piece && board[5] == piece && board[8] == piece) {
			return true;
		}
		else if (board[2] == piece && board[4] == piece && board[6] == piece) {
			return true;
		}
		else if (board[3] == piece && board[4] == piece && board[5] == piece) {
			return true;
		}
		else if (board[6] == piece && board[7] == piece && board[8] == piece) {
			return true;
		}
		else {
			return false;
		}
	}
}