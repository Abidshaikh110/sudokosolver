import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SudokuSolver {
    private static final int SIZE = 9;
    private int[][] board;
    public SudokuSolver(int[][] board) {
        this.board = board;
    }
    public boolean solve() {
        return solve(0, 0);
    }
    private boolean solve(int row, int col) {
        if (row == SIZE) {
            return true;
        }
        if (board[row][col] != 0) {
            int nextRow = (col == SIZE - 1) ? row + 1 : row;
            int nextCol = (col == SIZE - 1) ? 0 : col + 1;
            return solve(nextRow, nextCol);
        }
        for (int number = 1; number <= SIZE; number++) {
            if (isSafe(row, col, number)) {
                board[row][col] = number;

                int nextRow = (col == SIZE - 1) ? row + 1 : row;
                int nextCol = (col == SIZE - 1) ? 0 : col + 1;

                if (solve(nextRow, nextCol)) {
                    return true;
                } else {
                    board[row][col] = 0;
                }
            }
        }
        return false;
    }
    private boolean isSafe(int row, int col, int number) {
        return !usedInRow(row, number) && !usedInCol(col, number) && !usedInSubgrid(row - row % 3, col - col % 3, number);
    }
    private boolean usedInRow(int row, int number) {
        for (int col = 0; col < SIZE; col++) {
            if (board[row][col] == number) {
                return true;
            }
        }
        return false;
    }

    private boolean usedInCol(int col, int number) {
        for (int row = 0; row < SIZE; row++) {
            if (board[row][col] == number) {
                return true;
            }
        }
        return false;
    }

    private boolean usedInSubgrid(int startRow, int startCol, int number) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row + startRow][col + startCol] == number) {
                    return true;
                }
            }
        }
        return false;
    }
}

public class SudokuGUI extends JFrame {
    private final JTextField[][] grid = new JTextField[9][9];
    private int[][] board = new int[9][9];

    public SudokuGUI() {
        setTitle("Sudoku Solver");
        setSize(600, 600);
        setLayout(new GridLayout(9 + 1, 9));

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                grid[row][col] = new JTextField();
                grid[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                grid[row][col].setHorizontalAlignment(JTextField.CENTER);
                add(grid[row][col]);
            }
        }

        for (int row = 0; row < 9; row += 3) {
            for (int col = 0; col < 9; col += 3) {
                Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
                setGridBorder(row, col, border);
                setGridColor(row, col, new Color(220, 220, 220));
            }
        }
        JButton solveButton = new JButton("Solve");
        solveButton.setFont(new Font("Arial", Font.BOLD, 10));
        solveButton.setBackground(Color.GREEN);
        solveButton.setForeground(Color.WHITE);
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gatherInput();
                SudokuSolver solver = new SudokuSolver(board);
                if (solver.solve()) {
                    updateGrid();
                } else {
                    JOptionPane.showMessageDialog(SudokuGUI.this, "No solution exists.", "Sudoku Solver", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        add(solveButton);
        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.BOLD, 10));
        clearButton.setBackground(Color.RED);
        clearButton.setForeground(Color.WHITE);
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearGrid();
            }
        });

        add(clearButton);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    private void gatherInput() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String text = grid[row][col].getText().trim();
                board[row][col] = text.isEmpty() ? 0 : Integer.parseInt(text);
            }
        }
    }
    private void updateGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                grid[row][col].setText(String.valueOf(board[row][col]));
            }
        }
    }

    private void clearGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                grid[row][col].setText("");
                board[row][col] = 0;
            }
        }
    }
    private void setGridColor(int startRow, int startCol, Color color) {
        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                grid[row][col].setBackground(color);
            }
        }
    }
    private void setGridBorder(int startRow, int startCol, Border border) {
        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                grid[row][col].setBorder(border);
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SudokuGUI();
            }
        });
    }
}