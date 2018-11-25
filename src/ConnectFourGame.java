import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class ConnectFourGame extends JFrame{
	private static final int ROW = 6;
	private static final int COL = 7;
	private static final int FRAME_WIDTH = 840;
	private static final int FRAME_HEIGHT =	750;
	private static final int BLOCK_DIMENSIONS = 120; // image size (height and width)
	private static final int P1Score = 0;
	private static final int P2Score = 0;

	private JLabel[][] grid = new JLabel[ROW][COL];
	private JLabel[] dropArray = new JLabel[COL];
	private JPanel dropPanel;
	private JPanel gridPanel;
	private JPanel scorePanel;
	private ImageIcon emptyCircle = new ImageIcon(this.getClass().getResource("/emptyCircle.png"));
	private ImageIcon yellowCircle = new ImageIcon(this.getClass().getResource("/happyYellowCircle.png"));
	private ImageIcon redCircle = new ImageIcon(this.getClass().getResource("/madRedCircle.png"));
	private Color yellowColor = Color.YELLOW;
	private Color redColor = Color.RED;
	private Color blueColor = Color.BLUE;

	private Font font = new Font("Georgia", Font.ITALIC, 30);

	private final ImageIcon PLAYER_ONE = yellowCircle;
	private final ImageIcon PLAYER_TWO = redCircle;
	private ImageIcon activePlayer = PLAYER_ONE;

	ConnectFourGame(){
		super("Connect 4!");
		setLayout(new BorderLayout());
		createComponents();
		addMouseListener(new ButtonHandler());
		add(gridPanel, BorderLayout.CENTER);
		add(scorePanel, BorderLayout.NORTH);

		//		wireComponenets();
		setupFrame();
	}

	private void setupFrame() {
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setLocationRelativeTo(null);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private boolean checkHorizontal(int row_index, int col_index){
		int count = 1;
		for (int i = col_index - 1; i >= 0; i--) {
			if (i >= 0 && grid[row_index][i].getIcon() == activePlayer) {
				count++;
			} else {
				break; 
			}
		}

		for (int i = col_index + 1; i < COL; i++) {
			if (i < COL && grid[row_index][i].getIcon() == activePlayer) {
				count++;
			} else {
				break;
			}			
		}

		return (count >= 4);
	}

	private boolean checkVertical(int row_index, int col_index){
		int count = 1;
		for (int i = row_index - 1; i >= 0; i--) {
			if (i >= 0 && isActivePlayer(i, col_index)) {
				count++;
			} else {
				break; 
			}
		}

		for (int i = row_index + 1; i < ROW; i++) {
			if (i < ROW && isActivePlayer(i, col_index)) {
				count++;
			} else {
				break;
			}			
		}

		return (count >= 4);
	}

	private boolean checkBottomLeftToTopRight(int row_index, int col_index){
		int count = 1;
		for (int j = 1; j < 4; j++) {
			boolean withinGrid = (row_index+j < ROW) && (col_index-j >= 0);
			if (withinGrid && isActivePlayer(row_index+j, col_index-j)) {	//southwest
				count++;
				System.out.println("For row: " + row_index + " col_index:  " + col_index + " | count: "  + count);
			} else
				break;
		}

		for (int j = 1; j < 4; j++) {
			boolean withinGrid = (row_index-j >= 0) && (col_index+j < COL);
			if (withinGrid && isActivePlayer(row_index-j, col_index+j)) {	//northeast
				count++;
				System.out.print("here" + count);
			} else
				break;
		}
//		System.out.printFor row: " + row_index + " col_index:  " + col_index + " | count2: "  + count2);
		
		return count >= 4;
	}
	
	private boolean isActivePlayer(int row_index, int col_index) {
		return grid[row_index][col_index].getIcon() == activePlayer;
	}
	
	private boolean checkTopLeftToBottomRight(int row_index, int col_index) {

		int count = 1;
		for (int j = 1; j < ROW; j++) {
			boolean withinGrid = (row_index-j >= 0) && (col_index-j >= 0);
			if (withinGrid && isActivePlayer(row_index - j, col_index - j)) { // northwest
				count++;
			} else 
				break;
		}


		//		for (int j = 0; j < 4; j++) {
		//			if (grid[row_index-j][col_index+j].getIcon() == activePlayer) {	
		//				count++;
		//			}
		//		}

		for (int j = 1; j < 4; j++) {
			boolean withinGrid = (row_index+j < ROW) && (col_index+j < COL);
			if (withinGrid && isActivePlayer(row_index+j, col_index+j)) {		//southeast
				count++;
			} else
				break;
		}
//		System.out.print("For row: " + row_index + " col_index:  " + col_index + " | count2: "  + count2);
		
		return count >= 4;
	}

	private boolean checkIfWon(int row, int col) {
		return (checkHorizontal(row, col) || 
				checkVertical(row, col) ||
				checkBottomLeftToTopRight(row, col) ||
				checkTopLeftToBottomRight(row, col));
	}

	private void createComponents() {
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(ROW,COL));
		gridPanel.setBorder(new LineBorder(Color.BLACK));
		fillGrid();


		scorePanel = new JPanel();
		scorePanel.setLayout(new GridLayout(1,4));
		JLabel label1 = new JLabel("Player 1");
		label1.setFont(font);
		JLabel label2 = new JLabel("Player 2");
		label2.setFont(font);
		JLabel label3 = new JLabel(String.valueOf(P1Score));
		JLabel label4 = new JLabel(String.valueOf(P2Score));
		label1.setForeground(redColor);
		label2.setForeground(yellowColor);
		scorePanel.add(label1);
		scorePanel.add(label3);
		scorePanel.add(label2);
		scorePanel.add(label4);




		//		label = new JLabel("hello");
		//		JLabel label1 = new JLabel("Testing");
		//		JLabel label2 = new JLabel("123");
		//
		//		gridPanel.add(label1);
		//		gridPanel.add(label2);
		//		gridPanel.add(label);

	}

	private void fillGrid() {
		for (int j = 0; j < ROW; j++) {
			for (int k = 0; k < COL; k++) {
				grid[j][k] = new JLabel();

				grid[j][k].setIcon(emptyCircle);
				grid[j][k].setOpaque(true);
				//				grid[j][k].addMouseListener(new HighlightHandler());

				//				grid[j][k] = new JPanel();
				//				ImageIcon scaledYellowCircle = yellowCircle.getScaledInstance(grid[j][k].getWidth(), grid[j][k].getHeight(), Image.SCALE_SMOOTH);
				//				grid[j][k].add(new JLabel(scaledYellowCircle));
				gridPanel.add(grid[j][k]);
			}

		}
	}

	class ButtonHandler implements MouseListener {


		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			int col = x / BLOCK_DIMENSIONS;

			for (int i = (ROW - 1); i >= 0; i--) {
				JLabel checkedSpot = grid[i][col];
				if (checkedSpot.getIcon() == emptyCircle) {
					boolean playerWon = checkIfWon(i, col);
					System.out.print(playerWon);
					if (activePlayer == PLAYER_ONE ) {
						checkedSpot.setIcon(yellowCircle);
						activePlayer = PLAYER_TWO;							
					} else {
						checkedSpot.setIcon(redCircle);
						activePlayer = PLAYER_ONE;
					}
					return;
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
			//			int x = e.getX();
			//			int y = e.getY();
			//			int col = x / BLOCK_DIMENSIONS;
			//			
			//			for (int i = 0; i < ROW; i++) {
			//				Border border = BorderFactory.createLineBorder(Color.CYAN, 1);
			//				grid[i][col].setBorder(border);
			//			}

		}

		@Override
		public void mouseExited(MouseEvent e) {
			//			int x = e.getX();
			//			int y = e.getY();
			//			int col = x / BLOCK_DIMENSIONS;
			//			
			//			for (int i = 0; i < ROW; i++) {
			//				grid[i][col - 1].setBorder(null);
			//			}
		}
	}


	private void wireComponents() {

		ButtonHandler buttonHandler = new ButtonHandler();



	}
}

