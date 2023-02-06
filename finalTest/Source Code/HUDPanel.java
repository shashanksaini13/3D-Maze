import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
public class HUDPanel extends JPanel implements ActionListener, MouseListener {
	private static final String iconPath = "Icons/";
	private JButton[] buttons;
	private Game game;
	private Maze maze;
	private Player player;
	private static String[] commands = { "up", "left", "forward", "right", "down" };
	public static final int forward = 1;
	public static final int up = 2;
	public static final int down = 3;
	public static final int right = 4;
	public static final int left = 5;
	private boolean menuOn;
	public MovementListener movementListener;
	private ChamberLayers chamberLayers;
	private JPanel dPadPanel;
	public HUDPanel hudPanel = this;

	private final BufferedImage mapWordBtn, instructionsBtn, quitBtn, menuBtn, mapBtn;
	private int releasedX, releasedY;
	private boolean mapOn, instructOn;

	public HUDPanel(Game game, Maze maze, ChamberLayers chamberLayers) throws IOException {
		mapWordBtn=ImageIO.read(new File(iconPath+"Map Button.png"));
		instructionsBtn=ImageIO.read(new File(iconPath+"Instructions Button.png"));
		quitBtn=ImageIO.read(new File(iconPath+"Quit Button.png"));
		menuBtn=ImageIO.read(new File(iconPath+"Menu Button.png"));
		mapBtn=ImageIO.read(new File(iconPath+"Map Button 1.png"));

		menuOn=false;
		mapOn=false;
		instructOn=false;
		
		this.game = game;
		player = game.getPlayer();
		this.maze = maze;
		this.chamberLayers = chamberLayers;
		player = game.getPlayer();
		setProperties();
		setupEventListeners();
		setupButtons();
		addButtons();
		requestFocusInWindow();
		setFocusTraversalKeysEnabled(false);
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		Font f = new Font("Arial", Font.BOLD, 20);
		g.setFont(f);
		g.drawString("Level: " + (player.getPosition().getZ()+1) + "     Row: " + (player.getPosition().getY()+1) + "     Column: "
				+ (player.getPosition().getX()+1), (int) (.05 * 600), (int) (.05 * 800));
		g.drawString("Moves Made: " + player.getMoves(), (int) (.05 * 600), (int) (.08 * 800));
		String orientString = "";
		switch (player.getOrientation()) {
			case 0:
				orientString = "North";
				break;
			case 1:
				orientString = "East";
				break;
			case 2:
				orientString = "South";
				break;
			case 3:
				orientString = "West";
				break;
		}
		g.drawString("Facing: " + orientString, (int) (.05 * 600), (int) (.11 * 800));
		
		Graphics2D g2d=(Graphics2D)g;
		if (menuOn) {
			menuOn(g2d);
		} else {
			menuOff(g2d);
		}
	}

	private void checkBtnClick() throws IOException {
		if (!mapOn&&!instructOn&&!menuOn&&releasedX>639&&releasedX<639+mapBtn.getWidth()&&releasedY>43&&releasedY<43+mapBtn.getHeight()) {
			mapOn=true;
			menuOn=false;
			game.goToMapView();
		} else if (!mapOn&&!instructOn&&releasedX>719&&releasedX<719+menuBtn.getWidth()&&releasedY>45&&releasedY<45+menuBtn.getHeight()) {
			menuOn=!menuOn;
		} else if (!mapOn&&!instructOn&&menuOn&&releasedX>640&&releasedX<640+mapWordBtn.getWidth()&&releasedY>130&&releasedY<130+mapWordBtn.getHeight()) {
			mapOn=true;
			menuOn=false;
			game.goToMapView();
		} else if (!mapOn&&!instructOn&&menuOn&&releasedX>640&&releasedX<640+instructionsBtn.getWidth()&&releasedY>190&&releasedY<190+instructionsBtn.getHeight()) {
			instructOn=true;
			menuOn=false;
			game.toggleInstructions();
		} else if (!mapOn&&!instructOn&&menuOn&&releasedX>640&&releasedX<640+quitBtn.getWidth()&&releasedY>250&&releasedY<250+quitBtn.getHeight()) {
			menuOn=false;
			game.goToIntroScreen();
			game.changeGame(false);
		}		
	}

	private void menuOff(Graphics2D g2d) {
		g2d.drawImage(menuBtn, 710, 15, null);
		g2d.drawImage(mapBtn, 630, 11, null);
	}

	private void menuOn(Graphics2D g2d) {
		g2d.setColor(Color.decode("#808080"));
		g2d.fillRect(600, 0, 200, 290);
		g2d.setColor(Color.black);
		g2d.fillRect(600, 0, 7, 297);
		g2d.fillRect(600, 290, 200, 7);
		g2d.drawImage(mapWordBtn, 630, 100, null);
		g2d.drawImage(instructionsBtn, 630, 160, null);
		g2d.drawImage(quitBtn, 630, 220, null);
		g2d.drawImage(menuBtn, 710, 15, null);
	}
	
	public void changeMap(boolean map) {
		mapOn=map;
	}
	
	public void changeInstruct(boolean instruct) {
		instructOn=instruct;
	}
	
	public void mouseClicked(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {
		if (!mapOn) {
			releasedX=e.getX();
			releasedY=e.getY();
			System.out.println(releasedX+" "+releasedY);
			try {
			checkBtnClick();
			}catch (Exception z) {};
		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}
	private void setProperties() {
		this.setFocusTraversalKeysEnabled(false);
		this.setFocusable(true);
		//rows = 600 / 50;
		//cols = 800 / 50;
		this.setLayout(new GridLayout(4, 5, 0, 0));
		this.setSize(800, 600);
		this.setOpaque(false);
	}

	private void setupEventListeners() {
		movementListener = new MovementListener(game.getPlayer());
		game.getFrame().addKeyListener(movementListener);
		game.getFrame().addMouseListener(this);
	}

		private void setupButtons() {
			buttons = new JButton[5];
			for (int i = 0; i < buttons.length; i++) {
				ImageIcon icon = new ImageIcon(iconPath + commands[i] + "Arrow.png");
				buttons[i] = new JButton(icon);
				buttons[i].setActionCommand(commands[i]);
				buttons[i].addActionListener(movementListener);
				buttons[i].setContentAreaFilled(false);
				buttons[i].setBorderPainted(false);
				buttons[i].setFocusable(false);
			}
		}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("map")) {
			try {
			game.goToMapView();
			} catch (Exception z) {}
		} else if (e.getActionCommand().equals("instructions")) {
			game.toggleInstructions();
		} else if (e.getActionCommand().equals("quit")) {
			game.goToIntroScreen();
		}
	}

	private void addButtons() {	
		System.out.println("here");
		dPadPanel = new JPanel();
		dPadPanel.setOpaque(false);
		dPadPanel.setLayout(new GridLayout(3, 3, 0, 0));
		dPadPanel.add(new JLabel());
		dPadPanel.add(buttons[0]);
		dPadPanel.add(new JLabel());
		dPadPanel.add(buttons[1]);
		dPadPanel.add(buttons[2]);
		dPadPanel.add(buttons[3]);
		dPadPanel.add(new JLabel());
		dPadPanel.add(buttons[4]);
		dPadPanel.add(new JLabel());
		dPadPanel.setEnabled(true);
		
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 5; j++)
				if (i == 0 && j == 4)
					this.add(new JLabel());
				else if (i == 3 && j == 0)
					this.add(dPadPanel);
				else
					this.add(new JLabel());
		disableComponents();
		enableComponents();
	}

	public void disableComponents() {
		for (Component comp : dPadPanel.getComponents()) {
			comp.setEnabled(false);
		}
	}

	public void enableComponents(Room room, int orientation) {
		buttons[1].setEnabled(true);
		buttons[3].setEnabled(true);
		buttons[0].setEnabled(canMove(room, up, orientation));
		buttons[4].setEnabled(canMove(room, down, orientation));
		buttons[2].setEnabled(canMove(room, forward, orientation));
	}

	public void enableComponents () {
		enableComponents(getRoom(maze, player), player.getOrientation());
	}

	public static boolean canMove(Room room, int movementDirection, int playerOrientation) {
		if (movementDirection == left || movementDirection == right) {
			return true;
		} else if (movementDirection == up) {
			return room.getDoor(Room.up);
		} else if (movementDirection == down) {
			return room.getDoor(Room.down);
		} else if (movementDirection == forward) {
			return room.getDoor(playerOrientation);
		}
		return false;
	}

	public static Room getRoom(Maze maze, Player player) {
		Position pos = player.getPosition();
		return maze.getRoom(pos.getX(), pos.getY(), pos.getZ());
	}

	public class MovementListener implements ActionListener, KeyListener {
		private Player player;

		public MovementListener(Player p) {
			this.player = p;
		}

		public void animationFinished() {
			chamberLayers.setAnimation(0);
			enableComponents();
		}

		public void actionPerformed(ActionEvent e) {
			if (!mapOn&&!instructOn&&chamberLayers.getAnimation() == 0) {
				if (e.getActionCommand().equals(commands[2])) {
					move(forward);
				} else if (e.getActionCommand().equals(commands[0])) {
					move(up);
				} else if (e.getActionCommand().equals(commands[4])) {
					move(down);
				} else if (e.getActionCommand().equals(commands[1])) {
					move(left);
				} else if (e.getActionCommand().equals(commands[3])) {
					move(right);
				}
			}
		}

		public void keyReleased(KeyEvent e) {
			if (!mapOn&&!instructOn&&chamberLayers.getAnimation() == 0) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					move(forward);
				} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP) {
					move(up);
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN) {
					move(down);
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_KP_LEFT) {
					move(left);
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_KP_RIGHT) {
					move(right);
				}
				
////				//TODO: REMOVE FOR FINAL RELASE
//				else if (e.getKeyCode() == KeyEvent.VK_ALT) {
//					game.getPlayer().setPosition(new Position(0, 0, 0));
//					chamberLayers.getChamberView().devExit();
//				}
			}
			if (!mapOn&&!instructOn&&e.getKeyCode() == KeyEvent.VK_TAB) {
				mapOn=true;
				menuOn=false;
				try {
				game.goToMapView();
				} catch (Exception z) {}
			}
		}

		public void move(int direction) {
			chamberLayers.setAnimation(direction);
			disableComponents();
			double score = (double) maze.shortestPath() / (double) player.getMoves();
			if (!canMove(getRoom(maze, player), direction, player.getOrientation())) {
				animationFinished();
				return;
			}
			if (direction == forward) {
				if (getRoom(maze, player).leadsOutside(player.getOrientation())) {
					game.win(score);
				} else {
				player.moveForward();
				chamberLayers.getChamberView().moveForward();
			}
			} else if (direction == down) {
				if (getRoom(maze, player).leadsOutside(Room.down)) {
					game.win(score);
				} else {
				player.moveDown();
				chamberLayers.getChamberView().moveDown();
			}
			} else if (direction == up) {
				if (getRoom(maze, player).leadsOutside(Room.up)) {
					game.win(score);
				} else {
				player.moveUp();
				chamberLayers.getChamberView().moveUp();
			}
			} else if (direction == left) {
				player.turnLeft();
				chamberLayers.getChamberView().turnLeft();
			} else if (direction == right) {
				player.turnRight();
				chamberLayers.getChamberView().turnRight();
			}
		}

		// Beginning of empty methods
		public void keyPressed(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
		// End of empty methods
	}
}