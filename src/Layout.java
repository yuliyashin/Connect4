import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Layout extends JFrame {
	
	final static int ROW = 6;
	final static int COL = 7;
	final static int FRAME_WIDTH = 600;
	final static int FRAME_HEIGHT =	700;

	private JPanel[][] grid = new JPanel[ROW][COL];

	private ImageIcon yellow = new ImageIcon("");
	JPanel gridPanel = new JPanel(new GridLayout(ROW, COL));
	
	public Layout() {
		super("Connect 4");
		//		setLayout(new GridLayout());
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		JLabel label = new JLabel("hello", yellow, JLabel.CENTER);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add( label, BorderLayout.CENTER );
//		JPanel panel = new JPanel();
//		JLabel label = new JLabel();
//		panel.add(label);
//		add(panel);
//		fillGrid();
//		setLayout(new BorderLayout());
		
		

		
//		c.setLayout(new GridLayout(ROW, COL));


		//		container = getContentPane();
		//		container.add();


	}

	public void fillGrid() {
		for(int j = 0; j < ROW; j++) {
			for (int k = 0; k < COL; k++) {
				grid[j][k] = new JPanel();
				grid[j][k].add(new JLabel(yellow));
				gridPanel.add(grid[j][k], BorderLayout.CENTER);
			}
		}
	}
}