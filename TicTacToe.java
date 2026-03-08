import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends JFrame implements ActionListener {

    JButton[] buttons = new JButton[9];
    JButton resetButton, undoButton;

    boolean playerX = true;
    int lastMove = -1;

    public TicTacToe() {

        setTitle("Tic Tac Toe");
        setSize(400,500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel board = new JPanel();
        board.setLayout(new GridLayout(3,3));

        for(int i=0;i<9;i++){
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial",Font.BOLD,60));
            buttons[i].addActionListener(this);
            board.add(buttons[i]);
        }

        JPanel controlPanel = new JPanel();

        resetButton = new JButton("Reset");
        undoButton = new JButton("Undo");

        resetButton.addActionListener(e -> resetBoard());
        undoButton.addActionListener(e -> undoMove());

        controlPanel.add(resetButton);
        controlPanel.add(undoButton);

        add(board, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        JButton clicked = (JButton)e.getSource();

        for(int i=0;i<9;i++){

            if(clicked == buttons[i] && buttons[i].getText().equals("")){

                if(playerX){
                    buttons[i].setText("X");
                    playerX = false;
                } else {
                    buttons[i].setText("O");
                    playerX = true;
                }

                lastMove = i;
                checkWinner();
            }
        }
    }

    void checkWinner(){

        int[][] wins = {
                {0,1,2},{3,4,5},{6,7,8},
                {0,3,6},{1,4,7},{2,5,8},
                {0,4,8},{2,4,6}
        };

        for(int[] w : wins){

            if(!buttons[w[0]].getText().equals("") &&
               buttons[w[0]].getText().equals(buttons[w[1]].getText()) &&
               buttons[w[1]].getText().equals(buttons[w[2]].getText())){

                // highlight winning row
                buttons[w[0]].setBackground(Color.GREEN);
                buttons[w[1]].setBackground(Color.GREEN);
                buttons[w[2]].setBackground(Color.GREEN);

                JOptionPane.showMessageDialog(this,
                        buttons[w[0]].getText() + " Wins!");

                resetBoard();
                return;
            }
        }

        boolean draw = true;

        for(JButton b : buttons){
            if(b.getText().equals("")){
                draw = false;
                break;
            }
        }

        if(draw){
            JOptionPane.showMessageDialog(this,"Match Draw!");
            resetBoard();
        }
    }

    void undoMove(){

        if(lastMove != -1){

            buttons[lastMove].setText("");

            playerX = !playerX;

            lastMove = -1;
        }
    }

    void resetBoard(){

        for(JButton b : buttons){
            b.setText("");
            b.setBackground(null);
        }

        playerX = true;
        lastMove = -1;
    }

    public static void main(String[] args) {

        new TicTacToe();
    }
}
