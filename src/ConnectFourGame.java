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
	private static final int ROW = 6;
	private static final int COL = 7;
	private static final int FRAME_WIDTH = 960;
	private static final int FRAME_HEIGHT =	770;
	private static final int BLOCK_DIMENSIONS = 120; // image size (height and width)

	// score variables
	private static int P1Score = 0;
	private static int P2Score = 0;

	private JLabel[][] grid = new JLabel[ROW][COL];
	private JLabel[] dropArray = new JLabel[COL];

	private JPanel dropPanel;
	private JPanel gridPanel;
	private JPanel scorePanel;
	private JPanel optionPanel;
	// colors
	private Color yellowColor = Color.YELLOW;
	private Color redColor = Color.RED;
	private Color blueColor = Color.BLUE;

	private Font font = new Font("Georgia", Font.ITALIC, 30);

	// image icons
	private ImageIcon emptyCircle = new ImageIcon(this.getClass().getResource("/emptyCircle.png"));
	private ImageIcon yellowCircle = new ImageIcon(this.getClass().getResource("/happyYellowCircle.png"));
	private ImageIcon redCircle = new ImageIcon(this.getClass().getResource("/madRedCircle.png"));
	private final ImageIcon PLAYER_ONE = yellowCircle;
	private final ImageIcon PLAYER_TWO = redCircle;
	private ImageIcon activePlayer = PLAYER_ONE;

	private JButton reset;

	private Border winnerBorder = new LineBorder(Color.BLACK, 5);

	ConnectFourGame(){
		super("Connect 4!");
		setLayout(new BorderLayout());
		createComponents();

		addMouseListener(new myMouseListener());
		add(gridPanel, BorderLayout.CENTER);
		add(scorePanel, BorderLayout.NORTH);
		add(optionPanel, BorderLayout.EAST);

		//		wireComponenets();
		setupFrame();
	}

	private void setupFrame() {
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setLocationRelativeTo(null);
		pack();
		setVisible(true);
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

	//CHECK FOR WINNER
	private boolean checkHorizontal(int row_index, int col_index){
		ArrayList<Coord> coordinates = new ArrayList<>();
		coordinates.add(new Coord(row_index, col_index));
		//		int count = 1;
		for (int i = col_index - 1; i >= 0; i--) {
			if (i >= 0 && isActivePlayer(row_index, i)) { /*grid[row_index][i].getIcon() == activePlayer) {*/
				coordinates.add(new Coord(row_index, i));
				//				count++;
			} else {
				break; 
			}
		}

		for (int i = col_index + 1; i < COL; i++) {
			if (i < COL && isActivePlayer(row_index, i)) { /*grid[row_index][i].getIcon() == activePlayer)*/ 
				coordinates.add(new Coord(row_index, i));
				//				count++;
			} else {
				break;
			}			
		}
		//		System.out.print("SIZE: " + coordinates.size()); //debug
		if (winnerFound(coordinates)) {
			indicateWinner(coordinates);
		}

		return (winnerFound(coordinates));
		//		return (count >= 4);
	}

	private boolean checkVertical(int row_index, int col_index){
		ArrayList<Coord> coordinates = new ArrayList<>();
		coordinates.add(new Coord(row_index, col_index));
		//		int count = 1;
		for (int i = row_index - 1; i >= 0; i--) {
			if (i >= 0 && isActivePlayer(i, col_index)) {
				coordinates.add(new Coord(i, col_index));
			}
			//				count++;
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
		//		return (count >= 4);
	}

	private boolean checkBottomLeftToTopRight(int row_index, int col_index){
		ArrayList<Coord> coordinates = new ArrayList<>();
		coordinates.add(new Coord(row_index, col_index));
		for (int j = 1; j < 4; j++) {
			boolean withinGrid = (row_index+j < ROW) && (col_index-j >= 0);
			if (withinGrid && isActivePlayer(row_index+j, col_index-j)) {	//southwest
				coordinates.add(new Coord(row_index+j, col_index-j));
				//				count++;

			} else
				break;
		}

		for (int j = 1; j < 4; j++) {
			boolean withinGrid = (row_index-j >= 0) && (col_index+j < COL);
			if (withinGrid && isActivePlayer(row_index-j, col_index+j)) {	//northeast
				coordinates.add(new Coord(row_index-j, col_index+j));
				//				count++;
			} else
				break;
		}

		if (winnerFound(coordinates)) {
			indicateWinner(coordinates);
		}

		return (winnerFound(coordinates));
		//		return (count >= 4);
	}



	private boolean checkTopLeftToBottomRight(int row_index, int col_index) {
		ArrayList<Coord> coordinates = new ArrayList<>();
		coordinates.add(new Coord(row_index, col_index));
		//		int count = 1;

		for (int j = 1; j < ROW; j++) {
			boolean withinGrid = (row_index-j >= 0) && (col_index-j >= 0);
			if (withinGrid && isActivePlayer(row_index - j, col_index - j)) { // northwest
				coordinates.add(new Coord(row_index - j, col_index - j));
				//				count++;
			} else break;
		}

		for (int j = 1; j < 4; j++) {
			boolean withinGrid = (row_index + j < ROW) && (col_index + j < COL);
			if (withinGrid && isActivePlayer(row_index+j, col_index + j)) {		//southeast
				coordinates.add(new Coord(row_index + j, col_index + j));
				//				count++;
			} else break;
		}

		if (winnerFound(coordinates)) {
			indicateWinner(coordinates);
		}

		return (winnerFound(coordinates));
		//		return (count >= 4);
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

	private void createComponents() {
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(ROW,COL));
		gridPanel.setBorder(new LineBorder(Color.YELLOW));
		fillGrid();

		//		Border border = BorderFactory.createLineBorder(Color.GRAY, 3); border

		scorePanel = new JPanel();
		scorePanel.setLayout(new GridLayout(1,4));
		JLabel player1 = new JLabel("Player 1");
		player1.setFont(font);
		//		player1.setBorder(border);
		JLabel player2 = new JLabel("Player 2");
		player2.setFont(font);
		JLabel player1Score = new JLabel(String.valueOf(P1Score));
		JLabel player2Score = new JLabel(String.valueOf(P2Score));
		player1.setForeground(yellowColor);
		player2.setForeground(redColor);
		scorePanel.add(player1);
		scorePanel.add(player1Score);
		scorePanel.add(player2);
		scorePanel.add(player2Score);

		optionPanel = new JPanel();
		optionPanel.setLayout(new GridLayout(4,1));
		reset = new JButton("New Game");
		reset.addActionListener(new ButtonHandler());
		optionPanel.add(reset);
	}

	private void fillGrid() {
		for (int j = 0; j < ROW; j++) {
			for (int k = 0; k < COL; k++) {
				//				gridPanel.
				grid[j][k] = new JLabel();
				//				grid[j][k].addMouseListener(new highlighterListener());
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
			int x = e.getX();
			int y = e.getY();
			int col = x / BLOCK_DIMENSIONS;
			for (int i = (ROW - 1); i >= 0; i--) {
				JLabel checkedSpot = grid[i][col];
				boolean itsADraw = checkIfDraw();
				if (itsADraw){
					int option = JOptionPane.showConfirmDialog(null, "Nobody Wins! Would you like to play again?", "* It's a draw! *", JOptionPane.YES_NO_OPTION);
					if (option == JOptionPane.YES_OPTION) {
						resetBoard();
					}
				}
				if (checkedSpot.getIcon() == emptyCircle){
					
					if (activePlayer == PLAYER_ONE ) {
						
						checkedSpot.setIcon(yellowCircle);
						boolean playerWon = checkIfWon(i, col);
						if (playerWon) {
							P1Score++;
							int option = JOptionPane.showConfirmDialog(null, "Player 1 Wins! Would you like to play again?", "* We have a winner! *", JOptionPane.YES_NO_OPTION);
							if (option == JOptionPane.YES_OPTION) {
								resetBoard();
							}
						}
						activePlayer = PLAYER_TWO;
						return;
					} else {
						
						checkedSpot.setIcon(redCircle);
						boolean playerWon = checkIfWon(i, col);
						if (playerWon) {
							P2Score++;
							int option = JOptionPane.showConfirmDialog(null, "Player 2 Wins! Would you like to play again?", "* We have a winner *", JOptionPane.YES_NO_OPTION);
							if (option == JOptionPane.YES_OPTION) {
								resetBoard();
							}
						}
						activePlayer = PLAYER_ONE;
						return;
					}
				}
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e) {


		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			//						int x = e.getX();
			//						int y = e.getY();
			//						int col = x / BLOCK_DIMENSIONS;
			//						
			//						for (int i = 0; i < ROW; i++) {
			//							Border border = BorderFactory.createLineBorder(Color.CYAN, 1);
			//							grid[i][col].setBorder(border);
			//						}

		}

		@Override
		public void mouseExited(MouseEvent e) {
			//						int x = e.getX();
			//						int y = e.getY();
			//						int col = x / BLOCK_DIMENSIONS;
			//						
			//						for (int i = 0; i < ROW; i++) {
			//							grid[i][col - 1].setBorder(null);
			//						}
		}
	}

	class ButtonHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.print("i'm clicked!");
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

//	class highlighterListener extends MouseAdapter {

//		@Override
//		public void mouseEntered(MouseEvent e) {
//			int x = e.getX();
//			int y = e.getY();
//			System.out.print(x + " : " + y);
//			
//		}
//
//		@Override
//		public void mouseExited(MouseEvent e) {
//			// TODO Auto-generated method stub
//			
//		}

//	}