import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.io.*;

public class Game {
	private ArrayList<Double> minimumScores;
	private ArrayList<Double> topTen;
	private int screenWidth, screenHeight;
	private JFrame gameFrame;
	private Player player;
	private Maze maze;
	private IntroScreen introScreen;
	private DifficultyScreen difficultyScreen;
	private ChamberView chamberView;
	private ChamberLayers chamberLayers;
	private MapView mapView;
	private Instructions instructionScreen;
	private Component backFromInstructions = null;;
	private EndScreen endScreen;
	private boolean gameOn;
	
	public ChamberLayers getChamberLayers () {
		return chamberLayers;
	}
	
	public JFrame getFrame () {
		return gameFrame;
	}
	
	public static void main(String[] args) throws IOException {
		Game game = new Game();
		//game.runGame(1);
	}

	public Game() {
		Painting.loadImages();
		minimumScores = new ArrayList<Double>();
		for (int i = 0; i < 10; i++)
			minimumScores.add(0.0);
		
		gameFrame = new JFrame();
		gameFrame.setMinimumSize(new Dimension(800,630));
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setResizable(false);
		gameFrame.setFocusable(true);
		
		introScreen = new IntroScreen(this);
		introScreen.setMinimumSize(new Dimension(800,600));
		
		difficultyScreen = new DifficultyScreen(this);
		difficultyScreen.setMinimumSize(new Dimension(800,600));
		
		instructionScreen = new Instructions(this);
		instructionScreen.setMinimumSize(new Dimension(800,600));
		
		gameFrame.add(introScreen);
		gameFrame.setVisible(true);
		
		gameOn=false;
	}

	public void goToIntroScreen() {
		gameFrame.getContentPane().removeAll();
		gameFrame.add(introScreen);
		gameFrame.revalidate();
		gameFrame.repaint();
	}
	
	public void goToChamberView() {
		gameFrame.getContentPane().removeAll();
		gameFrame.add(chamberLayers);
		gameFrame.revalidate();
		gameFrame.repaint();
	}

	public void goToMapView() throws IOException {
		gameFrame.getContentPane().removeAll();
		mapView = new MapView(this, maze);
		gameFrame.add(mapView);
		gameFrame.revalidate();
		gameFrame.repaint();
	}

	public void goToDifficultySelect() {
		gameFrame.getContentPane().removeAll();
		gameFrame.add(difficultyScreen);
		gameFrame.revalidate();
		gameFrame.repaint();
	}

	public void runGame(int difficulty) throws IOException {
		int size = 1;
		if (difficulty == 0) {
			size = 4;
		} else if (difficulty == 1) {
			size = 5;
		} else if (difficulty == 2) {
			size = 6;
		}
		Position position = new Position(size-1, size-1, size-1);
		maze = new Maze(difficulty);
		player = new Player(position, 0, size, size, size);
		chamberLayers = new ChamberLayers(this, maze);
		gameFrame.getContentPane().removeAll();
		gameFrame.add(chamberLayers);
		gameFrame.revalidate();
		gameOn=true;
	}

	public void toggleInstructions() {
		if (backFromInstructions != null) {
			gameFrame.getContentPane().removeAll();
			gameFrame.add(backFromInstructions);
			gameFrame.revalidate();
			backFromInstructions.repaint();
			backFromInstructions = null;
			if (gameOn) {
				getChamberLayers().HUDPanel.changeInstruct(false);
			}
		} else {
			backFromInstructions = gameFrame.getContentPane().getComponent(0);
			gameFrame.getContentPane().removeAll();
			gameFrame.add(instructionScreen);
			gameFrame.revalidate();
			instructionScreen.repaint();
		}
	}
	
	public void changeGame(boolean game) {
		gameOn=game;
	}

	public ArrayList<Double> getMinimumScores() {
		return minimumScores;
	}

	public void submitScore(double score) {
		minimumScores.add(score);
		Collections.sort(minimumScores);
		Collections.reverse(minimumScores);
	}

	public void win(double score) {
		submitScore(score);
		System.out.println(minimumScores);
		topTen = new ArrayList<Double>();
		if (minimumScores.size() > 10)
			for (int i = 0; i < 10; i++)
				topTen.add(minimumScores.get(i));
		else
			topTen = minimumScores;
		endScreen = new EndScreen(score, topTen, this);
		gameFrame.getContentPane().removeAll();
		gameFrame.add(endScreen);
		gameFrame.revalidate();
	}

//	public void drawHeader(Graphics g) {
//		super.drawHeader(g);
//		g.setColor(Color.WHITE);
//		Font f = new Font("Arial", Font.BOLD, 20);
//		g.setFont(f);
//		g.drawString("Level: " + player.getPosition()[2] + "Row: " + player.getPosition()[0] + "Column: "
//				+ player.getPosition()[1], (int) (.05 * screenWidth), (int) (.05 * screenHeight));
//		g.drawString("Moves Made: " + player.getMoves(), (int) (.05 * screenWidth), (int) (.08 * screenHeight));
//		g.drawString("Facing: " + player.getOrientation(), (int) (.05 * screenWidth), (int) (.11 * screenHeight));
//	}

	public Player getPlayer() {
		return player;
	}
}