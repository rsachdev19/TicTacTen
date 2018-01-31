package tictacten;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TicTacTen extends JPanel implements KeyListener, MouseListener, MouseMotionListener {

    public static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    public JFrame frame;
    public boolean firstTurn = true;
    public int playerTurn = 1;
    public int[][][][] boardSub = new int[3][3][3][3]; //0 = unocuppied,   1 = X,   2 = O 
    public int[][] boardMain = new int[3][3];
    public int locationMainRow = 1;
    public int locationMainColumn = 1;
    public int locationSubRow = 1;
    public int locationSubColumn = 1;
    public Color faintYellow = new Color(255, 246, 143);
    public boolean gameOver = false;
    public int winner;
    public int turn = 1;
    public boolean tie = false;
    public boolean gameScreen = true;
    public String winMethod;

    public static void main(String[] args) {
        TicTacTen project = new TicTacTen("Title");
    }

    public TicTacTen(String title) {

        frame = new JFrame(title);
        frame.setSize(900, 940);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(this);
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int m = 0; m < 3; m++) {
                        boardSub[i][j][k][m] = 0;
                    }
                }
            }
        }

    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        if (gameScreen) {
            g.setColor(faintYellow);
            g.fillRect(locationMainColumn * 300 + locationSubColumn * 100, locationMainRow * 300 + locationSubRow * 100, 100, 100);
            g.setColor(Color.black);
            g.drawString("Player turn:" + playerTurn, 5, 20);
            drawBoard(g);
            g.setColor(faintYellow);
            g2.setStroke(new BasicStroke(10));
            if (!firstTurn) {
                g.drawRect(locationMainColumn * 300, locationMainRow * 300, 300, 300); //around the tic tac toe board
            } else {
                g.drawRect(0, 0, 890, 905); //Around the tic tac ten board
            }
        }
        if (gameOver) {
            g.drawString("Winner: Player " + winner, 450, 450);
            g.drawString("Win method: " + winMethod, 450, 460);
            g.drawString("Press R to restart", 450, 470);

        }
        repaint();
    }

    public void drawBoard(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 1; i < 9; i++) {
            if (i % 3 == 0) {
                g2.setStroke(new BasicStroke(10));
            } else {
                g2.setStroke(new BasicStroke(1));
            }
            g.drawLine(100 * i, 0, 100 * i, 900);
            g.drawLine(0, 100 * i, 900, 100 * i);
        }
        g2.setStroke(new BasicStroke(1));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int m = 0; m < 3; m++) {
                        if (boardSub[i][j][k][m] == 1) {
                            g.drawLine(j * 300 + m * 100, i * 300 + k * 100, j * 300 + (m + 1) * 100, i * 300 + (k + 1) * 100);
                            g.drawLine(j * 300 + m * 100, i * 300 + (k + 1) * 100, j * 300 + (m + 1) * 100, i * 300 + k * 100);
                        } else if (boardSub[i][j][k][m] == 2) {
                            g.drawOval(j * 300 + m * 100, i * 300 + k * 100, 100, 100);
                        }
                    }
                }
            }
        }
        g2.setStroke(new BasicStroke(3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardMain[i][j] == 1) {
                    g.drawLine(j * 300, i * 300, (j + 1) * 300, (i + 1) * 300);
                    g.drawLine((j + 1) * 300, i * 300, j * 300, (i + 1) * 300);
                }
                if (boardMain[i][j] == 2) {
                    g.drawOval(j*300,i*300,300,300);
                }
            }
        }
    }

    public void endTurn() {
        turn++;
        int counter = 0;
        boardSub[locationMainRow][locationMainColumn][locationSubRow][locationSubColumn] = playerTurn;
        checkWinSub();
        locationMainRow = locationSubRow;
        locationMainColumn = locationSubColumn;
        locationSubRow = 1;
        locationSubColumn = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardSub[locationMainRow][locationMainColumn][i][j] != 0) {
                    counter++;
                }
            }
        }
        if (counter == 9) { //Accounts for a filled 3x3 sub square
            firstTurn = true;
        }
        if (playerTurn == 1) {
            playerTurn = 2;
        } else if (playerTurn == 2) {
            playerTurn = 1;
        }

    }

    public void checkWinSub() {
        int count = 1;
        try {
            //CHECK IF THE PLAYER WINS HERE FROM THE PLACE THEY PUT IT --> board[locationRow][locationColumn]
            for (int i = 1; i < 3; i++) {
                if (boardSub[locationMainRow][locationMainColumn][locationSubRow][locationSubColumn + i] == playerTurn) { //Check to the right
                    count++;
                    checkCountSub(count, "2 to the right");
                } else {
                    count = 1;
                    break;
                }
            }
        } catch (Exception e) {
            count = 1;
        }
        try { //Check 2 to the left
            for (int i = 1; i < 3; i++) {
                if (boardSub[locationMainRow][locationMainColumn][locationSubRow][locationSubColumn - i] == playerTurn) {
                    count++;
                    checkCountSub(count, "2 to the left");
                } else {
                    count = 1;
                    break;
                }
            }
        } catch (Exception e) {
            count = 1;
        }
        try { //Check downwards
            if (boardSub[locationMainRow][locationMainColumn][locationSubRow + 1][locationSubColumn] == playerTurn && boardSub[locationMainRow][locationMainColumn][locationSubRow + 2][locationSubColumn] == playerTurn) {
                count += 2;
                checkCountSub(count, "2 downwards");
            }
        } catch (Exception e) {
            count = 1;
        }
        try { //Checking 2 upwards
            if (boardSub[locationMainRow][locationMainColumn][locationSubRow - 1][locationSubColumn] == playerTurn && boardSub[locationMainRow][locationMainColumn][locationSubRow - 2][locationSubColumn] == playerTurn) {
                count += 2;
                checkCountSub(count, "2 upwards");
            }
        } catch (Exception e) {
            count = 1;
        }
        try {//Going 1top and 1bottom
            if (boardSub[locationMainRow][locationMainColumn][locationSubRow - 1][locationSubColumn] == playerTurn && boardSub[locationMainRow][locationMainColumn][locationSubRow + 1][locationSubColumn] == playerTurn) {
                count += 2;
                checkCountSub(count, "1 up, 1 down");
            }
        } catch (Exception e) {
            count = 1;
        }
        try { //Going 1top left --> 1bottom right
            if (boardSub[locationMainRow][locationMainColumn][locationSubRow - 1][locationSubColumn - 1] == playerTurn && boardSub[locationMainRow][locationMainColumn][locationSubRow + 1][locationSubColumn + 1] == playerTurn) {
                count += 2;
                checkCountSub(count, "1 top left 1 bottom right");
            }
        } catch (Exception e) {
            count = 1;
        }
        try {//Going 2bottom right
            if (boardSub[locationMainRow][locationMainColumn][locationSubRow + 1][locationSubColumn + 1] == playerTurn && boardSub[locationMainRow][locationMainColumn][locationSubRow + 2][locationSubColumn + 2] == playerTurn) {
                count += 2;
                checkCountSub(count, "2 bottom right");
            }
        } catch (Exception e) {
            count = 1;
        }
        try {//going 1bottom left --> 1 top right
            if (boardSub[locationMainRow][locationMainColumn][locationSubRow + 1][locationSubColumn - 1] == playerTurn && boardSub[locationMainRow][locationMainColumn][locationSubRow - 1][locationSubColumn + 1] == playerTurn) {
                count += 2;
                checkCountSub(count, "1 botto left 1 top right");
            }
        } catch (Exception e) {
            count = 1;
        }
        try { //Going 2 top left
            for (int i = 1; i < 3; i++) {
                if (boardSub[locationMainRow][locationMainColumn][locationSubRow - i][locationSubColumn - i] == playerTurn) {
                    count++;
                    checkCountSub(count, "2 top left");
                } else {
                    count = 1;
                    break;
                }
            }
        } catch (Exception e) {
            count = 1;
        }
        try { //Going 2 bottom left
            for (int i = 1; i < 3; i++) {
                if (boardSub[locationMainRow][locationMainColumn][locationSubRow + i][locationSubColumn - i] == playerTurn) {
                    count++;
                    checkCountSub(count, "2 bottom left");
                } else {
                    count = 1;
                    break;
                }
            }
        } catch (Exception e) {
            count = 1;
        }
        try { //Going 2 top right
            if (boardSub[locationMainRow][locationMainColumn][locationSubRow - 1][locationSubColumn + 1] == playerTurn && boardSub[locationMainRow][locationMainColumn][locationSubRow - 2][locationSubColumn + 2] == playerTurn) {
                count += 2;
                checkCountSub(count, "2 top right");
            }

        } catch (Exception e) {
            count = 1;
        }
    }

    public void checkCountSub(int count, String tempString) {
        int countMain = 1;
        if (count == 3) {
            if (boardMain[locationMainRow][locationMainColumn] == 0) {
                boardMain[locationMainRow][locationMainColumn] = playerTurn;
                //System.out.println(tempString);
                try {
                    //CHECK IF THE PLAYER WINS HERE FROM THE PLACE THEY PUT IT --> board[locationRow][locationColumn]
                    for (int i = 1; i < 3; i++) {
                        if (boardMain[locationMainRow][locationMainColumn + i] == playerTurn) { //Check to the right
                            countMain++;
                            checkCountMain(countMain, "2 to the right");
                        } else {
                            countMain = 1;
                            break;
                        }
                    }
                } catch (Exception e) {
                    count = 1;
                }
                try { //Check 2 to the left
                    for (int i = 1; i < 3; i++) {
                        if (boardMain[locationMainRow][locationMainColumn - i] == playerTurn) {
                            countMain++;
                            checkCountMain(countMain, "2 to the left");
                        } else {
                            countMain = 1;
                            break;
                        }
                    }
                } catch (Exception e) {
                    count = 1;
                }
                try {
                    if (boardMain[locationMainRow + 2][locationMainColumn] == playerTurn && boardMain[locationMainRow + 1][locationMainColumn] == playerTurn) { //Check downwards
                        countMain += 2;
                        checkCountMain(countMain, "2 downwards");
                    }
                } catch (Exception e) {
                    count = 1;
                }
                try { //Checking 2 upwards
                    if (boardMain[locationMainRow - 1][locationMainColumn] == playerTurn && boardMain[locationMainRow - 2][locationMainColumn] == playerTurn) {
                        countMain += 2;
                        checkCountMain(countMain, "2 upwards");
                    }
                } catch (Exception e) {
                    count = 1;
                }
                try {//Going 1top and 1bottom
                    if (boardMain[locationMainRow - 1][locationMainColumn] == playerTurn && boardMain[locationMainRow + 1][locationMainColumn] == playerTurn) {
                        countMain += 2;
                        checkCountMain(countMain, "1 up, 1 down");
                    }
                } catch (Exception e) {
                    count = 1;
                }
                try {
                    if (boardMain[locationMainRow - 1][locationMainColumn - 1] == playerTurn && boardMain[locationMainRow + 1][locationMainColumn + 1] == playerTurn) { //Going 1top left --> 1bottom right
                        countMain += 2;
                        checkCountMain(countMain, "1 top left 1 bottom right");
                    }
                } catch (Exception e) {
                    count = 1;
                }
                try {
                    if (boardMain[locationMainRow + 1][locationMainColumn + 1] == playerTurn && boardMain[locationMainRow + 2][locationMainColumn + 2] == playerTurn) {  //Going 2bottom right
                        countMain += 2;
                        checkCountMain(countMain, "2 bottom right");
                    }
                } catch (Exception e) {
                    count = 1;
                }
                try {
                    if (boardMain[locationMainRow + 1][locationMainColumn - 1] == playerTurn && boardMain[locationMainRow - 1][locationMainColumn + 1] == playerTurn) { //going 1bottom left --> 1 top right
                        countMain += 2;
                        checkCountMain(countMain, "1 botto left 1 top right");
                    }
                } catch (Exception e) {
                    count = 1;
                }
                try { //Going 2 top left
                    for (int i = 1; i < 3; i++) {
                        if (boardMain[locationMainRow - i][locationMainColumn - i] == playerTurn) {
                            countMain++;
                            checkCountMain(countMain, "2 top left");
                        } else {
                            countMain = 1;
                            break;
                        }
                    }
                } catch (Exception e) {
                    count = 1;
                }
                try { //Going 2 bottom left
                    for (int i = 1; i < 3; i++) {
                        if (boardMain[locationMainRow + i][locationMainColumn - i] == playerTurn) {
                            countMain++;
                            checkCountMain(countMain, "2 bottom left");
                        } else {
                            countMain = 1;
                            break;
                        }
                    }
                } catch (Exception e) {
                    count = 1;
                }
                try { //Going 2 top right
                    if (boardMain[locationMainRow - 1][locationMainColumn + 1] == playerTurn && boardMain[locationMainRow - 2][locationMainColumn + 2] == playerTurn) {
                        countMain += 2;
                        checkCountMain(countMain, "2 top right");
                    }

                } catch (Exception e) {
                    count = 1;
                }
            }
        }
    }

    public void checkCountMain(int countMain, String tempString) {
        if (countMain == 3) {
            gameScreen = false;
            gameOver = true;
            winMethod = tempString;
            winner = playerTurn;
        }
    }

    public void restart() {
        firstTurn = true;
        gameScreen = true;
        playerTurn = 1;
        tie = false;
        turn = 1;
        gameOver = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int m = 0; m < 3; m++) {
                        boardSub[i][j][k][m] = 0;
                    }
                }
                boardMain[i][j] = 0;
            }
        }
        locationMainRow = 1;
        locationSubRow = 1;
        locationMainColumn = 1;
        locationSubColumn = 1;
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(69);
        }
        if (gameScreen) {
            if (firstTurn) {
                if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (locationSubColumn != 0) {
                        locationSubColumn--;
                    } else if (locationMainColumn != 0) {
                        locationMainColumn--;
                        locationSubColumn = 2;
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (locationSubColumn != 2) {
                        locationSubColumn++;
                    } else if (locationMainColumn != 2) {
                        locationMainColumn++;
                        locationSubColumn = 0;
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (locationSubRow != 2) {
                        locationSubRow++;
                    } else if (locationMainRow != 2) {
                        locationMainRow++;
                        locationSubRow = 0;
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_UP) {
                    if (locationSubRow != 0) {
                        locationSubRow--;
                    } else if (locationMainRow != 0) {
                        locationMainRow--;
                        locationSubRow = 2;
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (boardSub[locationMainRow][locationMainColumn][locationSubRow][locationSubColumn] == 0) {
                        endTurn();
                        firstTurn = false;
                    }

                }
            } else {
                if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (locationSubColumn != 0) {
                        locationSubColumn--;
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (locationSubColumn != 2) {
                        locationSubColumn++;
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (locationSubRow != 2) {
                        locationSubRow++;
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_UP) {
                    if (locationSubRow != 0) {
                        locationSubRow--;
                    }
                }
                if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (boardSub[locationMainRow][locationMainColumn][locationSubRow][locationSubColumn] == 0) {
                        endTurn();
                    }
                }
            }
        }
        if (gameOver || tie) {
            if (ke.getKeyCode() == KeyEvent.VK_R) {
                restart();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }

    @Override
    public void mouseClicked(MouseEvent me) {

    }

    @Override
    public void mousePressed(MouseEvent me) {
        if (me.getX() >= width / 2 - 100 && me.getX() <= width / 2 + 100 && me.getY() >= height / 2 - 100 && me.getY() <= height / 2 + 100) {
            //when the mouse clicks the center, execute this code
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {

    }

    @Override
    public void mouseEntered(MouseEvent me) {

    }

    @Override
    public void mouseExited(MouseEvent me) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent me) {
        //runs whenever the mouse is moved across the screen, same methods as mouseClicked
    }

}
