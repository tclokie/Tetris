import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class GamePanel extends Panel implements KeyListener
{
	private Dimension dim = null;

	Tetris game = null;

	BufferedImage osi;
	Graphics osg;

	static final int delay = 10;
	static final int period = 25;

	private static int count = 0;
	private static int level = 40;
	private static int linesTemp = 0;

	Timer tm = new Timer ();
	TimerTask task = new TimerTask ()
	{
		public void run()
		{
			if (dim != null && !game.pause)
			{
				if (linesTemp != game.lines)
				{
					level = (int)(40*Math.pow(0.95, game.lines));
					linesTemp = game.lines;
				}
				if (level < 5) level = 5;

				if (count % level == level-1)
				{
					game.update();
					count = 0;
				}
				else count++;

				repaint();
			}
		}
	};

	public GamePanel ()
	{
		addKeyListener(this);
		game = new Tetris();
		tm.scheduleAtFixedRate(task, delay, period);
	}

	public void paint (Graphics g)
	{
		dim = getSize();
		osi = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
		osg = osi.getGraphics();
		osg.setFont(new Font("Courier", Font.BOLD, 14));
		update(g);
	}

	public void update (Graphics g)
	{
		// clear previous frame;
		osg.setColor(Color.LIGHT_GRAY);
		osg.fillRect(0, 0, dim.width, dim.height);

		// do everything in osg
		game.display(osg);

		// then output:
		g.drawImage(osi, 0, 0, this);
	}

	public void keyPressed(KeyEvent ke)
	{
		switch (ke.getKeyCode())
		{
			case KeyEvent.VK_UP:	if (!game.pause && !game.gameOver) game.rotate(); // rotate CW
								break;
			case KeyEvent.VK_DOWN:	if (!game.pause && !game.gameOver) game.update(); // drop 1 step
								break;
			case KeyEvent.VK_LEFT:	if (!game.pause && !game.gameOver) game.move(true); // move left
								break;
			case KeyEvent.VK_RIGHT:	if (!game.pause && !game.gameOver) game.move(false); // move right
								break;
			case KeyEvent.VK_SPACE: if (!game.pause && !game.gameOver) game.drop(); // drop to bottom
								break;
			case KeyEvent.VK_SHIFT: if (!game.pause && !game.gameOver) game.swap(); // swap next piece with current piece
								break;
			case KeyEvent.VK_ESCAPE:	System.exit(0);
								break;
			case KeyEvent.VK_R:	if (game.pause) game = new Tetris();
								count = 0;
								level = 40;
								linesTemp = 0;
								break;
			case KeyEvent.VK_P:	game.pause = !game.pause;
								break;
			case KeyEvent.VK_G:	game.seeGhost = !game.seeGhost;
								break;
		}

		repaint();
	}

	public void keyReleased(KeyEvent ke){}
	public void keyTyped(KeyEvent ke){}

	public static void main(String[] args)
	{
		Program prog = new Program();
	}
}
