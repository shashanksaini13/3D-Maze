import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class ChamberView extends JPanel {
	private Game game;
	private Maze maze;
	private Position playerPos, nextRoomPos;
	private int playerDirection; // N0, E1, S2, W3
	private Vector cameraPos, screenPlaneRelPos;
	private Room currentRoom, nextRoom;
	private ArrayList<Triangle> triList;
	private double theta, phi;
	private java.util.Timer timer;
	private int animationTimer;
	private int animationType; //0 default, 1 rotate right, 2 rotate left, 3 forwards, 4 upwards, 5 downwards
	private double doorAngleN, doorAngleS, doorAngleE, doorAngleW, doorAngleU, doorAngleD;
	private Color[] doorColorArray, nextRoomDoorColorArray;
	private final Color ceilingColor=Color.decode("#C3C3C3");
	private boolean pressed, menuOn, timerrun;
	private int phicount, speed;
	private ChamberLayers chamberLayers;

	public ChamberView (Game game, Maze maze, ChamberLayers chamberLayers) {
		timer = new java.util.Timer();
		pressed=false;
		menuOn=false;
		timerrun=true;
		this.chamberLayers = chamberLayers;
		this.setBackground(Color.WHITE);
		this.maze = maze;
		doorColorArray = new Color[6];
		nextRoomDoorColorArray = new Color[6];
		animationTimer = -1;
		animationType = 0;
		speed=20;
		playerPos = new Position(maze.getSize()-1,maze.getSize()-1,maze.getSize()-1);
		System.out.println("Maze size: " + maze.getSize());
		currentRoom = maze.getRoom(playerPos);
		nextRoom = null;
		theta = 0;
		phi = 0;
		phicount = 0;
		cameraPos = new Vector(100*playerPos.getX(), 100*playerPos.getY(), 100*playerPos.getZ()).clone().plus(new Vector(50,50,50));
		screenPlaneRelPos = new Vector(0, 13, 0);
		setUpRooms();

		this.setFocusable(true);
		ChamberView panel = this;
		timer();
	}

	public void moveForward() {
		if (!pressed) {
			pressed=true;
			boolean shouldMove = false;
			
			if (playerDirection == 0 && new Position(playerPos.getX(), playerPos.getY()+1, playerPos.getZ()).isValid(maze.getSize()) && 
					maze.getRoom(playerPos.getX(),  playerPos.getY(),  playerPos.getZ()).getDoor(Room.north)) {
				nextRoomPos = new Position(playerPos.getX(), playerPos.getY()+1, playerPos.getZ());
				nextRoom = maze.getRoom(playerPos.getX(), playerPos.getY()+1, playerPos.getZ());
				shouldMove = true;
			} else if (playerDirection == 2 && new Position(playerPos.getX(), playerPos.getY()-1, playerPos.getZ()).isValid(maze.getSize()) &&
					maze.getRoom(playerPos.getX(),  playerPos.getY(),  playerPos.getZ()).getDoor(Room.south)) {
				nextRoomPos = new Position(playerPos.getX(), playerPos.getY()-1, playerPos.getZ());
				nextRoom = maze.getRoom(playerPos.getX(), playerPos.getY()-1, playerPos.getZ());
				shouldMove = true;
			} else if (playerDirection == 1 && new Position(playerPos.getX()+1, playerPos.getY(), playerPos.getZ()).isValid(maze.getSize()) &&
					maze.getRoom(playerPos.getX(),  playerPos.getY(),  playerPos.getZ()).getDoor(Room.east)) {
				nextRoomPos = new Position(playerPos.getX()+1, playerPos.getY(), playerPos.getZ());
				nextRoom = maze.getRoom(playerPos.getX()+1, playerPos.getY(), playerPos.getZ());
				shouldMove = true;
			} else if (playerDirection == 3 && new Position(playerPos.getX()-1, playerPos.getY(), playerPos.getZ()).isValid(maze.getSize()) &&
					maze.getRoom(playerPos.getX(),  playerPos.getY(),  playerPos.getZ()).getDoor(Room.west)) {
				nextRoomPos = new Position(playerPos.getX()-1, playerPos.getY(), playerPos.getZ());
				nextRoom = maze.getRoom(playerPos.getX()-1, playerPos.getY(), playerPos.getZ());
				shouldMove = true;
			} else {
				pressed=false;
			}

			Vector roomVector = new Vector(100*playerPos.getX(), 100*playerPos.getY(), 100*playerPos.getZ());
			cameraPos = roomVector.clone().plus(new Vector(50,50,50));
			//System.out.println("playerDirection: " + playerDirection);
			//System.out.println("playerPos: " + playerPos);
			if (shouldMove) {
				animationTimer = 0;
				animationType = 3;
			}
		}
	}

////	//TODO: REMOVE FOR FINAL RELEASE
//	public void devExit () {
//		playerPos = new Position(0, 0, 0);
//		currentRoom = maze.getRoom(playerPos);
//		cameraPos = new Vector(50, 50, 50);
//		this.setUpRooms();
//		this.repaint();
//		System.out.println("playerPos: " + playerPos);
//		System.out.println("triList.size(): " + triList.size());
//	}
	
	public void moveUp() {
		if (new Position(playerPos.getX(), playerPos.getY(), playerPos.getZ()+1).isValid(maze.getSize())
				&& maze.getRoom(playerPos.getX(),  playerPos.getY(),  playerPos.getZ()).getDoor(Room.up)
				&& !pressed) {
			pressed = true;
			animationTimer = 0;
			animationType = 4;
			nextRoomPos = new Position(playerPos.getX(), playerPos.getY(), playerPos.getZ()+1);
			nextRoom = maze.getRoom(playerPos.getX(), playerPos.getY(), playerPos.getZ()+1);
		}
	}

	public void moveDown() {
		if (new Position(playerPos.getX(), playerPos.getY(), playerPos.getZ()-1).isValid(maze.getSize())
				&& maze.getRoom(playerPos.getX(), playerPos.getY(), playerPos.getZ()).getDoor(Room.down)
				&& !pressed) {
			pressed = true;
			animationTimer = 0;
			animationType = 5;
			nextRoomPos = new Position(playerPos.getX(), playerPos.getY(), playerPos.getZ()-1);
			nextRoom = maze.getRoom(playerPos.getX(), playerPos.getY(), playerPos.getZ()-1);
		}
	}

	public void turnLeft() {
		if (!pressed) {
			pressed=true;
			playerDirection = (playerDirection + 3) % 4;
			animationTimer = 0;
			animationType = 2;
		}
	}

	public void turnRight() {
		if (!pressed) {
			pressed=true;
			playerDirection = (playerDirection + 1) % 4;
			animationTimer = 0;
			animationType = 1;
		}
	}

	public void timer() {
		timer.cancel();
		timer = new java.util.Timer();
		timer.scheduleAtFixedRate(new TimerTask () {
			public void run () {
				setUpRooms();
				if (animationTimer == 40 && (animationType != 4 && animationType != 5)) {
					animationTimer = -1;
					pressed=false;
					if (animationType == 3) {
						if (playerDirection == 0)
							playerPos = new Position(playerPos.getX(), playerPos.getY()+1, playerPos.getZ());
						else if (playerDirection == 1)
							playerPos = new Position(playerPos.getX()+1, playerPos.getY(), playerPos.getZ());
						else if (playerDirection == 2)
							playerPos = new Position(playerPos.getX(), playerPos.getY()-1, playerPos.getZ());
						else
							playerPos = new Position(playerPos.getX()-1, playerPos.getY(), playerPos.getZ());
					}
					animationType = 0;
					doorAngleN = doorAngleE = doorAngleS = doorAngleW = doorAngleU = doorAngleD = 0;
					if (animationType == 3)
						chamberLayers.getHUDPanel().enableComponents(nextRoom, playerDirection);
					else
						chamberLayers.getHUDPanel().enableComponents(currentRoom, playerDirection);
					nextRoom = null;
					chamberLayers.setAnimation(0);
					chamberLayers.HUDPanel.movementListener.animationFinished();
				} else if (animationTimer == 80 && (animationType == 4 || animationType == 5)) {
					animationTimer = -1;
					pressed=false;
					if (animationType == 4) {
						playerPos = new Position(playerPos.getX(), playerPos.getY(), playerPos.getZ()+1);
					} else if (animationType == 5) {
						playerPos = new Position(playerPos.getX(), playerPos.getY(), playerPos.getZ()-1);
					}
					animationType = 0;
					doorAngleN = doorAngleE = doorAngleS = doorAngleW = doorAngleU = doorAngleD = 0;
					chamberLayers.getHUDPanel().enableComponents(nextRoom, playerDirection);
					nextRoom = null;
					chamberLayers.setAnimation(0);
					chamberLayers.HUDPanel.movementListener.animationFinished();
				}
				if (animationTimer == -1) {
//					theta = playerDirection*Math.PI/2;
//					phicount = 0;
//					phi = 0;
				} else if (animationType == 1) {
					animationTimer++;
					theta += Math.PI/(2*40);
					chamberLayers.getHUDPanel().disableComponents();
				} else if (animationType == 2) {
					animationTimer++;
					theta -= Math.PI/(2*40);
					chamberLayers.getHUDPanel().disableComponents();
				} else if (animationType == 3) {
					animationTimer++;
					cameraPos = cameraPos.plus(screenPlaneRelPos.scale(100/screenPlaneRelPos.magnitude()/40));
					if (playerDirection == 0)
						doorAngleN = Math.min(animationTimer, 20)*Math.PI/2/20;
					else if (playerDirection == 1)
						doorAngleE = Math.min(animationTimer, 20)*Math.PI/2/20;
					else if (playerDirection == 2)
						doorAngleS = Math.min(animationTimer, 20)*Math.PI/2/20;
					else
						doorAngleW = Math.min(animationTimer, 20)*Math.PI/2/20;
					chamberLayers.getHUDPanel().disableComponents();
				} else if (animationType == 4) {
					if (animationTimer < 20) {
						phicount++;
						phi = phicount*Math.PI/40;
					} else if (animationTimer < 60) {
						cameraPos = cameraPos.plus(new Vector(0,0,(double) 100/40));
					} else {
						phicount--;
						phi = phicount*Math.PI/40;
					}
					if (animationTimer < 40) {
						doorAngleU += Math.PI /40;
					}
					animationTimer++;
					chamberLayers.getHUDPanel().disableComponents();
				} else if (animationType == 5) {
					if (animationTimer < 20) {
						phicount--;
						phi = phicount*Math.PI/40;
					} else if (animationTimer < 60) {
						cameraPos = cameraPos.plus(new Vector(0,0,(double) -100/40));
					} else {
						phicount++;
						phi = phicount*Math.PI/40;
					}
					if (animationTimer < 40) {
						doorAngleD += Math.PI /40;
					}
					animationTimer++;
					chamberLayers.getHUDPanel().disableComponents();
				}
				screenPlaneRelPos = new Vector(15*Math.cos(phi)*Math.sin(theta), 15*Math.cos(phi)*Math.cos(theta), 15*Math.sin(phi));
				ChamberView.this.repaint();
			}
		}, 0, speed);
	}


	private void setUpNextRoom () {
		Color roomColor = nextRoom.getColor();
		Vector roomVector = new Vector(100*nextRoomPos.getX(), 100*nextRoomPos.getY(), 100*nextRoomPos.getZ());

		for (int i = 0; i < 6; i++)
			if (nextRoom.getDoor(i))
				if (nextRoom.leadsOutside(i))
					nextRoomDoorColorArray[i] = new Color(189, 189, 189);
				else
					nextRoomDoorColorArray[i] = Color.BLACK;
			else if (0 <= i && i <= 3)
				nextRoomDoorColorArray[i] = roomColor;
			else
				nextRoomDoorColorArray[i] = ceilingColor;
		
		if (animationType != 4 || animationTimer > 20) { //down
			triList.add(new Triangle(new Vector(0, 0, 0).plus(roomVector), new Vector(0, 100, 0).plus(roomVector), new Vector(35, 100, 0).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(0, 0, 0).plus(roomVector), new Vector(35, 0, 0).plus(roomVector), new Vector(35, 100, 0).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(65, 0, 0).plus(roomVector), new Vector(100, 0, 0).plus(roomVector), new Vector(100, 100, 0).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(65, 0, 0).plus(roomVector), new Vector(65, 100, 0).plus(roomVector), new Vector(100, 100, 0).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(0, 0, 0).plus(roomVector), new Vector(100, 0, 0).plus(roomVector), new Vector(100, 35, 0).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(0, 0, 0).plus(roomVector), new Vector(0, 35, 0).plus(roomVector), new Vector(100, 35, 0).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(0, 65, 0).plus(roomVector), new Vector(0, 100, 0).plus(roomVector), new Vector(100, 100, 0).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(0, 65, 0).plus(roomVector), new Vector(100, 65, 0).plus(roomVector), new Vector(100, 100, 0).plus(roomVector), ceilingColor));
//			triList.add(new Triangle(new Vector(0,0,0).plus(roomVector), new Vector(100,0,0).plus(roomVector), new Vector(0,100,0).plus(roomVector), ceilingColor));
//			triList.add(new Triangle(new Vector(0,100,0).plus(roomVector), new Vector(100,0,0).plus(roomVector), new Vector(100,100,0).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(30,30,0).plus(roomVector), new Vector(30,30+40*Math.cos(0),-40*Math.sin(0)).plus(roomVector), 
					new Vector(70,30+40*Math.cos(0),-40*Math.sin(0)).plus(roomVector), nextRoomDoorColorArray[5]));
			triList.add(new Triangle(new Vector(30,30,0).plus(roomVector), new Vector(70,30,0).plus(roomVector), 
					new Vector(70,30+40*Math.cos(0),-40*Math.sin(0)).plus(roomVector), nextRoomDoorColorArray[5]));
		}
				
		if (animationType != 5 || animationTimer > 20) { //up
			triList.add(new Triangle(new Vector(0, 0, 100).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), new Vector(35, 100, 100).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(0, 0, 100).plus(roomVector), new Vector(35, 0, 100).plus(roomVector), new Vector(35, 100, 100).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(65, 0, 100).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(65, 0, 100).plus(roomVector), new Vector(65, 100, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(0, 0, 100).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), new Vector(100, 35, 100).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(0, 0, 100).plus(roomVector), new Vector(0, 35, 100).plus(roomVector), new Vector(100, 35, 100).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(0, 65, 100).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(0, 65, 100).plus(roomVector), new Vector(100, 65, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), ceilingColor));
//			triList.add(new Triangle(new Vector(0,0,100).plus(roomVector), new Vector(100,0,100).plus(roomVector), new Vector(0,100,100).plus(roomVector), ceilingColor));
//			triList.add(new Triangle(new Vector(0,100,100).plus(roomVector), new Vector(100,0,100).plus(roomVector), new Vector(100,100,100).plus(roomVector), ceilingColor));
			triList.add(new Triangle(new Vector(30,30,100).plus(roomVector), new Vector(30,30+40*Math.cos(0),100-40*Math.sin(0)).plus(roomVector), 
					new Vector(70,30+40*Math.cos(0),100-40*Math.sin(0)).plus(roomVector), nextRoomDoorColorArray[4]));
			triList.add(new Triangle(new Vector(30,30,100).plus(roomVector), new Vector(70,30,100).plus(roomVector), 
					new Vector(70,30+40*Math.cos(0),100-40*Math.sin(0)).plus(roomVector), nextRoomDoorColorArray[4]));
		}
		
		if (playerDirection != Room.south || animationType == 4 || animationType == 5) { //north
			triList.add(new Triangle(new Vector(0, 100, 0).plus(roomVector), new Vector(35, 100, 0).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(0, 100, 100).plus(roomVector), new Vector(35, 100, 100).plus(roomVector), new Vector(35, 100, 0).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(65, 100, 0).plus(roomVector), new Vector(100, 100, 0).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(65, 100, 0).plus(roomVector), new Vector(65, 100, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(0, 100, 55).plus(roomVector), new Vector(100, 100, 55).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(0, 100, 55).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
//			triList.add(new Triangle(new Vector(0,100,0).plus(roomVector), new Vector(100,100,0).plus(roomVector), new Vector(0,100,100).plus(roomVector), roomColor));
//			triList.add(new Triangle(new Vector(0,100,100).plus(roomVector), new Vector(100,100,0).plus(roomVector), new Vector(100,100,100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(70-40*Math.cos(0),100+40*Math.sin(0),0).plus(roomVector), 
					new Vector(70,100,0).plus(roomVector), new Vector(70,100,60).plus(roomVector), nextRoomDoorColorArray[0]));
			triList.add(new Triangle(new Vector(70-40*Math.cos(0),100+40*Math.sin(0),0).plus(roomVector), 
					new Vector(70-40*Math.cos(0),100+40*Math.sin(0),60).plus(roomVector), new Vector(70,100,60).plus(roomVector), nextRoomDoorColorArray[0]));	
		}
		
		if (playerDirection != Room.north || animationType == 4 || animationType == 5) { //south
			triList.add(new Triangle(new Vector(0, 0, 0).plus(roomVector), new Vector(35, 0, 0).plus(roomVector), new Vector(0, 0, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(0, 0, 100).plus(roomVector), new Vector(35, 0, 100).plus(roomVector), new Vector(35, 0, 0).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(65, 0, 0).plus(roomVector), new Vector(100, 0, 0).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(65, 0, 0).plus(roomVector), new Vector(65, 0, 100).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(0, 0, 55).plus(roomVector), new Vector(100, 0, 55).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(0, 0, 55).plus(roomVector), new Vector(0, 0, 100).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), roomColor));
//			triList.add(new Triangle(new Vector(0,0,0).plus(roomVector), new Vector(100,0,0).plus(roomVector), new Vector(0,0,100).plus(roomVector), roomColor));
//			triList.add(new Triangle(new Vector(0,0,100).plus(roomVector), new Vector(100,0,0).plus(roomVector), new Vector(100,0,100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(30,0,0).plus(roomVector), new Vector(30+40*Math.cos(0),-40*Math.sin(0),0).plus(roomVector), 
					new Vector(30+40*Math.cos(0),-40*Math.sin(0),60).plus(roomVector), nextRoomDoorColorArray[2]));
			triList.add(new Triangle(new Vector(30,0,0).plus(roomVector), new Vector(30,0,60).plus(roomVector), 
					new Vector(30+40*Math.cos(0),-40*Math.sin(0),60).plus(roomVector), nextRoomDoorColorArray[2]));
		}
		
		if (playerDirection != Room.east || animationType == 4 || animationType == 5) { //west
			triList.add(new Triangle(new Vector(0, 0, 0).plus(roomVector), new Vector(0, 35, 0).plus(roomVector), new Vector(0, 0, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(0, 0, 100).plus(roomVector), new Vector(0, 35, 100).plus(roomVector), new Vector(0, 35, 0).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(0, 65, 0).plus(roomVector), new Vector(0, 100, 0).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(0, 65, 0).plus(roomVector), new Vector(0, 65, 100).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(0, 0, 55).plus(roomVector), new Vector(0, 100, 55).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(0, 0, 55).plus(roomVector), new Vector(0, 0, 100).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), roomColor));
//			triList.add(new Triangle(new Vector(0,0,0).plus(roomVector), new Vector(0,100,0).plus(roomVector), new Vector(0,0,100).plus(roomVector), roomColor));
//			triList.add(new Triangle(new Vector(0,0,100).plus(roomVector), new Vector(0,100,0).plus(roomVector), new Vector(0,100,100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(-40*Math.sin(0),70-40*Math.cos(0),0).plus(roomVector), 
					new Vector(0,70,0).plus(roomVector), new Vector(0,70,60).plus(roomVector), nextRoomDoorColorArray[3]));
			triList.add(new Triangle(new Vector(-40*Math.sin(0),70-40*Math.cos(0),0).plus(roomVector), 
					new Vector(-40*Math.sin(0),70-40*Math.cos(0),60).plus(roomVector), new Vector(0,70,60).plus(roomVector), nextRoomDoorColorArray[3]));
		}
		
		if (playerDirection != Room.west || animationType == 4 || animationType == 5) { //east
			triList.add(new Triangle(new Vector(100, 0, 0).plus(roomVector), new Vector(100, 35, 0).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(100, 0, 100).plus(roomVector), new Vector(100, 35, 100).plus(roomVector), new Vector(100, 35, 0).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(100, 65, 0).plus(roomVector), new Vector(100, 100, 0).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(100, 65, 0).plus(roomVector), new Vector(100, 65, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(100, 0, 55).plus(roomVector), new Vector(100, 100, 55).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(100, 0, 55).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
//			triList.add(new Triangle(new Vector(100,100,0).plus(roomVector), new Vector(100,0,0).plus(roomVector), new Vector(100,100,100).plus(roomVector), roomColor));
//			triList.add(new Triangle(new Vector(100,100,100).plus(roomVector), new Vector(100,0,0).plus(roomVector), new Vector(100,0,100).plus(roomVector), roomColor));
			triList.add(new Triangle(new Vector(100,30,0).plus(roomVector), new Vector(100+40*Math.sin(0),30+40*Math.cos(0),0).plus(roomVector), 
					new Vector(100+40*Math.sin(0),30+40*Math.cos(0),60).plus(roomVector), nextRoomDoorColorArray[1]));
			triList.add(new Triangle(new Vector(100,30,0).plus(roomVector), new Vector(100,30,60).plus(roomVector), 
					new Vector(100+40*Math.sin(0),30+40*Math.cos(0),60).plus(roomVector), nextRoomDoorColorArray[1]));
		}
		
		triList.add(new Triangle(new Vector(1,99,0).plus(roomVector), new Vector(2,99,0).plus(roomVector), new Vector(-1,101,101).plus(roomVector), Color.BLACK));
		triList.add(new Triangle(new Vector(-1,101,101).plus(roomVector), new Vector(2,99,0).plus(roomVector), new Vector(0,101,101).plus(roomVector), Color.BLACK));
		triList.add(new Triangle(new Vector(99,99,0).plus(roomVector), new Vector(100,99,0).plus(roomVector), new Vector(101,101,101).plus(roomVector), Color.BLACK));
		triList.add(new Triangle(new Vector(101,101,101).plus(roomVector), new Vector(100,99,0).plus(roomVector), new Vector(102,101,102).plus(roomVector), Color.BLACK));
		triList.add(new Triangle(new Vector(0,0,0).plus(roomVector), new Vector(0,1,0).plus(roomVector), new Vector(0,0,101).plus(roomVector), Color.BLACK));
		triList.add(new Triangle(new Vector(0,0,101).plus(roomVector), new Vector(0,1,0).plus(roomVector), new Vector(0,1,101).plus(roomVector), Color.BLACK));
		triList.add(new Triangle(new Vector(100,0,0).plus(roomVector), new Vector(100,1,0).plus(roomVector), new Vector(100,0,101).plus(roomVector), Color.BLACK));
		triList.add(new Triangle(new Vector(100,0,101).plus(roomVector), new Vector(100,1,0).plus(roomVector), new Vector(100,1,101).plus(roomVector), Color.BLACK));
		
		if (nextRoom.getPainting() != null) {
			triList.addAll(nextRoom.getPainting().getTriangles());
		}
	}


	private void setUpCurrentRoom () {
		currentRoom = maze.getRoom(playerPos);
		Color roomColor = currentRoom.getColor();
		Vector roomVector = new Vector(100*playerPos.getX(), 100*playerPos.getY(), 100*playerPos.getZ());
		
		for (int i = 0; i < 6; i++)
		if (currentRoom.getDoor(i))
			if (currentRoom.leadsOutside(i))
				doorColorArray[i] = new Color(189, 189, 189);
			else
				doorColorArray[i] = Color.BLACK;
		else if (0 <= i && i <= 3)
			doorColorArray[i] = roomColor;
		else
			doorColorArray[i] = ceilingColor;
		
		//down
		triList.add(new Triangle(new Vector(0, 0, 0).plus(roomVector), new Vector(0, 100, 0).plus(roomVector), new Vector(35, 100, 0).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(0, 0, 0).plus(roomVector), new Vector(35, 0, 0).plus(roomVector), new Vector(35, 100, 0).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(65, 0, 0).plus(roomVector), new Vector(100, 0, 0).plus(roomVector), new Vector(100, 100, 0).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(65, 0, 0).plus(roomVector), new Vector(65, 100, 0).plus(roomVector), new Vector(100, 100, 0).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(0, 0, 0).plus(roomVector), new Vector(100, 0, 0).plus(roomVector), new Vector(100, 35, 0).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(0, 0, 0).plus(roomVector), new Vector(0, 35, 0).plus(roomVector), new Vector(100, 35, 0).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(0, 65, 0).plus(roomVector), new Vector(0, 100, 0).plus(roomVector), new Vector(100, 100, 0).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(0, 65, 0).plus(roomVector), new Vector(100, 65, 0).plus(roomVector), new Vector(100, 100, 0).plus(roomVector), ceilingColor));
//		triList.add(new Triangle(new Vector(0,0,0).plus(roomVector), new Vector(100,0,0).plus(roomVector), new Vector(0,100,0).plus(roomVector), ceilingColor));
//		triList.add(new Triangle(new Vector(0,100,0).plus(roomVector), new Vector(100,0,0).plus(roomVector), new Vector(100,100,0).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(30,30,0).plus(roomVector), new Vector(30,30+40*Math.cos(doorAngleD),-40*Math.sin(doorAngleD)).plus(roomVector), 
				new Vector(70,30+40*Math.cos(doorAngleD),-40*Math.sin(doorAngleD)).plus(roomVector), doorColorArray[5]));
		triList.add(new Triangle(new Vector(30,30,0).plus(roomVector), new Vector(70,30,0).plus(roomVector), 
				new Vector(70,30+40*Math.cos(doorAngleD),-40*Math.sin(doorAngleD)).plus(roomVector), doorColorArray[5]));
		
		//up
		triList.add(new Triangle(new Vector(0, 0, 100).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), new Vector(35, 100, 100).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(0, 0, 100).plus(roomVector), new Vector(35, 0, 100).plus(roomVector), new Vector(35, 100, 100).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(65, 0, 100).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(65, 0, 100).plus(roomVector), new Vector(65, 100, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(0, 0, 100).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), new Vector(100, 35, 100).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(0, 0, 100).plus(roomVector), new Vector(0, 35, 100).plus(roomVector), new Vector(100, 35, 100).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(0, 65, 100).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(0, 65, 100).plus(roomVector), new Vector(100, 65, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), ceilingColor));
//		triList.add(new Triangle(new Vector(0,0,100).plus(roomVector), new Vector(100,0,100).plus(roomVector), new Vector(0,100,100).plus(roomVector), ceilingColor));
//		triList.add(new Triangle(new Vector(0,100,100).plus(roomVector), new Vector(100,0,100).plus(roomVector), new Vector(100,100,100).plus(roomVector), ceilingColor));
		triList.add(new Triangle(new Vector(30,30,100).plus(roomVector), new Vector(30,30+40*Math.cos(doorAngleU),100-40*Math.sin(doorAngleU)).plus(roomVector), 
				new Vector(70,30+40*Math.cos(doorAngleU),100-40*Math.sin(doorAngleU)).plus(roomVector), doorColorArray[4]));
		triList.add(new Triangle(new Vector(30,30,100).plus(roomVector), new Vector(70,30,100).plus(roomVector), 
				new Vector(70,30+40*Math.cos(doorAngleU),100-40*Math.sin(doorAngleU)).plus(roomVector), doorColorArray[4]));
		
		//north
		triList.add(new Triangle(new Vector(0, 100, 0).plus(roomVector), new Vector(35, 100, 0).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(0, 100, 100).plus(roomVector), new Vector(35, 100, 100).plus(roomVector), new Vector(35, 100, 0).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(69, 100, 0).plus(roomVector), new Vector(100, 100, 0).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(69, 100, 0).plus(roomVector), new Vector(69, 100, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(0, 100, 59).plus(roomVector), new Vector(100, 100, 59).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(0, 100, 59).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
//		triList.add(new Triangle(new Vector(0,100,0).plus(roomVector), new Vector(100,100,0).plus(roomVector), new Vector(0,100,100).plus(roomVector), roomColor));
//		triList.add(new Triangle(new Vector(0,100,100).plus(roomVector), new Vector(100,100,0).plus(roomVector), new Vector(100,100,100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(70-40*Math.cos(doorAngleN),100+40*Math.sin(doorAngleN),0).plus(roomVector), 
				new Vector(70,100,0).plus(roomVector), new Vector(70,100,60).plus(roomVector), doorColorArray[0]));
		triList.add(new Triangle(new Vector(70-40*Math.cos(doorAngleN),100+40*Math.sin(doorAngleN),0).plus(roomVector), 
				new Vector(70-40*Math.cos(doorAngleN),100+40*Math.sin(doorAngleN),60).plus(roomVector), new Vector(70,100,60).plus(roomVector), doorColorArray[0]));		
		
		//south
		triList.add(new Triangle(new Vector(0, 0, 0).plus(roomVector), new Vector(31, 0, 0).plus(roomVector), new Vector(0, 0, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(0, 0, 100).plus(roomVector), new Vector(31, 0, 100).plus(roomVector), new Vector(31, 0, 0).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(65, 0, 0).plus(roomVector), new Vector(100, 0, 0).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(65, 0, 0).plus(roomVector), new Vector(65, 0, 100).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(0, 0, 59).plus(roomVector), new Vector(100, 0, 59).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(0, 0, 59).plus(roomVector), new Vector(0, 0, 100).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), roomColor));
//		triList.add(new Triangle(new Vector(0,0,0).plus(roomVector), new Vector(100,0,0).plus(roomVector), new Vector(0,0,100).plus(roomVector), roomColor));
//		triList.add(new Triangle(new Vector(0,0,100).plus(roomVector), new Vector(100,0,0).plus(roomVector), new Vector(100,0,100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(30,0,0).plus(roomVector), new Vector(30+40*Math.cos(doorAngleS),-40*Math.sin(doorAngleS),0).plus(roomVector), 
				new Vector(30+40*Math.cos(doorAngleS),-40*Math.sin(doorAngleS),60).plus(roomVector), doorColorArray[2]));
		triList.add(new Triangle(new Vector(30,0,0).plus(roomVector), new Vector(30,0,60).plus(roomVector), 
				new Vector(30+40*Math.cos(doorAngleS),-40*Math.sin(doorAngleS),60).plus(roomVector), doorColorArray[2]));
		
		//west
		triList.add(new Triangle(new Vector(0, 0, 0).plus(roomVector), new Vector(0, 31, 0).plus(roomVector), new Vector(0, 0, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(0, 0, 100).plus(roomVector), new Vector(0, 31, 100).plus(roomVector), new Vector(0, 31, 0).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(0, 65, 0).plus(roomVector), new Vector(0, 100, 0).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(0, 65, 0).plus(roomVector), new Vector(0, 65, 100).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(0, 0, 59).plus(roomVector), new Vector(0, 100, 59).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(0, 0, 59).plus(roomVector), new Vector(0, 0, 100).plus(roomVector), new Vector(0, 100, 100).plus(roomVector), roomColor));
//		triList.add(new Triangle(new Vector(0,0,0).plus(roomVector), new Vector(0,100,0).plus(roomVector), new Vector(0,0,100).plus(roomVector), roomColor));
//		triList.add(new Triangle(new Vector(0,0,100).plus(roomVector), new Vector(0,100,0).plus(roomVector), new Vector(0,100,100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(-40*Math.sin(doorAngleW),70-40*Math.cos(doorAngleW),0).plus(roomVector), 
				new Vector(0,70,0).plus(roomVector), new Vector(0,70,60).plus(roomVector), doorColorArray[3]));
		triList.add(new Triangle(new Vector(-40*Math.sin(doorAngleW),70-40*Math.cos(doorAngleW),0).plus(roomVector), 
				new Vector(-40*Math.sin(doorAngleW),70-40*Math.cos(doorAngleW),60).plus(roomVector), new Vector(0,70,60).plus(roomVector), doorColorArray[3]));
		
		//east
		triList.add(new Triangle(new Vector(100, 0, 0).plus(roomVector), new Vector(100, 35, 0).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(100, 0, 100).plus(roomVector), new Vector(100, 35, 100).plus(roomVector), new Vector(100, 35, 0).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(100, 69, 0).plus(roomVector), new Vector(100, 100, 0).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(100, 69, 0).plus(roomVector), new Vector(100, 69, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(100, 0, 59).plus(roomVector), new Vector(100, 100, 59).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(100, 0, 59).plus(roomVector), new Vector(100, 0, 100).plus(roomVector), new Vector(100, 100, 100).plus(roomVector), roomColor));
//		triList.add(new Triangle(new Vector(100,100,0).plus(roomVector), new Vector(100,0,0).plus(roomVector), new Vector(100,100,100).plus(roomVector), roomColor));
//		triList.add(new Triangle(new Vector(100,100,100).plus(roomVector), new Vector(100,0,0).plus(roomVector), new Vector(100,0,100).plus(roomVector), roomColor));
		triList.add(new Triangle(new Vector(100,30,0).plus(roomVector), new Vector(100+40*Math.sin(doorAngleE),30+40*Math.cos(doorAngleE),0).plus(roomVector), 
				new Vector(100+40*Math.sin(doorAngleE),30+40*Math.cos(doorAngleE),60).plus(roomVector), doorColorArray[1]));
		triList.add(new Triangle(new Vector(100,30,0).plus(roomVector), new Vector(100,30,60).plus(roomVector), 
				new Vector(100+40*Math.sin(doorAngleE),30+40*Math.cos(doorAngleE),60).plus(roomVector), doorColorArray[1]));
	
		triList.add(new Triangle(new Vector(1,99,0).plus(roomVector), new Vector(2,99,0).plus(roomVector), new Vector(-1,101,101).plus(roomVector), Color.BLACK));
		triList.add(new Triangle(new Vector(-1,101,101).plus(roomVector), new Vector(2,99,0).plus(roomVector), new Vector(0,101,101).plus(roomVector), Color.BLACK));
		triList.add(new Triangle(new Vector(99,99,0).plus(roomVector), new Vector(100,99,0).plus(roomVector), new Vector(101,101,101).plus(roomVector), Color.BLACK));
		triList.add(new Triangle(new Vector(101,101,101).plus(roomVector), new Vector(100,99,0).plus(roomVector), new Vector(102,101,102).plus(roomVector), Color.BLACK));
		triList.add(new Triangle(new Vector(0,0,0).plus(roomVector), new Vector(0,1,0).plus(roomVector), new Vector(0,0,101).plus(roomVector), Color.BLACK));
		triList.add(new Triangle(new Vector(0,0,101).plus(roomVector), new Vector(0,1,0).plus(roomVector), new Vector(0,1,101).plus(roomVector), Color.BLACK));
		triList.add(new Triangle(new Vector(100,0,0).plus(roomVector), new Vector(100,1,0).plus(roomVector), new Vector(100,0,101).plus(roomVector), Color.BLACK));
		triList.add(new Triangle(new Vector(100,0,101).plus(roomVector), new Vector(100,1,0).plus(roomVector), new Vector(100,1,101).plus(roomVector), Color.BLACK));
		
		if (currentRoom.getPainting() != null) {
			triList.addAll(currentRoom.getPainting().getTriangles());
		}
	}

	private void setUpRooms () {
		//planeList = new ArrayList<Plane>();
		triList = new ArrayList<Triangle>();
		
		if (nextRoom != null)
			setUpNextRoom();
		if (nextRoom != null && animationTimer > 60) {
			setUpCurrentRoom();
			setUpNextRoom();
		} else {
			if (nextRoom != null)
				setUpNextRoom();
			setUpCurrentRoom();
		}
	}

	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		//System.out.println("----------");

		//Calculates the coordinate system of the screen-plane
		Vector a_0 = new Vector(Math.cos(phi)*Math.sin(theta), Math.cos(phi)*Math.cos(theta), Math.sin(phi));
		Vector b_0 = new Vector(Math.sin(theta+Math.PI/2), Math.cos(theta+Math.PI/2), 0);
		Vector c_0 = a_0.cross(b_0.clone()).scale(1/a_0.magnitude());
		
		//Fix all of the triangles that may lie
		//System.out.println("triList.size(): " + triList.size());
		for (int counter = 0; counter < triList.size(); counter++) {
			//System.out.println("counter: " + counter);
			Triangle tri = triList.get(counter);
			Color color = null;
			try {
				color = tri.getColor();
			} catch (Exception e) { color = Color.white; }
//			try {
//				color = tri.getColor();
//			} catch (Exception e) {
//				//System.out.println("triList.size(): " + triList.size() + "   counter: " + counter);
//				game.goToDifficultySelect();
//			}
			g.setColor(color);
			Vector[] verts = tri.getVerts();
			Vector vert1 = verts[0], vert2 = verts[1], vert3 = verts[2];
			double d_1 = Math.abs(((vert1.minus(cameraPos)).dot(screenPlaneRelPos))/screenPlaneRelPos.magnitude());
			double d_2 = Math.abs(((vert2.minus(cameraPos)).dot(screenPlaneRelPos))/screenPlaneRelPos.magnitude());
			double d_3 = Math.abs(((vert3.minus(cameraPos)).dot(screenPlaneRelPos))/screenPlaneRelPos.magnitude());
			//System.out.println("D: " + d_1 + " " + d_2 + " " + d_3);
			
			int numGoodVerts = 0;
			for (Vector pos : verts)
				if (((pos.minus(cameraPos)).dot(screenPlaneRelPos))/screenPlaneRelPos.magnitude() > 0.01)
					numGoodVerts++;
			if (numGoodVerts == 0) {
				try {
				triList.remove(counter);
				counter--;
				}catch (Exception e) {}
			} else if (numGoodVerts == 2) {
				if (((vert1.minus(cameraPos)).dot(screenPlaneRelPos))/screenPlaneRelPos.magnitude() < 0.01) { //vert 1 bad 
					Vector extraVec1 = vert2.plus( vert1.minus(vert2).scale(d_2/(d_1+d_2) - 0.01) );
					Vector extraVec2 = vert3.plus( vert1.minus(vert3).scale(d_3/(d_1+d_3) - 0.01) );
					triList.add(counter, new Triangle(vert2, extraVec1, extraVec2, color));
					triList.add(counter, new Triangle(vert2, vert3, extraVec2, color));
				} else if (((vert2.minus(cameraPos)).dot(screenPlaneRelPos))/screenPlaneRelPos.magnitude() < 0.01) { //vert 2 bad
					Vector extraVec1 = vert1.plus( vert2.minus(vert1).scale(d_1/(d_2+d_1) - 0.01) );
					Vector extraVec2 = vert3.plus( vert2.minus(vert3).scale(d_3/(d_2+d_3) - 0.01) );
					triList.add(counter, new Triangle(vert1, extraVec1, extraVec2, color));
					triList.add(counter, new Triangle(vert1, vert3, extraVec2, color));
				} else { //vert 3 bad
					Vector extraVec1 = vert1.plus( vert3.minus(vert1).scale(d_1/(d_3+d_1) - 0.01) );
					Vector extraVec2 = vert2.plus( vert3.minus(vert2).scale(d_2/(d_3+d_2) - 0.01) );
					triList.add(counter, new Triangle(vert1, extraVec1, extraVec2, color));
					triList.add(counter, new Triangle(vert1, vert2, extraVec2, color));
				}
				triList.remove(counter+2);
				counter++;
			} else if (numGoodVerts == 1) {
				if (((vert1.minus(cameraPos)).dot(screenPlaneRelPos))/screenPlaneRelPos.magnitude() > 0.01) { //vert 1 good
					Vector vec2Prime = vert1.plus( vert2.minus(vert1).scale(d_1/(d_2+d_1) - 0.01) );
					Vector vec3Prime = vert1.plus( vert3.minus(vert1).scale(d_1/(d_3+d_1) - 0.01) );
					triList.remove(counter);
					triList.add(counter, new Triangle(vert1, vec2Prime, vec3Prime, color));
				} else if (((vert2.minus(cameraPos)).dot(screenPlaneRelPos))/screenPlaneRelPos.magnitude() > 0.01) { //vert 2 good
					Vector vec1Prime = vert2.plus( vert1.minus(vert2).scale(d_2/(d_1+d_2) - 0.01) );
					Vector vec3Prime = vert2.plus( vert3.minus(vert2).scale(d_2/(d_3+d_2) - 0.01) );
					triList.remove(counter);
					triList.add(counter, new Triangle(vec1Prime, vert2, vec3Prime, color));
				} else { //vert 3 good
					Vector vec1Prime = vert3.plus( vert1.minus(vert3).scale(d_3/(d_1+d_3) - 0.01) );
					Vector vec2Prime = vert3.plus( vert2.minus(vert3).scale(d_3/(d_2+d_3) - 0.01) );
					triList.remove(counter);
					triList.add(counter, new Triangle(vec1Prime, vec2Prime, vert3, color));
				}
			}
		}
		
		//Draws all of the triangles
		for (int j = 0; j < triList.size(); j++) {
			Triangle tri = triList.get(j);
			//System.out.println(tri);
			Vector[] verts = tri.getVerts();
			int[] x = new int[3];
			int[] y = new int[3];
			for (int i = 0; i < 3; i++) {
				Vector p_0 = verts[i].minus( screenPlaneRelPos.clone().scale( screenPlaneRelPos.dot(verts[i].minus(a_0))/Math.pow(screenPlaneRelPos.magnitude(),2) ) );
				double d = ((verts[i].minus(cameraPos.plus(screenPlaneRelPos))).dot(screenPlaneRelPos))/screenPlaneRelPos.magnitude();
				x[i] = (int) (8*((p_0.minus(cameraPos.plus(a_0))).dot(b_0))/b_0.magnitude() * screenPlaneRelPos.magnitude() / (d + screenPlaneRelPos.magnitude())) + 400;
				y[i] = (int) (5.7*((p_0.minus(cameraPos.plus(a_0))).dot(c_0)/c_0.magnitude()) * screenPlaneRelPos.magnitude() / (d + screenPlaneRelPos.magnitude())) + 285;
			}
			//System.out.println("(" + (x[0]-400) + ", " + (y[0]-300) + ") (" + (x[1]-400) + ", " + (y[1]-300) + ") (" + (x[2]-400) + ", " + (y[2]-300) + ")");
			g.setColor(tri.getColor());
			g.fillPolygon(x, y, 3);
		}
	}
}