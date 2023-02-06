import javax.swing.*; 
import java.io.File;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.Timer;
import javax.imageio.ImageIO;
public class IntroScreen extends JPanel implements ActionListener{
	private Game game;
	private JButton playButton; private JButton instructionsButton;
	public IntroScreen (Game game) {
		int width = 800;
		int height = 600;
		this.setBackground(new Color (190,190,190)); //temporary
		this.setLayout(null);
		playButton = new JButton ("Play");
		instructionsButton = new JButton ("Instructions");
		playButton.setActionCommand("P");
		playButton.addActionListener(this);
		instructionsButton.setActionCommand("I");
		instructionsButton.addActionListener(this);
		playButton.setBackground(new Color (178,0,0));
		playButton.setForeground(Color.WHITE);
		playButton.setSize((int) (width*.3), (int) (height*.12));
		playButton.setLocation((int) (width*.35), (int)(height*.51));
		playButton.setFont(new Font ("Arial", Font.PLAIN, (int)(height/17)));
		this.add(playButton);
		instructionsButton.setBackground(new Color (178,0,0));
		instructionsButton.setForeground(Color.WHITE);
		instructionsButton.setSize((int) (width*.3), (int) (height*.12));
		instructionsButton.setLocation((int) (width*.35), (int)(height*.69));
		instructionsButton.setFont(new Font ("Arial", Font.PLAIN, (int)(height/17)));
		this.add(instructionsButton);
		this.game = game;
	}
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLUE);
		g.drawRect(50, 50, 100, 100);
		BufferedImage i;
		try {
			i = ImageIO.read(new File("OtherImages/GoodBackgroundAD.jpg"));
		}
		catch (Exception e) {
			i = null;
		}
		int width = 800;
		int height = 600;
		g.drawImage(i,0,0, width, height,null); 
		g.setColor(new Color (96,96,96));
		g.fillRoundRect((int) (width*.34)-3, (int)(height*.5)+2, (int) (width*.32), (int) (height*.14), (int) (width*.02), (int) (width*.02));
		g.fillRoundRect((int) (width*.34)-3, (int)(height*.68)+2, (int) (width*.32), (int) (height*.14), (int) (width*.02), (int) (width*.02));
		g.setColor(new Color (178,0,0));
		g.fillRoundRect((int) (width*.34), (int)(height*.5), (int) (int) (width*.32), (int) (height*.14), (int) (width*.02), (int) (width*.02));
		g.fillRoundRect((int) (width*.34), (int)(height*.68), (int) (int) (width*.32), (int) (height*.14), (int) (width*.02), (int) (width*.02));
		g.setFont(new Font ("Calibri", Font.BOLD, (int)(height*.24))); 
		g.setColor(new Color (0,0,128));
		Graphics2D g2 = (Graphics2D)g;
	    AffineTransform a = g2.getTransform();
	    g2.rotate(-Math.PI/2);
	    g2.setTransform(a);
	    g2.drawString("3D MAZE", 118, 200);
	}
	public void actionPerformed (ActionEvent event) {
		String get = event.getActionCommand();
		if (get.equals("P")) {
			game.goToDifficultySelect();
		}
		if (get.equals("I")) {
			game.toggleInstructions();
		}
	}
}