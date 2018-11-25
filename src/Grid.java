import java.util.Arrays;

public class Grid {

	final static int COL = 7;
	final static int ROW = 6;

	private char[][] grid;

	public void initializeGrid() {
		grid = new char[ROW][COL];

		for (char[] row : grid) {
			Arrays.fill(row, '\0');
		}
	}

	public void printGrid() {
		for (char[] row : grid) {
			for (char ch : row) {
				System.out.print(ch);	// will not print anything if null
			}
			System.out.println();
		}
	}
}
