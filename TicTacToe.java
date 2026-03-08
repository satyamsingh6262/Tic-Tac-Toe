import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends JFrame implements ActionListener {

    JButton[] buttons = new JButton[9];
    boolean playerX = true;

    public TicTacToe() {

        setTitle("Tic Tac Toe");
        setSize(400,400);
        setLayout(new GridLayout(3,3));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for(int i=0;i<9;i++){
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial",Font.BOLD,60));
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        JButton clicked = (JButton)e.getSource();

        if(clicked.getText().equals("")){

            if(playerX){
                clicked.setText("X");
                playerX = false;
            } else {
                clicked.setText("O");
                playerX = true;
            }

            checkWinner();
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

                JOptionPane.showMessageDialog(this,
                        buttons[w[0]].getText()+" Wins!");

                resetBoard();
                return;
            }
        }
    }

    void resetBoard(){
        for(JButton b : buttons){
            b.setText("");
        }
        playerX = true;
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}