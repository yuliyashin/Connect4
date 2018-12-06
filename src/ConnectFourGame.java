import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class ConnectFourGame extends JFrame{
	//dimensions
	private final int ROW = 6;
	private final int COL = 7;
	private final int FRAME_WIDTH = 960;
	private final int FRAME_HEIGHT = 760;
	private final int BLOCK_DIMENSIONS = 130; // image size (height and width)

	private final int aiMode = 0;
	private final int playerMode = 1;
	private int gameMode;

	// score variables
	private static int P1Score = 0;
	private static int P2Score = 0;
	private static int AIScore = 0;

	private JLabel[][] grid = new JLabel[ROW][COL];
	private JLabel[] dropArray = new JLabel[COL];
	private JLabel player1;
	private JLabel player2;
	private JLabel playerAI;
	private JLabel player1Score;
	private JLabel player2Score;
	private JLabel playerAIScore;

	private JPanel dropPanel;
	private JPanel gridPanel;
	private JPanel playerPanel;
	private JPanel optionPanel;
	// colors
	private Color yellowColor = Color.YELLOW;
	private Color redColor = Color.RED;
	private Color blueColor = Color.BLUE;
	private Color greenColor = Color.GREEN;

	//font 
	private Font font = new Font("Courier", Font.BOLD, 15);

	// image icons
	private ImageIcon emptyCircle = new ImageIcon(this.getClass().getResource("/emptyCircle.png"));
	private ImageIcon yellowCircle = new ImageIcon(this.getClass().getResource("/happyYellowCircle.png"));
	private ImageIcon redCircle = new ImageIcon(this.getClass().getResource("/madRedCircle.png"));
	private ImageIcon greenCircle = new ImageIcon(this.getClass().getResource("/greenCircle.png"));
	private final ImageIcon PLAYER_ONE = yellowCircle;
	private final ImageIcon PLAYER_TWO = redCircle;
	private final ImageIcon PLAYER_AI = greenCircle; 
	private ImageIcon activePlayer = PLAYER_ONE;

	private JButton reset;
	private Timer t;
	private Border winnerBorder = new LineBorder(Color.BLACK, 5);
	private Border player1Border = new LineBorder(Color.YELLOW, 3);
	private Border player2Border = new LineBorder(Color.RED, 3);
	private Border playerAIBorder = new LineBorder(Color.GREEN, 3);

	ConnectFourGame(){
		setLayout(new BorderLayout());
		addMouseListener(new myMouseListener());

		gameMode = gameModeOption();
		System.out.print(gameMode);
		if (gameMode == aiMode) {
			createPvsAI();
		}
		else {
			createPvsP();
		}

		addComponents();
		setupFrame();
		setVisible(true);
	}

	// asks user which game mode they'd like to play before main game
	private int gameModeOption() {
		String buttonArray[] = { "AI", "Player" };
		int choice = JOptionPane.showOptionDialog(this, "Player versus:", "Select an Option",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttonArray,
				buttonArray[0]);
		return choice;
	}

	private void addComponents() {
		add(gridPanel, BorderLayout.CENTER);
		add(playerPanel, BorderLayout.NORTH);
		add(optionPanel, BorderLayout.EAST);
	}

	private void setupFrame() {
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setLocationRelativeTo(null);
		pack();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	static class Coord{
		int row;
		int col;

		Coord(int r, int c){
			row = r;
			col = c;
		}
	}

	// game functions
	private boolean isActivePlayer(int row_index, int col_index) {
		return grid[row_index][col_index].getIcon() == activePlayer;
	}

	private boolean winnerFound(ArrayList<Coord> list) {
		return (list.size() >= 4);
	}

	private void indicateWinner(ArrayList<Coord> list) {
		for (int i = 0; i < list.size(); i++) {
			int row = list.get(i).row;
			int col = list.get(i).col;
			grid[row][col].setBorder(winnerBorder);
		}
	}

	private boolean checkHorizontal(int row_index, int col_index){
		ArrayList<Coord> coordinates = new ArrayList<>();
		coordinates.add(new Coord(row_index, col_index));

		for (int i = col_index - 1; i >= 0; i--) {

			if (i >= 0 && isActivePlayer(row_index, i)) {
				coordinates.add(new Coord(row_index, i));

			} else {

				break; 
			}

		}

		for (int i = col_index + 1; i < COL; i++) {

			if (i < COL && isActivePlayer(row_index, i)) {  
				coordinates.add(new Coord(row_index, i));

			} else {

				break;
			}

		}

		if (winnerFound(coordinates)) {
			indicateWinner(coordinates);
		}

		return (winnerFound(coordinates));
	}

	private boolean checkVertical(int row_index, int col_index){
		ArrayList<Coord> coordinates = new ArrayList<>();

		coordinates.add(new Coord(row_index, col_index));

		for (int i = row_index - 1; i >= 0; i--) {
			if (i >= 0 && isActivePlayer(i, col_index)) {
				coordinates.add(new Coord(i, col_index));
			}
			else {
				break; 
			}
		}

		for (int i = row_index + 1; i < ROW; i++) {
			if (i < ROW && isActivePlayer(i, col_index)) {
				coordinates.add(new Coord(i, col_index));
			} else {
				break;
			}			
		}

		if (winnerFound(coordinates)) {
			indicateWinner(coordinates);
		}

		return (winnerFound(coordinates));
	}

	private boolean checkBottomLeftToTopRight(int row_index, int col_index){
		ArrayList<Coord> coordinates = new ArrayList<>();
		coordinates.add(new Coord(row_index, col_index));

		for (int j = 1; j < 4; j++) {

			boolean withinGrid = (row_index+j < ROW) && (col_index-j >= 0);

			if (withinGrid && isActivePlayer(row_index+j, col_index-j)) {	//southwest
				coordinates.add(new Coord(row_index+j, col_index-j));

			} else
				break;

		}

		for (int j = 1; j < 4; j++) {

			boolean withinGrid = (row_index-j >= 0) && (col_index+j < COL);

			if (withinGrid && isActivePlayer(row_index-j, col_index+j)) {	//northeast
				coordinates.add(new Coord(row_index-j, col_index+j));

			} else
				break;

		}

		if (winnerFound(coordinates)) {
			indicateWinner(coordinates);
		}

		return (winnerFound(coordinates));
	}

	private boolean checkTopLeftToBottomRight(int row_index, int col_index) {
		ArrayList<Coord> coordinates = new ArrayList<>();
		coordinates.add(new Coord(row_index, col_index));

		for (int j = 1; j < ROW; j++) {

			boolean withinGrid = (row_index-j >= 0) && (col_index-j >= 0);

			if (withinGrid && isActivePlayer(row_index - j, col_index - j)) { // northwest
				coordinates.add(new Coord(row_index - j, col_index - j));

			} else 
				break;

		}

		for (int j = 1; j < 4; j++) {
			boolean withinGrid = (row_index + j < ROW) && (col_index + j < COL);

			if (withinGrid && isActivePlayer(row_index+j, col_index + j)) {		//southeast
				coordinates.add(new Coord(row_index + j, col_index + j));

			} else 
				break;
		}

		if (winnerFound(coordinates)) {
			indicateWinner(coordinates);
		}

		return (winnerFound(coordinates));
	}

	private boolean checkIfWon(int row, int col) {
		return (checkHorizontal(row, col) || 
				checkVertical(row, col) ||
				checkBottomLeftToTopRight(row, col) ||
				checkTopLeftToBottomRight(row, col));
	}

	private boolean checkIfDraw() {
		for (int j = 0; j < ROW; j++) {

			for (int k = 0; k < COL; k++) {

				if (grid[j][k].getIcon() == emptyCircle) {
					return false;
				}
			}
		}
		return true;
	}

	private void resetGame() {
		resetScoreBoard();
		resetBoard();
	}

	private void resetScoreBoard() {
		P1Score = 0;
		P2Score = 0;
	}

	private void resetBoard() {
		clearGrid();
	}

	//	 sets up game for Player versus AI
	void createPvsAI() {
		System.out.print("Player VS AI");
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(ROW,COL));
		fillGrid();

		playerPanel = new JPanel();
		playerPanel.setLayout(new GridLayout(1,4));

		JLabel currentlyPlaying = new JLabel("  Currently Playing:");
		currentlyPlaying.setFont(font);

		player1 = new JLabel("Player 1");
		player1.setFont(font);
		player1.setBorder(player1Border);
		playerAI = new JLabel("Player AI  ");
		playerAI.setFont(font);

		player1Score = new JLabel(String.valueOf(P1Score));
		playerAIScore = new JLabel(String.valueOf(AIScore));

		playerPanel.add(currentlyPlaying);
		playerPanel.add(player1);
		playerPanel.add(playerAI);

		optionPanel = new JPanel();
		optionPanel.setLayout(new GridLayout(4,1));

		reset = new JButton("New Game");
		reset.setFont(font);
		reset.addActionListener(new ButtonHandler());

		optionPanel.add(reset);
	}

	//	sets up game for Player versus Player
	void createPvsP() {
		System.out.print("Player VS Player!");
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(ROW,COL));
		fillGrid();

		playerPanel = new JPanel();
		playerPanel.setLayout(new GridLayout(1,4));

		JLabel currentlyPlaying = new JLabel("  Currently Playing:");
		currentlyPlaying.setFont(font);

		player1 = new JLabel("Player 1");
		player1.setFont(font);
		player1.setBorder(player1Border);
		player2 = new JLabel("Player 2  ");
		player2.setFont(font);

		player1Score = new JLabel(String.valueOf(P1Score));
		player2Score = new JLabel(String.valueOf(P2Score));

		playerPanel.add(currentlyPlaying);
		playerPanel.add(player1);
		playerPanel.add(player2);

		optionPanel = new JPanel();
		optionPanel.setLayout(new GridLayout(4,1));

		reset = new JButton("New Game");
		reset.setFont(font);
		reset.addActionListener(new ButtonHandler());

		optionPanel.add(reset);
	}

	private void fillGrid() {
		for (int j = 0; j < ROW; j++) {
			for (int k = 0; k < COL; k++) {
				grid[j][k] = new JLabel();
				grid[j][k].setIcon(emptyCircle);
				grid[j][k].setOpaque(true);
				gridPanel.add(grid[j][k]);
			}
		}
	}
	private void clearGrid() {
		for (int j = 0; j < ROW; j++) {

			for (int k = 0; k < COL; k++) {
				grid[j][k].setIcon(emptyCircle);
				grid[j][k].setBorder(null);
			}
		}
	}

	class myMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();	// jframe mouse coordinates
			int col = x / BLOCK_DIMENSIONS;	//select a column in proportion to jframe's mouse click coordinates

			if (gameMode == playerMode) {
				for (int i = (ROW - 1); i >= 0; i--) {	// checks for empty space in selected column

					JLabel checkedSpot = grid[i][col];
					if (checkedSpot.getIcon() == emptyCircle){

						if (activePlayer == PLAYER_ONE ) {
							checkedSpot.setIcon(yellowCircle);
							boolean playerWon = checkIfWon(i, col);

							if (playerWon) {
								P1Score++;
								player1Score.setText(String.valueOf(P1Score));

								int option = JOptionPane.showConfirmDialog(null, "Player 1 Wins! Would you like to play again?", "* We have a winner! *", JOptionPane.YES_NO_OPTION);
								if (option == JOptionPane.YES_OPTION) {
									resetBoard();
								}
							}

							boolean itsADraw = checkIfDraw();

							if (itsADraw){
								int option = JOptionPane.showConfirmDialog(null, "Nobody Wins! Would you like to play again?", "* It's a draw! *", JOptionPane.YES_NO_OPTION);

								if (option == JOptionPane.YES_OPTION) {
									resetBoard();
								}
							}
							//						player1.setFont(~Font.BOLD));;
							player1.setBorder(null);
							player2.setBorder(player2Border);
							activePlayer = PLAYER_TWO;

							return;

						} else {
							checkedSpot.setIcon(redCircle);

							boolean playerWon = checkIfWon(i, col);

							if (playerWon) {
								P2Score++;
								System.out.print("player2Score: " + P2Score);
								player2Score.setText(String.valueOf(P2Score));

								int option = JOptionPane.showConfirmDialog(null, "Player 2 Wins! Would you like to play again?", "* We have a winner *", JOptionPane.YES_NO_OPTION);
								if (option == JOptionPane.YES_OPTION) {
									resetBoard();
								}
							}

							boolean itsADraw = checkIfDraw();

							if (itsADraw){
								int option = JOptionPane.showConfirmDialog(null, "Nobody Wins! Would you like to play again?", "* It's a draw! *", JOptionPane.YES_NO_OPTION);

								if (option == JOptionPane.YES_OPTION) {
									resetBoard();
								}
							}

							activePlayer = PLAYER_ONE;
							player2.setBorder(null);
							player1.setBorder(player1Border);
							return;
						}
					}
				}
			}

			if (gameMode == aiMode) {
				for (int i = (ROW - 1); i >= 0; i--) {	// checks for empty space in selected column
					JLabel checkedSpot = grid[i][col];
					if (checkedSpot.getIcon() == emptyCircle){

						if (activePlayer == PLAYER_ONE ) {
							checkedSpot.setIcon(yellowCircle);
							boolean playerWon = checkIfWon(i, col);

							if (playerWon) {
								P1Score++;
								player1Score.setText(String.valueOf(P1Score));

								int option = JOptionPane.showConfirmDialog(null, "Player 1 Wins! Would you like to play again?", "* We have a winner! *", JOptionPane.YES_NO_OPTION);
								if (option == JOptionPane.YES_OPTION) {
									resetBoard();
								}
							}

							boolean itsADraw = checkIfDraw();

							if (itsADraw){
								int option = JOptionPane.showConfirmDialog(null, "Nobody Wins! Would you like to play again?", "* It's a draw! *", JOptionPane.YES_NO_OPTION);

								if (option == JOptionPane.YES_OPTION) {
									resetBoard();
								}
							}
							//						player1.setFont(~Font.BOLD));;
							player1.setBorder(null);
							playerAI.setBorder(playerAIBorder);
							activePlayer = PLAYER_AI;

							

							if (activePlayer == PLAYER_AI) {
								int randomTime = ((int)(Math.random() * (1000)));
								new java.util.Timer().schedule( 
								        new java.util.TimerTask() {
								            @Override
								            public void run() {
								                aiMove();
								            }
								        }, 
								        randomTime 
								);
						 
//								playerWon = checkIfWon(i, col);
//								itsADraw = checkIfDraw();
//								
//								if (playerWon) {
//									AIScore++;
//									playerAI.setText(String.valueOf(AIScore));
//
//									int option = JOptionPane.showConfirmDialog(null, "Player 1 Wins! Would you like to play again?", "* We have a winner! *", JOptionPane.YES_NO_OPTION);
//									if (option == JOptionPane.YES_OPTION) {
//										resetBoard();
//									}
//								}
//
//								if (itsADraw){
//									int option = JOptionPane.showConfirmDialog(null, "Nobody Wins! Would you like to play again?", "* It's a draw! *", JOptionPane.YES_NO_OPTION);
//
//									if (option == JOptionPane.YES_OPTION) {
//										resetBoard();
//									}
//								}
							}

							return;

						}
						//					}
					}
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}

	private void aiMove() {
		ArrayList<Coord> possibleAIMoves = new ArrayList<>();
		int index = -1;
		for (int j = 0; j < COL; j++) {			// k == ROW; j == COL;
			System.out.println("(COL, ROW)");
			System.out.print("(" + j);
			for (int k = (ROW - 1); k >= 0; k--) {
				System.out.print("," + k + ")");
				System.out.println();
				JLabel checkedSpot = grid[k][j];
				if (checkedSpot.getIcon() == emptyCircle) {
//					System.out.print("empty!!" + ("Ro")"
					possibleAIMoves.add(new Coord(k,j)); 
					index++;
					break;
				}
			}
		}
		int randomIndex = ((int)(Math.random() * (index+1)));
		int row = possibleAIMoves.get(randomIndex).row;
		int col = possibleAIMoves.get(randomIndex).col;
		System.out.print("Row: " + row + "Col: " + col + " ");
		grid[row][col].setIcon(PLAYER_AI);
		boolean playerWon = checkIfWon(row, col);
		boolean itsADraw = checkIfDraw();
		
		if (playerWon) {
			AIScore++;
//			playerAI.setText(String.valueOf(AIScore));
			int option = JOptionPane.showConfirmDialog(null, "AI Wins! Would you like to play again?", "* We have a winner! *", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				resetBoard();
			}
		}

		if (itsADraw){
			int option = JOptionPane.showConfirmDialog(null, "Nobody Wins! Would you like to play again?", "* It's a draw! *", JOptionPane.YES_NO_OPTION);

			if (option == JOptionPane.YES_OPTION) {
				resetBoard();
			}
		}
		
		playerAI.setBorder(null);
		player1.setBorder(player1Border);
		activePlayer = PLAYER_ONE;
	}

	class ButtonHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == reset) {

				int confirmed = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to reset the game?", "Reset Game Message Box",
						JOptionPane.YES_NO_OPTION);

				if (confirmed == JOptionPane.YES_OPTION) {
					resetGame();
				}
			}
		}
	}
}
