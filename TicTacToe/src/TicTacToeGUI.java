import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TicTacToeGUI extends JFrame implements ActionListener {
    private char[][] board;
    private JButton[][] buttons;
    private char currentPlayer;
    private JLabel statusLabel;
    private boolean gameOver;

    public TicTacToeGUI() {
        // Initialize the game board and UI components
        board = new char[3][3];
        buttons = new JButton[3][3];
        currentPlayer = 'X';
        gameOver = false;

        // Set up the frame
        setTitle("Tic-Tac-Toe");
        setLayout(new BorderLayout());
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel for the buttons
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));

        // Initialize the buttons and board
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new JButton("");
                buttons[row][col].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[row][col].setFocusPainted(false);
                buttons[row][col].addActionListener(this);
                boardPanel.add(buttons[row][col]);
                board[row][col] = ' '; // Set initial value to empty space
            }
        }

        // Label for displaying game status
        statusLabel = new JLabel("Player X's turn");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        // Add components to the frame
        add(boardPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return; // Do nothing if the game is over
        }

        // Identify which button was clicked
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (e.getSource() == buttons[row][col]) {
                    if (board[row][col] == ' ') { // Check if the cell is empty
                        board[row][col] = currentPlayer; // Set the current player's mark
                        buttons[row][col].setText(String.valueOf(currentPlayer));
                        buttons[row][col].setEnabled(false); // Disable the button

                        // Check if the current player has won
                        if (haveWon(board, currentPlayer)) {
                            statusLabel.setText("Player " + currentPlayer + " has won!");
                            gameOver = true;
                        } else if (isBoardFull()) {
                            statusLabel.setText("It's a draw!");
                            gameOver = true;
                        } else {
                            // Switch players
                            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                            statusLabel.setText("Player " + currentPlayer + "'s turn");
                        }
                    } else {
                        // Invalid move (this part was in the original code as a prompt to retry)
                        JOptionPane.showMessageDialog(this, "Invalid move. Try again!");
                    }
                }
            }
        }
    }

    // Method to check if the board is full (draw condition)
    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    // Method to check if the current player has won
    public static boolean haveWon(char[][] board, char player) {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == player && board[row][1] == player && board[row][2] == player) {
                return true;
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == player && board[1][col] == player && board[2][col] == player) {
                return true;
            }
        }

        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }

        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        new TicTacToeGUI();
    }
}
