import java.awt.*;

public class Tetris
{
	private static final byte ROWS = 22;
	private static final byte COLUMNS = 10;
	private static final byte SIZE = 30;

	private static byte[][] board = new byte[ROWS][COLUMNS]; // top, left is 0; rows 0 and 1 are not displayed.
	private static Color[] colours = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE,
									Color.CYAN, Color.MAGENTA, Color.YELLOW, new Color (255,127,0)};

	private Piece piece, check, next, hold, ghost;
	public boolean seeGhost = true;
	public boolean gameOver = false;

	public int lines = 0, pieces = 0;

	boolean pause = false;

	public Tetris()
	{
		for (byte i = 0; i < ROWS; i++)
			for (byte j = 0; j < COLUMNS; j++)
				board[i][j] = 0;
		next = new Piece();
		hold = new Piece();
		piece = new Piece();
	}

	public void update()
	{
		checkEndGame();

		if (!gameOver)
		{
			// checks to see if the current piece has stopped
			check = new Piece(piece);
			check.y++;

			if (checkValid())
			{
				// if so, adds the current piece to the board, then adds the next piece
				addPieceToBoard();
				addNextPiece();
				pieces++;
			}
			else
			{
				// if not, drops by one
				piece.y++;
			}
		}
	}

	public void drop()
	{
		check = new Piece (piece);
		check.y++;

		while(!checkValid())
		{
			check.y++;
			piece.y++;
		}

		update();
	}

	public void move (boolean direction)
	{
		check = new Piece (piece);

		if (direction) // left
		{
			check.x--;
			if (!checkValid())
			{
				piece.x--;
			}
		}
		else // right
		{
			check.x++;
			if (!checkValid())
			{
				piece.x++;
			}
		}
	}

	public void rotate()
	{
		if (piece.kind == 6) return; // O blocks don't rotate

		// turn piece clockwise, if it can be done
		check = new Piece (piece);
		check.rotateCW();

		if (!checkValid())
		{
			piece.rotateCW();
			return;
		}

		else
		{
			kick();
		}
	}

	public void swap()
	{		// swap piece with hold
		check = new Piece(hold);
		check.x = piece.x;
		check.y = piece.y;
		if (!checkValid())
		{
			hold = new Piece(piece);
			piece = new Piece(check);
		}
		else
		{
			kick();
		}
	}

	private void addNextPiece ()
	{
		// creates a new piece at the top-middle
		check = new Piece(next);
		next = new Piece();

		if (!checkValid()) piece = new Piece(check);
		else gameOver = true;
	}

	private void addPieceToBoard()
	{
		for (byte i = 0; i < piece.grid.length; i++)
		{
			for (byte j = 0; j < piece.grid[0].length; j++)
			{
				byte temp = piece.grid[i][j];
				if (temp != 0) board[piece.y+i][piece.x+j] = temp;
			}
		}
	}

	private void setGhost()
	{
		ghost = new Piece (piece);
		check = new Piece (ghost);

		check.y++;

		while(!checkValid())
		{
			check.y++;
			ghost.y++;
		}
	}

	private boolean checkValid () //checks to see if the current piece has reached the wall/floor/another piece
	{
		// true if move is not valid

		// checks to see if the bottom line of the piece would surpass the bottom, left wall, right wall
		if (check.y + check.bottom() >= ROWS)
		{
			return true;
		}
		else if (check.x + check.left() < 0)
		{
			return true;
		}
		else if (check.x + check.right() >= COLUMNS)
		{
			return true;
		}

		// checks to see if the current piece has reached another piece
		for (byte i = 0; i < check.grid.length; i++)
		{
			for (byte j = 0; j < check.grid[0].length; j++)
			{
				if (check.grid[i][j] != 0 && board[check.y+i][check.x+j] != 0)
				{
					return true;
				}
			}
		}

		// if none of these issues have occurred, the piece does not have to stop
		return false;
	}

	private void checkLine()
	{
		boolean full;

		// checks lines
		for (byte i = ROWS-1; i >= piece.y; i--) // checks all rows up to the top of the newly added piece
		{
			full = true;

			for (byte j = 0; j < COLUMNS; j++)
			{
				if (board[i][j] == 0) full = false;
			}

			if (full)
			{
				// drop the lines above it
				for (byte a = i; a > 0; a--)
				{
					for (byte b = 0; b < COLUMNS; b++)
					{
						board[a][b] = board[a-1][b];
					}
				}

				lines++;

				return;
			}
		}
	}

	private void checkEndGame()
	{
		for (byte j = 0; j < COLUMNS; j++)
		{
			if (board[2][j] != 0) gameOver = true;
		}
	}

	private void kick()													// Fix kicks perhaps?
	{
		// kick right
		check.x++;
		if (!checkValid())
		{
			piece.rotateCW();
			piece.x++;
			return;
		}

		// kick left
		check.x -= 2;
		if (!checkValid())
		{
			piece.rotateCW();
			piece.x--;
			return;
		}

		// kick down
		check.x++;
		check.y++;
		if (!checkValid())
		{
			piece.rotateCW();
			piece.y++;
			return;
		}

		// kick down-right
		check.x++;
		if (!checkValid())
		{
			piece.rotateCW();
			piece.x++;
			piece.y++;
			return;
		}

		// kick down-left
		check.x -=2 ;
		if (!checkValid())
		{
			piece.rotateCW();
			piece.x--;
			piece.y++;
			return;
		}

		// kick up
		check.x++;
		check.y-=2;
		if (!checkValid())
		{
			piece.rotateCW();
			piece.y--;
			return;
		}

		// kick up-right
		check.x++;
		if (!checkValid())
		{
			piece.rotateCW();
			piece.x++;
			piece.y--;
			return;
		}

		// kick up-left
		check.x-=2;
		if (!checkValid())
		{
			piece.rotateCW();
			piece.x--;
			piece.y--;
			return;
		}

											// try CCW???
	}

	public void display (Graphics g)
	{
		if (!gameOver && !pause) checkLine();

		// display board
		for (byte i = 2; i < ROWS; i++)
		{
			for (byte j = 0; j < COLUMNS; j++)
			{
				g.setColor(colours[board[i][j]]);
				g.fillRect(SIZE*j, SIZE*(i-2), SIZE, SIZE);
			}
		}

		// display ghost
		if (seeGhost)
		{
			setGhost();
			for (byte i = 0; i < ghost.grid.length; i++)
			{
				for (byte j = 0; j < ghost.grid[0].length; j++)
				{
					byte temp = ghost.grid[i][j];
					if (temp != 0)
					{
						g.setColor(colours[temp].darker().darker());
						g.fillRect(SIZE*(j+ghost.x), SIZE*(ghost.y+i-2), SIZE, SIZE);
					}
				}
			}
		}

		// display piece
		if (piece != null)
			for (byte i = 0; i < piece.grid.length; i++)
				for (byte j = 0; j < piece.grid[0].length; j++)
				{
					byte temp = piece.grid[i][j];
					if (temp != 0)
					{
						g.setColor(colours[temp]);
						g.fillRect(SIZE*(j+piece.x), SIZE*(piece.y+i-2), SIZE, SIZE);
					}
				}

		// display held piece
		for (byte i = 0; i < hold.grid.length; i++)
		{
			for (byte j = 0; j < hold.grid[0].length; j++)
			{
				byte temp = hold.grid[i][j];
				g.setColor(colours[temp]);
				g.fillRect(SIZE*j+350, SIZE*i+450, SIZE, SIZE);
			}
		}

		// display next piece
		for (byte i = 0; i < next.grid.length; i++)
		{
			for (byte j = 0; j < next.grid[0].length; j++)
			{
				byte temp = next.grid[i][j];
				g.setColor(colours[temp]);
				g.fillRect(SIZE*j+350, SIZE*i+50, SIZE, SIZE);
			}
		}

		// grid lines
		g.setColor(Color.GRAY);
		for (byte i = 1; i < 10; i++) g.drawLine(SIZE*i, 0, SIZE*i, 20*SIZE); // vertical
		for (byte i = 1; i < 20; i++) g.drawLine(0, SIZE*i, 10*SIZE, SIZE*i); // horizontal
		// grid lines for held piece
		for (byte i = 0; i <= hold.grid[0].length; i++) g.drawLine(350+SIZE*i, 450, 350+SIZE*i, 450+hold.grid.length*SIZE); // vertical
		for (byte i = 0; i <= hold.grid.length; i++) g.drawLine(350, SIZE*i+450, 350+hold.grid[0].length*SIZE, SIZE*i+450); // horizontal
		// grid lines for next piece
		for (byte i = 0; i <= next.grid[0].length; i++) g.drawLine(350+SIZE*i, 50, 350+SIZE*i, 50+next.grid.length*SIZE); // vertical
		for (byte i = 0; i <= next.grid.length; i++) g.drawLine(350, SIZE*i+50, 350+next.grid[0].length*SIZE, SIZE*i+50); // horizontal

		g.setColor(Color.RED);
		g.drawLine(0, 0, 0, 20*SIZE); // Left
		g.drawLine(10*SIZE, 0, 10*SIZE, 20*SIZE); // Right
		g.drawLine(0, SIZE*20, 10*SIZE, SIZE*20); // Bottom

		// Score
		g.setColor(Color.BLACK);
		g.drawString(("Pieces: "+pieces), 350, 350);
		g.drawString(("Lines: "+lines), 350, 250);
		g.drawString(("Next"), 350, 48);
		g.drawString(("Held"), 350, 448);

		if (gameOver)
		{
			g.fillRect(111,265,77,20);
			g.setColor(Color.RED);
			g.drawRect(111,265,77,20);
			g.drawString(("GAME OVER"), 114, 280);
			seeGhost = false;
			piece = null;

			if (!pause) clearNextPiece();
		}
		else if (pause)
		{
			g.setColor(Color.WHITE);
			g.fillRect(120,265,61,20);
			g.setColor(Color.BLUE);
			g.drawString(("PAUSE"), 132, 280);
		}
	}

	public void clearNextPiece()
	{
		for (byte i = 0; i < ROWS; i++)
		{
			for (byte j = 0; j < COLUMNS; j++)
			{
				if (board[i][j] != 0)
				{
					board[i][j] = 0;
					return;
				}
			}
		}

		pause = true;
	}

	public static void main(String[] args)
	{
		Program prog = new Program();
	}
}