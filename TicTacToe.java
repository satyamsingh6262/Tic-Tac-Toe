import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToe extends JFrame implements ActionListener {

    // ─── Colors ───────────────────────────────────────────────────
    private static final Color COLOR_BG    = new Color(30, 30, 46);
    private static final Color COLOR_PANEL = new Color(49, 50, 68);
    private static final Color COLOR_BTN   = new Color(69, 71, 90);
    private static final Color COLOR_X     = new Color(243, 139, 168);
    private static final Color COLOR_O     = new Color(137, 220, 235);
    private static final Color COLOR_WIN   = new Color(166, 227, 161);
    private static final Color COLOR_TEXT  = new Color(205, 214, 244);
    private static final Color COLOR_ACCENT= new Color(203, 166, 247);

    // ─── UI Components ────────────────────────────────────────────
    private JButton[] cells = new JButton[9];
    private JButton   resetBtn, undoBtn, modeBtn;
    private JLabel    statusLabel, scoreLabel;

    // ─── Game State ───────────────────────────────────────────────
    private boolean playerXTurn = true;
    private int     lastMove    = -1;
    private boolean gameOver    = false;
    private int     scoreX = 0, scoreO = 0, draws = 0;

    // ─── Mode & Difficulty ────────────────────────────────────────
    enum Mode       { TWO_PLAYER, VS_AI }
    enum Difficulty { EASY, MEDIUM, HARD }

    private Mode       mode       = Mode.TWO_PLAYER;
    private Difficulty difficulty = Difficulty.MEDIUM;

    private final Random random = new Random();

    private final int[][] WIN_COMBOS = {
        {0,1,2},{3,4,5},{6,7,8},
        {0,3,6},{1,4,7},{2,5,8},
        {0,4,8},{2,4,6}
    };

    // ═════════════════════════════════════════════════════════════
    public TicTacToe() {
        showStartDialog();
        buildUI();
    }

    // ─── Mode Selection Dialog ────────────────────────────────────
    private void showStartDialog() {
        JDialog dialog = new JDialog(this, "Choose Game Mode", true);
        dialog.setSize(360, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(COLOR_BG);
        dialog.setResizable(false);

        JLabel title = new JLabel("Tic Tac Toe", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(COLOR_ACCENT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
        dialog.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(4, 1, 8, 8));
        center.setBackground(COLOR_BG);
        center.setBorder(BorderFactory.createEmptyBorder(5, 40, 20, 40));

        JButton twoPlayerBtn = dialogBtn("2 Player");
        JButton easyBtn      = dialogBtn("vs AI  —  Easy");
        JButton mediumBtn    = dialogBtn("vs AI  —  Medium");
        JButton hardBtn      = dialogBtn("vs AI  —  Hard");

        center.add(twoPlayerBtn);
        center.add(easyBtn);
        center.add(mediumBtn);
        center.add(hardBtn);

        dialog.add(center, BorderLayout.CENTER);

        twoPlayerBtn.addActionListener(e -> { mode = Mode.TWO_PLAYER;                              dialog.dispose(); });
        easyBtn.addActionListener(e      -> { mode = Mode.VS_AI; difficulty = Difficulty.EASY;     dialog.dispose(); });
        mediumBtn.addActionListener(e    -> { mode = Mode.VS_AI; difficulty = Difficulty.MEDIUM;   dialog.dispose(); });
        hardBtn.addActionListener(e      -> { mode = Mode.VS_AI; difficulty = Difficulty.HARD;     dialog.dispose(); });

        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);
    }

    private JButton dialogBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setBackground(COLOR_BTN);
        btn.setForeground(COLOR_TEXT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_ACCENT); btn.setForeground(COLOR_BG); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(COLOR_BTN);    btn.setForeground(COLOR_TEXT); }
        });
        return btn;
    }

    // ─── Main Window ─────────────────────────────────────────────
    private void buildUI() {
        setTitle("Tic Tac Toe");
        setSize(440, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 4));
        header.setBackground(COLOR_BG);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        String modeText = (mode == Mode.TWO_PLAYER)
                ? "2 Player Mode"
                : "vs AI  |  " + difficulty + " difficulty";
        JLabel modeLabel = new JLabel(modeText, SwingConstants.CENTER);
        modeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        modeLabel.setForeground(COLOR_ACCENT);

        scoreLabel = new JLabel(getScoreText(), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        scoreLabel.setForeground(COLOR_TEXT);

        header.add(modeLabel);
        header.add(scoreLabel);
        add(header, BorderLayout.NORTH);

        // Board
        JPanel board = new JPanel(new GridLayout(3, 3, 6, 6));
        board.setBackground(COLOR_BG);
        board.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        for (int i = 0; i < 9; i++) {
            cells[i] = new JButton("");
            cells[i].setFont(new Font("Segoe UI", Font.BOLD, 56));
            cells[i].setBackground(COLOR_BTN);
            cells[i].setForeground(COLOR_TEXT);
            cells[i].setFocusPainted(false);
            cells[i].setBorderPainted(false);
            cells[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            cells[i].addActionListener(this);
            board.add(cells[i]);
        }
        add(board, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new BorderLayout(0, 8));
        footer.setBackground(COLOR_BG);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));

        statusLabel = new JLabel("X's Turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        statusLabel.setForeground(COLOR_X);
        footer.add(statusLabel, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 8, 0));
        btnPanel.setBackground(COLOR_BG);

        undoBtn  = controlBtn("Undo");
        resetBtn = controlBtn("Reset");
        modeBtn  = controlBtn("Change Mode");

        undoBtn.addActionListener(e  -> undoMove());
        resetBtn.addActionListener(e -> resetBoard());
        modeBtn.addActionListener(e  -> { dispose(); new TicTacToe(); });

        btnPanel.add(undoBtn);
        btnPanel.add(resetBtn);
        btnPanel.add(modeBtn);
        footer.add(btnPanel, BorderLayout.SOUTH);
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton controlBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(COLOR_PANEL);
        btn.setForeground(COLOR_TEXT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_ACCENT); btn.setForeground(COLOR_BG); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(COLOR_PANEL);  btn.setForeground(COLOR_TEXT); }
        });
        return btn;
    }

    // ─── Player Move ─────────────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;
        JButton clicked = (JButton) e.getSource();
        for (int i = 0; i < 9; i++) {
            if (clicked == cells[i] && cells[i].getText().equals("")) {
                makeMove(i, "X");
                if (!gameOver && mode == Mode.VS_AI) {
                    triggerAIMove();
                }
                return;
            }
        }
    }

    private void makeMove(int index, String symbol) {
        lastMove = index;
        cells[index].setText(symbol);
        cells[index].setForeground(symbol.equals("X") ? COLOR_X : COLOR_O);
        playerXTurn = !playerXTurn;
        updateStatus();
        checkWinner();
    }

    // ─── AI Move (with delay for natural feel) ───────────────────
    private void triggerAIMove() {
        statusLabel.setText("AI Thinking...");
        statusLabel.setForeground(COLOR_O);
        Timer delay = new Timer(500, e -> {
            if (!gameOver) {
                int move = switch (difficulty) {
                    case EASY   -> aiEasy();
                    case MEDIUM -> aiMedium();
                    case HARD   -> aiHard();
                };
                if (move != -1) makeMove(move, "O");
            }
        });
        delay.setRepeats(false);
        delay.start();
    }

    // Easy: random cell
    private int aiEasy() {
        List<Integer> empty = emptyCells();
        return empty.isEmpty() ? -1 : empty.get(random.nextInt(empty.size()));
    }

    // Medium: win > block > center > random
    private int aiMedium() {
        int move;
        move = findWinningMove("O"); if (move != -1) return move;
        move = findWinningMove("X"); if (move != -1) return move;
        if (cells[4].getText().equals("")) return 4;
        return aiEasy();
    }

    /** Returns the index that completes 2-in-a-row for 'symbol', or -1 */
    private int findWinningMove(String symbol) {
        for (int[] w : WIN_COMBOS) {
            int blank = -1, count = 0;
            for (int idx : w) {
                if (cells[idx].getText().equals(symbol)) count++;
                else if (cells[idx].getText().equals("")) blank = idx;
            }
            if (count == 2 && blank != -1) return blank;
        }
        return -1;
    }

    // Hard: Minimax (unbeatable)
    private int aiHard() {
        int best = Integer.MIN_VALUE, bestMove = -1;
        for (int i : emptyCells()) {
            cells[i].setText("O");
            int score = minimax(false, 0);
            cells[i].setText("");
            if (score > best) { best = score; bestMove = i; }
        }
        return bestMove;
    }

    private int minimax(boolean isMaximizing, int depth) {
        String winner = getWinner();
        if (winner != null) return winner.equals("O") ? 10 - depth : depth - 10;
        if (emptyCells().isEmpty()) return 0;

        if (isMaximizing) {
            int best = Integer.MIN_VALUE;
            for (int i : emptyCells()) {
                cells[i].setText("O");
                best = Math.max(best, minimax(false, depth + 1));
                cells[i].setText("");
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i : emptyCells()) {
                cells[i].setText("X");
                best = Math.min(best, minimax(true, depth + 1));
                cells[i].setText("");
            }
            return best;
        }
    }

    private String getWinner() {
        for (int[] w : WIN_COMBOS) {
            String a = cells[w[0]].getText();
            if (!a.equals("") && a.equals(cells[w[1]].getText()) && a.equals(cells[w[2]].getText()))
                return a;
        }
        return null;
    }

    private List<Integer> emptyCells() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 9; i++)
            if (cells[i].getText().equals("")) list.add(i);
        return list;
    }

    // ─── Win / Draw Check ─────────────────────────────────────────
    private void checkWinner() {
        for (int[] w : WIN_COMBOS) {
            String a = cells[w[0]].getText();
            if (!a.equals("") && a.equals(cells[w[1]].getText()) && a.equals(cells[w[2]].getText())) {
                for (int idx : w) {
                    cells[idx].setBackground(COLOR_WIN);
                    cells[idx].setForeground(COLOR_BG);
                }
                gameOver = true;
                if (a.equals("X")) { scoreX++; statusLabel.setText("X Wins!"); statusLabel.setForeground(COLOR_X); }
                else               { scoreO++; statusLabel.setText("O Wins!"); statusLabel.setForeground(COLOR_O); }
                scoreLabel.setText(getScoreText());
                String msg = (mode == Mode.VS_AI) ? (a.equals("X") ? "You Win!" : "AI Wins!") : (a + " Wins!");
                showResultDialog(msg);
                return;
            }
        }
        if (emptyCells().isEmpty()) {
            gameOver = true;
            draws++;
            statusLabel.setText("Draw!");
            statusLabel.setForeground(COLOR_TEXT);
            scoreLabel.setText(getScoreText());
            showResultDialog("It's a Draw!");
        }
    }

    private void showResultDialog(String msg) {
        Timer t = new Timer(350, e -> {
            int choice = JOptionPane.showOptionDialog(this, msg, "Game Over",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new Object[]{"Play Again", "Change Mode"}, "Play Again");
            if (choice == 0) resetBoard();
            else { dispose(); new TicTacToe(); }
        });
        t.setRepeats(false);
        t.start();
    }

    // ─── Undo ─────────────────────────────────────────────────────
    private void undoMove() {
        if (gameOver || lastMove == -1) return;
        cells[lastMove].setText("");
        cells[lastMove].setBackground(COLOR_BTN);
        cells[lastMove].setForeground(COLOR_TEXT);
        playerXTurn = !playerXTurn;
        lastMove = -1;
        updateStatus();
    }

    // ─── Reset ────────────────────────────────────────────────────
    private void resetBoard() {
        for (JButton b : cells) {
            b.setText("");
            b.setBackground(COLOR_BTN);
            b.setForeground(COLOR_TEXT);
        }
        playerXTurn = true;
        lastMove    = -1;
        gameOver    = false;
        updateStatus();
    }

    private void updateStatus() {
        if (gameOver) return;
        boolean xTurn = playerXTurn;
        if (mode == Mode.TWO_PLAYER) {
            statusLabel.setText(xTurn ? "X's Turn" : "O's Turn");
            statusLabel.setForeground(xTurn ? COLOR_X : COLOR_O);
        } else {
            statusLabel.setText(xTurn ? "Your Turn" : "AI Thinking...");
            statusLabel.setForeground(xTurn ? COLOR_X : COLOR_O);
        }
    }

    private String getScoreText() {
        String xLabel = (mode == Mode.VS_AI) ? "You (X)" : "X";
        String oLabel = (mode == Mode.VS_AI) ? "AI (O)"  : "O";
        return xLabel + ": " + scoreX + "   Draw: " + draws + "   " + oLabel + ": " + scoreO;
    }

    // ─── Entry Point ─────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new TicTacToe();
        });
    }
}
