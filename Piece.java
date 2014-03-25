public class Piece
{
	byte[][] grid;
	byte kind;

	byte x, y;

	public Piece() // random piece
	{
		kind = (byte)(Math.random() * 7);
		x = 3;
		y = 2;

		draw();
	}
	public Piece (byte type) // given piece type
	{
		kind = type;
		x = 3;
		y = 2;

		draw();
	}

	public Piece (Piece p) // duplicate piece
	{
		grid = p.grid;
		kind = p.kind;
		x = p.x;
		y = p.y;
	}

	private void draw()
	{
		switch(kind)
		{
			case 1: // Z - red
				grid = new byte[][] {{1,1,0},
									 {0,1,1},
									 {0,0,0}};
				break;
			case 2: // S - green
				grid = new byte[][] {{0,2,2},
									 {2,2,0},
									 {0,0,0}};
				break;
			case 3: // J - blue
				grid = new byte[][] {{3,0,0},
									 {3,3,3},
									 {0,0,0}};
				break;
			case 4: // I - cyan
				grid = new byte[][] {{0,0,0,0},
									 {4,4,4,4},
									 {0,0,0,0},
									 {0,0,0,0}};
				y--;
				break;
			case 5: // T - magenta
				grid = new byte[][] {{0,5,0},
									 {5,5,5},
									 {0,0,0}};
				break;
			case 6: // O - yellow
				grid = new byte[][] {{0,6,6,0},
									 {0,6,6,0},
									 {0,0,0,0}};
				break;
			case 0: // L - orange
				grid = new byte[][] {{0,0,7},
									 {7,7,7},
									 {0,0,0}};
				break;
		}
	}

	public void rotateCW ()
	{
		verticalFlip();
		diagonalFlip();
	}
	private void verticalFlip()
	{
		byte[][] temp = new byte[grid.length][grid[0].length];

		for (byte i = 0; i < grid.length; i++)
		{
			for (byte j = 0; j < grid[0].length; j++)
			{
				temp[i][j] = grid[grid.length-1-i][j];
			}
		}

		grid = temp;
	}

	private void diagonalFlip()
	{
		byte[][] temp = new byte[grid[0].length][grid.length];

		for (byte i = 0; i < temp.length; i++)
		{
			for (byte j = 0; j < temp[0].length; j++)
			{
				temp[i][j] = grid[j][i];
			}
		}

		grid = temp;
	}
/*	public void rotateCCW ()
	{
		horizontalFlip();
		diagonalFlip();
	}
*/
/*	private void horizontalFlip()
	{
		byte[][] temp = new byte[4][4];

		for (byte i = 0; i < 4; i++)
		{
			for (byte j = 0; j < 4; j++)
			{
				temp[i][j] = grid[i][3-j];
			}
		}

		grid = temp;
	}
*/


	public byte bottom()
	{
		for (byte i = (byte)(grid.length - 1); i >= 0; i--)
		{
			for (byte j = 0; j < grid[0].length; j++)
			{
				if (grid[i][j] != 0) return i;
			}
		}

		return 0;
	}

	public byte left()
	{
		for (byte j = 0; j < grid[0].length; j++)
		{
			for (byte i = 0; i < grid.length; i++)
			{
				if (grid[i][j] != 0) return j;
			}
		}

		return 0;
	}

	public byte right()
	{
		for (byte j = (byte)(grid[0].length - 1); j >= 0; j--)
		{
			for (byte i = 0; i < grid.length; i++)
			{
				if (grid[i][j] != 0) return j;
			}
		}

		return 0;
	}


	public static void main(String[] args)
	{
		Program prog = new Program();
	}
}