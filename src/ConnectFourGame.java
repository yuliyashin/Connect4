import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
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
import javax.swing.border.LineBorder;

public class ConnectFourGame extends JFrame{
	static final int ROW = 6;
	static final int COL = 7;
	static final int FRAME_WIDTH = 840;
	static final int FRAME_HEIGHT =	750;
	static final int BLOCK_DIMENSIONS = 120; // image size (height and width)
	private int P1Score = 0;
	private int P2Score = 0;

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
	
	private boolean checkVertical(int row, int col){
		return false;
	}
	
	private boolean checkDiagonally(int row, int col){
		return false;
	}
	
	private boolean checkIfWon(int row, int col) {
		return (checkHorizontal(row, col) || 
				checkVertical(row, col) ||
				checkDiagonally(row, col));
		
	}

	private void createComponents() {
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(ROW,COL));
		gridPanel.setBorder(new LineBorder(Color.BLACK));
		fillGrid();


		scorePanel = new JPanel();
		scorePanel.setLayout(new GridLayout(1,4));
		JLabel label1 = new JLabel("Player 1");
		JLabel label2 = new JLabel("Player 2");
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
//				grid[j][k].addMouseListener();

				//				grid[j][k] = new JPanel();
				//				ImageIcon scaledYellowCircle = yellowCircle.getScaledInstance(grid[j][k].getWidth(), grid[j][k].getHeight(), Image.SCALE_SMOOTH);
				//				grid[j][k].add(new JLabel(scaledYellowCircle));
				gridPanel.add(grid[j][k]);
			}

		}
	}

	class ButtonHandler implements MouseListener {

		//		@Override
		//		public void actionPerformed(ActionEvent e) {
		//			for (int j = 0; j < ROW; j++) {
		//				for (int k = 0; k < COL; k++) {
		//					if (e.getSource() == grid[j][k]) {
		//						//						grid[j][k] = new JButton(new ImageIcon("/Users/julie/Desktop/yellowCircle.jpg"))
		//						if (activePlayer == PLAYER_ONE) {
		//							grid[j][k].setIcon(yellowCircle);
		//							grid[j][k].setEnabled(true);
		//							activePlayer = PLAYER_TWO;							
		//						} else {
		//							grid[j][k].setIcon(redCircle);
		//							grid[j][k].setEnabled(true);
		//							activePlayer = PLAYER_ONE;
		//						}
		//					}
		//				}
		//			}
		//		}

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
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}


	private void wireComponents() {

		ButtonHandler buttonHandler = new ButtonHandler();



	}
}

