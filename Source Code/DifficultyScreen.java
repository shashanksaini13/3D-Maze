import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*; 
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
public class DifficultyScreen extends JPanel implements ActionListener{
	private Game game;
	private JButton easy; private JButton medium; private JButton hard;
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
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
		g.setFont(new Font ("Arial", Font.BOLD, (int)(height/8)));
		g.setColor(new Color (96,96,96));
		g.drawString("Difficulty", (int)(width*.3075)-5, (int)(height*.3)-2);
		g.setColor(Color.WHITE);
		g.drawString("Difficulty", (int)(width*.3075), (int)(height*.3));
		g.setFont(new Font ("Arial", Font.BOLD, (int)(height/20)));
		g.drawString("4 x 4 x 4", (int)(width*.1175), (int)(height*.8));
		g.drawString("5 x 5 x 5", (int)(width*.425), (int)(height*.8));
		g.drawString("6 x 6 x 6", (int)(width*.7325), (int)(height*.8));
		g.setColor(new Color (96,96,96));
		g.fillRoundRect((int) (width*.0775)-5, (int)(height*.5)+4, (int) (width*.23), (int) (height*.18), (int) (width*.025), (int) (width*.025));
		g.fillRoundRect((int) (width*.385)-5, (int)(height*.5)+4, (int) (width*.23), (int) (height*.18), (int) (width*.025), (int) (width*.025));
		g.fillRoundRect((int) (width*.6925)-5, (int)(height*.5)+4, (int) (width*.23), (int) (height*.18), (int) (width*.025), (int) (width*.025));
		g.setColor(new Color (178,0,0));
		g.fillRoundRect((int) (width*.0775), (int)(height*.5), (int) (width*.23), (int) (height*.18), (int) (width*.025), (int) (width*.025));
		g.fillRoundRect((int) (width*.385), (int)(height*.5), (int) (width*.23), (int) (height*.18), (int) (width*.025), (int) (width*.025));
		g.fillRoundRect((int) (width*.6925), (int)(height*.5), (int) (width*.23), (int) (height*.18), (int) (width*.025), (int) (width*.025));
	}
	public DifficultyScreen (Game game) {
		this.game = game;
		this.setLayout(null);
		this.setBorder(BorderFactory.createEmptyBorder(20,55,20,55));
		this.setBackground(new Color (190,190,190)); //temporary
		int width = 800;
		int height = 600;
		easy = new JButton ("Easy");
		medium = new JButton ("Medium");
		hard = new JButton ("Hard");
		easy.setActionCommand("E");
		medium.setActionCommand("M");
		hard.setActionCommand("H");
		easy.addActionListener(this);
		medium.addActionListener(this);
		hard.addActionListener(this);
		easy.setBackground(new Color (178,0,0));
		easy.setForeground(Color.WHITE);
		easy.setSize((int) (width*.21), (int) (height*.16));
		easy.setLocation((int) (width*.0875), (int)(height*.51));
		medium.setBackground(new Color (178,0,0));
		medium.setForeground(Color.WHITE); 
		medium.setSize((int) (width*.21), (int) (height*.16));
		medium.setLocation((int) (width*.395), (int)(height*.51));
		hard.setBackground(new Color (178,0,0));
		hard.setForeground(Color.WHITE);
		hard.setSize((int) (width*.21), (int) (height*.16));
		hard.setLocation((int) (width*.7025), (int)(height*.51));
		easy.setFont(new Font ("Arial", Font.BOLD, (int)(height/20)));
		medium.setFont(new Font ("Arial", Font.BOLD, (int)(height/20)));
		hard.setFont(new Font ("Arial", Font.BOLD, (int)(height/20)));
		this.add(easy); this.add(medium); this.add(hard);
	}
	public void actionPerformed (ActionEvent event) {
		String get = event.getActionCommand();
		try {
			if (get.equals("E")) {
				game.runGame(0);
			}
			if (get.equals("M")) {
				game.runGame(1);
			}
			if (get.equals("H")) {
				game.runGame(2);
			}
		}
		catch (Exception e) {} //throws an error with the IO
	}
}