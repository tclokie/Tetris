import java.awt.*;
import java.awt.event.*;

public class Program extends Frame implements WindowListener, KeyListener
{
	public Program()
	{
		GamePanel panel = new GamePanel();

		setTitle("Trevor's Awesome Tetris");
		setLocation(100,60); // X,Y (ints)
		setSize(500,650); // X,Y (ints)
		setResizable(false);
		setVisible(true);

		addWindowListener(this);
		addKeyListener(panel);
		add(panel);
	}
	public void windowDeactivated(WindowEvent we){} // not current window being used
	public void windowActivated(WindowEvent we){} // is current window being used
	public void windowDeiconified(WindowEvent we){}
	public void windowIconified(WindowEvent we){}
	public void windowClosed(WindowEvent we){} // finished closing
	public void windowClosing(WindowEvent we) // when you hit that red X
	{
		System.exit(0);
		//dispose();
	}
	public void windowOpened(WindowEvent we){} // finished opening

	public void keyPressed(KeyEvent ke){}
	public void keyReleased(KeyEvent ke){}
	public void keyTyped(KeyEvent ke){}
}