import javax.imageio.ImageIO;
import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
public class Instructions extends JPanel implements ActionListener{
	private Game game; private JButton backButton;
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		int width = 800;
		int height = 600;
		try {
			final BufferedImage i = ImageIO.read(new File("OtherImages/ArrowThing.png"));
			int axel=51;
			g.drawImage(i,482,244,120,120,null); 
		}
		catch (Exception e) {
		}
		g.setColor(new Color (96,96,96));
		g.setFont(new Font ("Arial", Font.BOLD, (int)(height/9)));
		g.drawString("Instructions:", (int)( width*.05)-3, (int)(height*.16)-5);
		g.setColor(Color.WHITE);
		g.drawString("Instructions:", (int)(width*.05), (int)(height*.16));
		g.setFont(new Font ("Arial", Font.PLAIN, (int)(height/20)));
		g.drawString("TAB (toggle): Opens the map", (int)(width*.06), (int)(height*.34));
		g.drawString("Left Arrow Key: Rotate left", (int)(width*.06), (int)(height*.405));
		g.drawString("Right Arrow Key: Rotate right", (int)(width*.06), (int)(height*.47));
		g.drawString("Up Arrow Key: Go up", (int)(width*.06), (int)(height*.6));
		g.drawString("Down Arroy Key: Go Down", (int)(width*.06), (int)(height*.665));
		g.drawString("Space Bar: Go forward", (int)(width*.06), (int)(height*.73));
		g.setFont(new Font ("Arial", Font.PLAIN, (int)(height/30)));
		g.drawString("Left arrow: Rotate left", (int)(width*.58), (int)(height*.23));
		g.drawString("Right arrow: Rotate right", (int)(width*.58), (int)(height*.27));
		g.drawString("Middle arrow: Go forward", (int)(width*.58), (int)(height*.31));
		g.drawString("Up arrow: Go up", (int)(width*.58), (int)(height*.35));
		g.drawString("Down arrow: Go down", (int)(width*.58), (int)(height*.39));
		g.drawString("Triple line: Expands menu", (int)(width*.58), (int)(height*.7));
		g.drawString("Map button: Opens map", (int)(width*.58), (int)(height*.74));
		g.setFont(new Font ("Arial", Font.BOLD, (int)(height/25)));
		g.drawString("Click buttons on the D-pad:", (int)(width*.58), (int)(height*.18));
		g.drawString("Menu buttons:", (int)(width*.58), (int)(height*.65));
		g.setColor(new Color (96,96,96));
		g.fillRoundRect((int) (width*.45)-4, (int)(height*.79)+3, (int) (width*.36), (int) (height*.11), (int) (width*.015), (int) (width*.015));
		g.setColor(new Color (178,0,0));
		g.fillRoundRect((int) (width*.45), (int)(height*.79), (int) (width*.36), (int) (height*.11), (int) (width*.015), (int) (width*.015));
		//draw the image paintComponent is automatic right???
	}
	public void actionPerformed (ActionEvent event) {
		game.toggleInstructions();
		//game.getChamberLayers().HUDPanel.changeInstruct(false);
	}
	public Instructions (Game game) {
		int width = 800;
		int height = 600;
		this.game = game;
		this.setLayout(null);
		this.setBackground(new Color (153,153,153));
		backButton = new JButton ("Back");
		backButton.addActionListener(this);
		backButton.setBackground(new Color (178,0,0));
		backButton.setForeground(Color.WHITE);
		backButton.setSize((int) (width*.34), (int) (height*.1));
		backButton.setLocation((int) (width*.46), (int)(height*.79));
		backButton.setFont(new Font ("Arial", Font.PLAIN, (int)(height/13)));
		this.add(backButton);
	}
	
}