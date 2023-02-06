import java.awt.Color;
import java.util.*;
public class Room {
    public static final int north = 0;
    public static final int east = 1;
    public static final int south = 2;
    public static final int west = 3;
    public static final int up = 4;
    public static final int down = 5;
    private boolean[] doors;
    private Color wallColor;
    private boolean[] outside;
    private Painting painting;
    private static ArrayList<Color> usedColors;
    public Room () {
        this(null, null);
    }
    public Room (boolean[] doors, boolean[] outside) {
    	doors = new boolean[6];
    	outside = new boolean[6];
    	painting = null;
        if (doors != null)
            this.doors = doors;
        if (outside != null)
            this.outside = outside;
        if (usedColors == null) {
            usedColors = new ArrayList<Color>();
        }
        Random rand = new Random();
        int r=0;
        int g=0;
        int b=0;
        while (r <= 128 || g <= 128 || b <= 128 || colorUsed(new Color(r, g, b))) {
            r = rand.nextInt(255);
            g = rand.nextInt(255);
            b = rand.nextInt(255);
        }
        wallColor = new Color(r, g, b);
        usedColors.add(wallColor);
    }
    public void setDoor(int direction, boolean isOpen) {
        doors[direction] = isOpen;
    }
    public boolean getDoor(int orientation) {
        return doors[orientation];
    }
    public Color getColor() {
        return wallColor;
    }
    private boolean colorUsed(Color color) {
        for (Color usedColor : usedColors)
        	if (color.equals(usedColor))
        		return true;
        return false;
    }
    public static void resetColors() {
        usedColors.clear();
    }
    public boolean leadsOutside(int orientation) {
        return doors[orientation] && outside[orientation];
    }
    public void setLeadsOutside (int orientation) {
    	outside[orientation] = true;
    }
    public void setPainting (Painting painting) {
    	this.painting = painting;
    }
    public Painting getPainting () {
    	return painting;
    }
}