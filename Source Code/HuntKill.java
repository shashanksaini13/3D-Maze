import java.util.*;

public class HuntKill {
	private ArrayList<int[]> roomsVisited;
	private int numRooms;
	private Room[][][] rooms;
//	public static void main(String[] args) {
//		HuntKill test = new HuntKill(4);
//		test.generateMaze();
//	}

	public HuntKill(int roomNum) {
		roomsVisited=new ArrayList<int[]>();
		numRooms = roomNum;

	}

	public Room[][][] generateMaze() {
		rooms = new Room[numRooms][numRooms][numRooms];
		for (int i = 0; i < numRooms; i++) {
			for (int j = 0; j < numRooms; j++) {
				for (int k = 0; k < numRooms; k++) {
					rooms[i][j][k] = new Room();
				}
			}
		}
		int[] firstRoom = { (int) (Math.random() * numRooms), (int) (Math.random() * numRooms),
				(int) (Math.random() * numRooms) };
		//System.out.println("firstRoom: "+firstRoom[0]+" "+firstRoom[1]+" "+firstRoom[2]);
		roomsVisited.add(firstRoom);
		boolean generationComplete = false;
		while (!generationComplete) {
			boolean walkComplete = false;
			while (!walkComplete) {
				int[] currentWalk = walk();

				if (currentWalk == null) {
					//System.out.println("Walk complete");
					walkComplete = true;
				}
				else {
					int[] walkCoord = { currentWalk[0], currentWalk[1], currentWalk[2] };
					//System.out.println("walk: "+currentWalk[0]+" "+currentWalk[1]+" "+currentWalk[2]);
					roomsVisited.add(walkCoord);
				}
			}
			if (roomsVisited.size() < Math.pow(numRooms, 3)) {
				//System.out.println("hunt in process");
				int[] currentHunt = hunt();
				if (currentHunt == null) {
					//System.out.println("Maze generation complete.");
					generationComplete = true;
				} else {
					//System.out.println("hunt: " + currentHunt[0] + " " + currentHunt[1] + " " + currentHunt[2]);
					roomsVisited.add(currentHunt);
				}
			} else {
				//System.out.println("Maze generation complete.");
				generationComplete = true;
			}
		}
		//System.out.println("All coords:");
		for (int i = 0; i < roomsVisited.size(); i++) {
			//System.out.println(
					//i + ": " + roomsVisited.get(i)[0] + " " + roomsVisited.get(i)[1] + " " + roomsVisited.get(i)[2]);
		}
		//System.out.println("Size: " + roomsVisited.size());

		if (Maze.shortestPath(rooms)<12) {
			roomsVisited=new ArrayList<int[]>();
			return generateMaze();
		}
		return rooms;
	}

	private int[] hunt() {
		boolean tester = true;
		int[] returnval = new int[3];
		for (int i = 0; i < numRooms; i++) {
			for (int j = 0; j < numRooms; j++) {
				for (int l = 0; l < numRooms; l++) {
					tester = true;
					for (int k = 0; k < roomsVisited.size(); k++) {
						int[] test = new int[] { i, j, l };
						if (((roomsVisited.get(k)[0] == test[0]) && (roomsVisited.get(k)[1] == test[1])
								&& (roomsVisited.get(k)[2] == test[2]))) {
							tester = false;
							break;
						}
					}
					if (tester) {
						returnval = new int[] { i, j, l };
						for (int k = 0; k < roomsVisited.size(); k++) {
							ArrayList<Integer> directions = new ArrayList<Integer>();
							for (int m = 0; m < 6; m++) {
								directions.add(m);
							}
							directions=randomize(directions);
							while (directions.size() > 0) {
								if (directions.get(0)==0 && roomsVisited.get(k)[0] == returnval[0] + 1 && (roomsVisited.get(k)[1] == returnval[1]
										&& roomsVisited.get(k)[2] == returnval[2])) {
									rooms[returnval[0]][returnval[1]][returnval[2]].setDoor(Room.east, true);
									rooms[roomsVisited.get(k)[0]][roomsVisited.get(k)[1]][roomsVisited.get(k)[2]]
											.setDoor(Room.west,true);
									return returnval;
								}
								else if (directions.get(0)==1 && roomsVisited.get(k)[0] == returnval[0] - 1 && (roomsVisited.get(k)[1] == returnval[1]
										&& roomsVisited.get(k)[2] == returnval[2])) {
									rooms[returnval[0]][returnval[1]][returnval[2]].setDoor(Room.west, true);
									rooms[roomsVisited.get(k)[0]][roomsVisited.get(k)[1]][roomsVisited.get(k)[2]]
											.setDoor(Room.east,true);
									return returnval;
								}
								else if (directions.get(0)==2 && roomsVisited.get(k)[1] == returnval[1] + 1 && (roomsVisited.get(k)[0] == returnval[0]
										&& roomsVisited.get(k)[2] == returnval[2])) {
									rooms[returnval[0]][returnval[1]][returnval[2]].setDoor(Room.north, true);
									rooms[roomsVisited.get(k)[0]][roomsVisited.get(k)[1]][roomsVisited.get(k)[2]]
											.setDoor(Room.south,true);
									return returnval;
								}
								else if (directions.get(0)==3 && roomsVisited.get(k)[1] == returnval[1] - 1 && (roomsVisited.get(k)[0] == returnval[0]
										&& roomsVisited.get(k)[2] == returnval[2])) {
									rooms[returnval[0]][returnval[1]][returnval[2]].setDoor(Room.south, true);
									rooms[roomsVisited.get(k)[0]][roomsVisited.get(k)[1]][roomsVisited.get(k)[2]]
											.setDoor(Room.north,true);
									return returnval;
								}
								else if (directions.get(0)==4 && roomsVisited.get(k)[2] == returnval[2] + 1 && (roomsVisited.get(k)[1] == returnval[1]
										&& roomsVisited.get(k)[0] == returnval[0])) {
									rooms[returnval[0]][returnval[1]][returnval[2]].setDoor(Room.up, true);
									rooms[roomsVisited.get(k)[0]][roomsVisited.get(k)[1]][roomsVisited.get(k)[2]]
											.setDoor(Room.down,true);
									return returnval;
								}
								else if (directions.get(0)==5 && roomsVisited.get(k)[2] == returnval[2] - 1 && (roomsVisited.get(k)[1] == returnval[1]
										&& roomsVisited.get(k)[0] == returnval[0])) {
									rooms[returnval[0]][returnval[1]][returnval[2]].setDoor(Room.down, true);
									rooms[roomsVisited.get(k)[0]][roomsVisited.get(k)[1]][roomsVisited.get(k)[2]]
											.setDoor(Room.up,true);
									return returnval;
								}
								directions.remove(0);
							}
						}
					}
				}
			}
		}
		return null;
	}
	public int[] walk() {
		ArrayList<Integer> directions = new ArrayList<Integer>();
		for (int i = 0; i < 6; i++) {
			directions.add(i);
		}
		directions=randomize(directions);
		while (directions.size() > 0) {
			int cellDirection = (int) (Math.random() * directions.size()) + 1;
			if (directions.get(cellDirection - 1) == 0) {
				if (roomsVisited.get(roomsVisited.size() - 1)[1] - 1 >= 0) {
					ArrayList<Integer> xIndexes = new ArrayList<Integer>();
					for (int i = 0; i < roomsVisited.size() - 1; i++) {
						if (roomsVisited.get(i)[0] == roomsVisited.get(roomsVisited.size() - 1)[0])
							xIndexes.add(i);
					}
					if (xIndexes.size() > 0) {
						ArrayList<Integer> yIndexes = new ArrayList<Integer>();
						for (int i = 0; i < roomsVisited.size() - 1; i++) {
							if (roomsVisited.get(i)[1] == roomsVisited.get(roomsVisited.size() - 1)[1] - 1)
								yIndexes.add(i);
						}
						if (yIndexes.size() > 0) {
							ArrayList<Integer> zIndexes = new ArrayList<Integer>();
							for (int i = 0; i < roomsVisited.size() - 1; i++) {
								if (roomsVisited.get(i)[2] == roomsVisited.get(roomsVisited.size() - 1)[2])
									zIndexes.add(i);
							}
							boolean sameXY = false;
							for (int i = 0; i < xIndexes.size(); i++) {
								for (int j = 0; j < yIndexes.size(); j++) {
									if (xIndexes.get(i) == yIndexes.get(j))
										sameXY = true;
								}
							}
							boolean sameYZ = false;
							for (int i = 0; i < yIndexes.size(); i++) {
								for (int j = 0; j < zIndexes.size(); j++) {
									if (yIndexes.get(i) == zIndexes.get(j))
										sameYZ = true;
								}
							}
							if (sameXY && sameYZ) {
								directions.remove(directions.indexOf(0));
							} else {
								int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0],
										roomsVisited.get(roomsVisited.size() - 1)[1] - 1,
										roomsVisited.get(roomsVisited.size() - 1)[2] };
								rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.north, true);
								rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
								                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
								                                                    		.setDoor(Room.south, true);
								return returnCell;
							}
						} else {
							int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0],
									roomsVisited.get(roomsVisited.size() - 1)[1] - 1,
									roomsVisited.get(roomsVisited.size() - 1)[2] };
							rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.north, true);
							rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
							                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
							                                                    		.setDoor(Room.south, true);
							return returnCell;
						}
					} else {
						int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0],
								roomsVisited.get(roomsVisited.size() - 1)[1] - 1,
								roomsVisited.get(roomsVisited.size() - 1)[2] };
						rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.north, true);
						rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
						                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
						                                                    		.setDoor(Room.south, true);
						return returnCell;
					}
				} else
					directions.remove(directions.indexOf(0));
			} else if (directions.get(cellDirection - 1) == 1) {
				if (roomsVisited.get(roomsVisited.size() - 1)[0] + 1 < numRooms) {
					ArrayList<Integer> xIndexes = new ArrayList<Integer>();
					for (int i = 0; i < roomsVisited.size() - 1; i++) {
						if (roomsVisited.get(i)[0] == roomsVisited.get(roomsVisited.size() - 1)[0] + 1)
							xIndexes.add(i);
					}
					if (xIndexes.size() > 0) {
						ArrayList<Integer> yIndexes = new ArrayList<Integer>();
						for (int i = 0; i < roomsVisited.size() - 1; i++) {
							if (roomsVisited.get(i)[1] == roomsVisited.get(roomsVisited.size() - 1)[1])
								yIndexes.add(i);
						}
						if (yIndexes.size() > 0) {
							ArrayList<Integer> zIndexes = new ArrayList<Integer>();
							for (int i = 0; i < roomsVisited.size() - 1; i++) {
								if (roomsVisited.get(i)[2] == roomsVisited.get(roomsVisited.size() - 1)[2])
									zIndexes.add(i);
							}
							boolean sameXY = false;
							for (int i = 0; i < xIndexes.size(); i++) {
								for (int j = 0; j < yIndexes.size(); j++) {
									if (xIndexes.get(i) == yIndexes.get(j))
										sameXY = true;
								}
							}
							boolean sameYZ = false;
							for (int i = 0; i < yIndexes.size(); i++) {
								for (int j = 0; j < zIndexes.size(); j++) {
									if (yIndexes.get(i) == zIndexes.get(j))
										sameYZ = true;
								}
							}
							if (sameXY && sameYZ) {
								directions.remove(directions.indexOf(1));
							} else {
								int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0] + 1,
										roomsVisited.get(roomsVisited.size() - 1)[1],
										roomsVisited.get(roomsVisited.size() - 1)[2] };
								rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.west, true);
								rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
								                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
								                                                    		.setDoor(Room.east, true);
								return returnCell;
							}
						} else {
							int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0] + 1,
									roomsVisited.get(roomsVisited.size() - 1)[1],
									roomsVisited.get(roomsVisited.size() - 1)[2] };
							rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.west, true);
							rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
							                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
							                                                    		.setDoor(Room.east, true);
							return returnCell;
						}
					} else {
						int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0] + 1,
								roomsVisited.get(roomsVisited.size() - 1)[1],
								roomsVisited.get(roomsVisited.size() - 1)[2] };
						rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.west, true);
						rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
						                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
						                                                    		.setDoor(Room.east, true);
						return returnCell;
					}
				} else {
					directions.remove(directions.indexOf(1));
				}

			} else if (directions.get(cellDirection - 1) == 2) {
				if (roomsVisited.get(roomsVisited.size() - 1)[1] + 1 < numRooms) {
					ArrayList<Integer> xIndexes = new ArrayList<Integer>();
					for (int i = 0; i < roomsVisited.size() - 1; i++) {
						if (roomsVisited.get(i)[0] == roomsVisited.get(roomsVisited.size() - 1)[0])
							xIndexes.add(i);
					}
					if (xIndexes.size() > 0) {
						ArrayList<Integer> yIndexes = new ArrayList<Integer>();
						for (int i = 0; i < roomsVisited.size() - 1; i++) {
							if (roomsVisited.get(i)[1] == roomsVisited.get(roomsVisited.size() - 1)[1] + 1)
								yIndexes.add(i);
						}
						if (yIndexes.size() > 0) {
							ArrayList<Integer> zIndexes = new ArrayList<Integer>();
							for (int i = 0; i < roomsVisited.size() - 1; i++) {
								if (roomsVisited.get(i)[2] == roomsVisited.get(roomsVisited.size() - 1)[2])
									zIndexes.add(i);
							}
							boolean sameXY = false;
							for (int i = 0; i < xIndexes.size(); i++) {
								for (int j = 0; j < yIndexes.size(); j++) {
									if (xIndexes.get(i) == yIndexes.get(j))
										sameXY = true;
								}
							}
							boolean sameYZ = false;
							for (int i = 0; i < yIndexes.size(); i++) {
								for (int j = 0; j < zIndexes.size(); j++) {
									if (yIndexes.get(i) == zIndexes.get(j))
										sameYZ = true;
								}
							}
							if (sameXY && sameYZ) {
								directions.remove(directions.indexOf(2));
							} else {
								int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0],
										roomsVisited.get(roomsVisited.size() - 1)[1] + 1,
										roomsVisited.get(roomsVisited.size() - 1)[2] };
								rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.south, true);
								rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
								                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
								                                                    		.setDoor(Room.north, true);
								return returnCell;
							}
						} else {
							int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0],
									roomsVisited.get(roomsVisited.size() - 1)[1] + 1,
									roomsVisited.get(roomsVisited.size() - 1)[2] };
							rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.south, true);
							rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
							                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
							                                                    		.setDoor(Room.north, true);
							return returnCell;
						}
					} else {
						int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0],
								roomsVisited.get(roomsVisited.size() - 1)[1] + 1,
								roomsVisited.get(roomsVisited.size() - 1)[2] };
						rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.south, true);
						rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
						                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
						                                                    		.setDoor(Room.north, true);
						return returnCell;
					}
				} else {
					directions.remove(directions.indexOf(2));

				}
			} else if (directions.get(cellDirection - 1) == 3) {
				if (roomsVisited.get(roomsVisited.size() - 1)[0] - 1 >= 0) {
					ArrayList<Integer> xIndexes = new ArrayList<Integer>();
					for (int i = 0; i < roomsVisited.size() - 1; i++) {
						if (roomsVisited.get(i)[0] == roomsVisited.get(roomsVisited.size() - 1)[0] - 1)
							xIndexes.add(i);
					}
					if (xIndexes.size() > 0) {
						ArrayList<Integer> yIndexes = new ArrayList<Integer>();
						for (int i = 0; i < roomsVisited.size() - 1; i++) {
							if (roomsVisited.get(i)[1] == roomsVisited.get(roomsVisited.size() - 1)[1])
								yIndexes.add(i);
						}
						if (yIndexes.size() > 0) {
							ArrayList<Integer> zIndexes = new ArrayList<Integer>();
							for (int i = 0; i < roomsVisited.size() - 1; i++) {
								if (roomsVisited.get(i)[2] == roomsVisited.get(roomsVisited.size() - 1)[2])
									zIndexes.add(i);
							}
							boolean sameXY = false;
							for (int i = 0; i < xIndexes.size(); i++) {
								for (int j = 0; j < yIndexes.size(); j++) {
									if (xIndexes.get(i) == yIndexes.get(j))
										sameXY = true;
								}
							}
							boolean sameYZ = false;
							for (int i = 0; i < yIndexes.size(); i++) {
								for (int j = 0; j < zIndexes.size(); j++) {
									if (yIndexes.get(i) == zIndexes.get(j))
										sameYZ = true;
								}
							}
							if (sameXY && sameYZ) {
								directions.remove(directions.indexOf(3));
							} else {
								int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0] - 1,
										roomsVisited.get(roomsVisited.size() - 1)[1],
										roomsVisited.get(roomsVisited.size() - 1)[2] };
								rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.east, true);
								rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
								                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
								                                                    		.setDoor(Room.west, true);
								return returnCell;
							}
						} else {
							int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0] - 1,
									roomsVisited.get(roomsVisited.size() - 1)[1],
									roomsVisited.get(roomsVisited.size() - 1)[2] };
							rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.east, true);
							rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
							                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
							                                                    		.setDoor(Room.west, true);
							return returnCell;
						}
					} else {
						int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0] - 1,
								roomsVisited.get(roomsVisited.size() - 1)[1],
								roomsVisited.get(roomsVisited.size() - 1)[2] };
						rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.east, true);
						rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
						                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
						                                                    		.setDoor(Room.west, true);
						return returnCell;
					}
				} else {
					directions.remove(directions.indexOf(3));
				}
			} else if (directions.get(cellDirection - 1) == 4) {
				if (roomsVisited.get(roomsVisited.size() - 1)[2] + 1 < numRooms) {
					ArrayList<Integer> xIndexes = new ArrayList<Integer>();
					for (int i = 0; i < roomsVisited.size() - 1; i++) {
						if (roomsVisited.get(i)[0] == roomsVisited.get(roomsVisited.size() - 1)[0])
							xIndexes.add(i);
					}
					if (xIndexes.size() > 0) {
						ArrayList<Integer> yIndexes = new ArrayList<Integer>();
						for (int i = 0; i < roomsVisited.size() - 1; i++) {
							if (roomsVisited.get(i)[1] == roomsVisited.get(roomsVisited.size() - 1)[1])
								yIndexes.add(i);
						}
						if (yIndexes.size() > 0) {
							ArrayList<Integer> zIndexes = new ArrayList<Integer>();
							for (int i = 0; i < roomsVisited.size() - 1; i++) {
								if (roomsVisited.get(i)[2] == roomsVisited.get(roomsVisited.size() - 1)[2] + 1)
									zIndexes.add(i);
							}
							boolean sameXY = false;
							for (int i = 0; i < xIndexes.size(); i++) {
								for (int j = 0; j < yIndexes.size(); j++) {
									if (xIndexes.get(i) == yIndexes.get(j))
										sameXY = true;
								}
							}
							boolean sameYZ = false;
							for (int i = 0; i < yIndexes.size(); i++) {
								for (int j = 0; j < zIndexes.size(); j++) {
									if (yIndexes.get(i) == zIndexes.get(j))
										sameYZ = true;
								}
							}
							if (sameXY && sameYZ) {
								directions.remove(directions.indexOf(4));
							} else {
								int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0],
										roomsVisited.get(roomsVisited.size() - 1)[1],
										roomsVisited.get(roomsVisited.size() - 1)[2] + 1 };
								rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.down, true);
								rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
								                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
								                                                    		.setDoor(Room.up, true);
								return returnCell;
							}
						} else {
							int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0],
									roomsVisited.get(roomsVisited.size() - 1)[1],
									roomsVisited.get(roomsVisited.size() - 1)[2] + 1 };
							rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.down, true);
							rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
							                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
							                                                    		.setDoor(Room.up, true);
							return returnCell;
						}
					} else {
						int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0],
								roomsVisited.get(roomsVisited.size() - 1)[1],
								roomsVisited.get(roomsVisited.size() - 1)[2] + 1 };
						rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.down, true);
						rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
						                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
						                                                    		.setDoor(Room.up, true);
						return returnCell;
					}
				} else {
					directions.remove(directions.indexOf(4));
				}
			} else if (directions.get(cellDirection - 1) == 5) {
				if (roomsVisited.get(roomsVisited.size() - 1)[2] - 1 >= 0) {
					ArrayList<Integer> xIndexes = new ArrayList<Integer>();
					for (int i = 0; i < roomsVisited.size() - 1; i++) {
						if (roomsVisited.get(i)[0] == roomsVisited.get(roomsVisited.size() - 1)[0])
							xIndexes.add(i);
					}
					if (xIndexes.size() > 0) {
						ArrayList<Integer> yIndexes = new ArrayList<Integer>();
						for (int i = 0; i < roomsVisited.size() - 1; i++) {
							if (roomsVisited.get(i)[1] == roomsVisited.get(roomsVisited.size() - 1)[1])
								yIndexes.add(i);
						}
						if (yIndexes.size() > 0) {
							ArrayList<Integer> zIndexes = new ArrayList<Integer>();
							for (int i = 0; i < roomsVisited.size() - 1; i++) {
								if (roomsVisited.get(i)[2] == roomsVisited.get(roomsVisited.size() - 1)[2] - 1)
									zIndexes.add(i);
							}
							boolean sameXY = false;
							for (int i = 0; i < xIndexes.size(); i++) {
								for (int j = 0; j < yIndexes.size(); j++) {
									if (xIndexes.get(i) == yIndexes.get(j))
										sameXY = true;
								}
							}
							boolean sameYZ = false;
							for (int i = 0; i < yIndexes.size(); i++) {
								for (int j = 0; j < zIndexes.size(); j++) {
									if (yIndexes.get(i) == zIndexes.get(j))
										sameYZ = true;
								}
							}
							if (sameXY && sameYZ) {
								directions.remove(directions.indexOf(5));
							} else {
								int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0],
										roomsVisited.get(roomsVisited.size() - 1)[1],
										roomsVisited.get(roomsVisited.size() - 1)[2] - 1 };
								rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.up, true);
								rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
								                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
								                                                    		.setDoor(Room.down, true);
								return returnCell;
							}
						} else {
							int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0],
									roomsVisited.get(roomsVisited.size() - 1)[1],
									roomsVisited.get(roomsVisited.size() - 1)[2] - 1 };
							rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.up, true);
							rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
							                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
							                                                    		.setDoor(Room.down, true);
							return returnCell;
						}
					} else {
						int[] returnCell = { roomsVisited.get(roomsVisited.size() - 1)[0],
								roomsVisited.get(roomsVisited.size() - 1)[1],
								roomsVisited.get(roomsVisited.size() - 1)[2] - 1 };
						rooms[returnCell[0]][returnCell[1]][returnCell[2]].setDoor(Room.up, true);
						rooms[roomsVisited.get(roomsVisited.size() - 1)[0]][roomsVisited
						                                                    .get(roomsVisited.size() - 1)[1]][roomsVisited.get(roomsVisited.size() - 1)[2]]
						                                                    		.setDoor(Room.down, true);
						return returnCell;
					}
				} else {
					directions.remove(directions.indexOf(5));
				}
			}

		}
		return null;
	}
	private ArrayList<Integer> randomize(ArrayList<Integer> arr){
		Random rand = new Random();
		for (int i=0; i<arr.size(); i++) {
			int randPos = rand.nextInt(arr.size());
			int temp = arr.get(i);
			arr.set(i,arr.get(randPos));
			arr.set(randPos, temp);
		}
		return arr;
	}
}