import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;

import acm.graphics.*;
import acm.program.*;

public class GraphicsGame extends GraphicsProgram {
	/**
	 * Here are all of the constants
	 */
	public static final int PROGRAM_WIDTH = 500;
	public static final int PROGRAM_HEIGHT = 500;
	public static final String lABEL_FONT = "Arial-Bold-22";
	public static final String EXIT_SIGN = "EXIT";
	public static final String IMG_FILENAME_PATH = "images/";
	public static final String IMG_EXTENSION = ".png";
	public static final String VERTICAL_IMG_FILENAME = "_vert";
	public static final int NUM_ROWS = 6;
	public static final int NUM_COLS = 6;
	Level level;		//create the level
	GObject toDrag;		//make an object that is being dragged by mouse
	Vehicle clicked;	//the vehicle thats being clicked on
	double lastX;		//variable to keep track of the x-coordinate when the mouse was first pressed
	double lastY;		//variable to keep track of the y-coordinate when the mouse was first pressed
	double newX;		//variable to keep track of x coord. with the new position of mouse
	double newY;		//variable to keep track of y coord. with the new position of mouse
	

	//TODO declare your instance variables here
	
	public void init() {
		setSize(PROGRAM_WIDTH, PROGRAM_HEIGHT);
		
		//initialize the level
		level = new Level(NUM_ROWS, NUM_COLS);
		//actually setup the level with the vehicles on it
		level.setupLevel(NUM_ROWS, NUM_COLS);
	}

	public void run() 
	{
		//TODO write this part, which is like your main function
		drawLevel();		//draw the level: grid lines, vehicles, labels
		//add mouse listeners. yay -_-
		addMouseListeners();
	}

	private void drawLevel() 
	{
		//TODO write the code to draw the entire level, which should
		//mostly be calls to some of your helper functions.
		//make the number of moves label in the top left corner
		GLabel numMoves = new GLabel(Integer.toString(level.getNumMoves()), spaceWidth()*0.5, spaceHeight()*0.5);
		//set the font of label above
		numMoves.setFont(lABEL_FONT);
		//add the label
		add(numMoves);
		//draw the grid lines
		drawGridLines();
		//draw the winning tile (label)
		drawWinningTile();
		//draw the vehicles
		drawCars();
		
	}

	/**
	 * This should draw the label EXIT and add it to the space that 
	 * represents the winning tile.
	 */
	private void drawWinningTile() 
	{
		//the winning space is (1,5)
		GLabel exit = new GLabel(EXIT_SIGN, spaceWidth()*(NUM_COLS-1), spaceHeight()*(2));
		exit.setFont(lABEL_FONT);	//set the font and add the label
		add(exit);
	}

	/**
	 * draw the lines of the grid.  Test this out and make sure you
	 * have it working first.  Should draw the number of grids
	 * based on the number of rows and columsn in Level
	 */
	private void drawGridLines() 
	{
		//make the grid lines that go vertically
		for(int i = 0; i < NUM_COLS; i++)
		{
			GLine g1 = new GLine(i*spaceWidth(), 0, i*spaceWidth(), PROGRAM_HEIGHT);
			add(g1);
		}
		
		//make the grid lines that go horizontally
		for (int j = 0; j < NUM_ROWS; j++)
		{
			GLine g2 = new GLine(0, j*spaceHeight(), PROGRAM_WIDTH, j*spaceHeight());
			add(g2);
		}
	}

	/**
	 * Maybe given a list of all the cars, you can go through them
	 * and call drawCar on each?
	 */
	private void drawCars() 
	{
		//loop through the array list to get all the vehicles on the board
		ArrayList<Vehicle> temp = new ArrayList<Vehicle>();
		temp = level.getBoard().getVehicleOnBoard();
		
		//loop and actually draw the car omg magic
		for (int i = 0; i < temp.size(); i++)
		{
			drawCar(temp.get(i));
		}
	}

	/**
	 * Given a vehicle object, which we will call v, use the information
	 * from that vehicle to then create a GImage and add it to the screen.
	 * Make sure to use the constants for the image path ("/images"), the
	 * extension ".png" and the additional suffix to the filename if the
	 * object is vertical when creating your GImage.  Also make sure to
	 * set the images size according to the size of your spaces
	 * 
	 * @param v the Vehicle object to be drawn
	 */
	private void drawCar(Vehicle v) 
	{
		//TODO implement drawCar
		GImage v1;
		//if the vehicle is vertical
		if (v.isVert())
		{
			//make the image and set the size
			v1 = new GImage(IMG_FILENAME_PATH + v.getVehicleType() + VERTICAL_IMG_FILENAME + IMG_EXTENSION, spaceWidth()*v.getStart().getCol(), spaceHeight()*v.getStart().getRow());
			v1.setSize(spaceWidth(), spaceHeight()*v.getSpace());
		}
		
		//if the vehicle is horizontal
		else
		{
			//make image and set size
			v1 = new GImage(IMG_FILENAME_PATH + v.getVehicleType() + IMG_EXTENSION, spaceWidth()*v.getStart().getCol(), spaceHeight()*v.getStart().getRow());
			v1.setSize(spaceWidth()*v.getSpace(), spaceHeight());
		}
		
		//add vehicle
		add(v1);
	}

	//TODO implement the mouse listeners here
	@Override
	public void mousePressed(MouseEvent e)
	{
		//if what was clicked wasn't an image, then dont do anything
		if(!(getElementAt(e.getX(), e.getY()) instanceof GImage))
		{
			return;
		}
		
		//set the element being clicked to the object that needs to be dragged
		
		lastX = e.getX();
		lastY = e.getY();
		toDrag = getElementAt(lastX, lastY);
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		//again check if the thing clicked is actually an image. if not, go away
		if (!(getElementAt(lastX, lastY) instanceof GImage))
		{
			return;
		}
		
		//get the vehicle thats being clicked on and set it to the vehicle thats being clicked
		clicked = getVehicleFromXY(lastX, lastY);
		
		//i dont want any null pointer exceptions
		if (clicked != null && toDrag != null)
		{
			//if the vehicle is vertical, set the location, but dont change the x coord.
			if(clicked.isVert())
				toDrag.setLocation(clicked.getStart().getCol()*spaceWidth(), e.getY() - (toDrag.getSize().getHeight()));
		
			//else if the vehicle is horizontal, set the location, but dont change the y coord.
			else
				toDrag.setLocation(e.getX() - (toDrag.getSize().getWidth()), clicked.getStart().getRow()*spaceHeight());
		}	
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		//as long as nothing is null. bc PLEASE NO NULL POINTER EXCEPTIONS
		if (clicked != null)
		{
			//put new x and y coord in variable
			newX = e.getX();
			newY = e.getY();
			//calculate the number of spaces moved
			int spaces = calculateSpacesMoved();
			//if can't move, snap back to original location
			if(!level.moveNumSpaces(clicked.getStart(), spaces))
			{
				toDrag.setLocation(lastX, lastY);
			}
			
			//move numspaces is already called.. so if it can move, then set the location to its new place
			toDrag.setLocation(clicked.getStart().getCol()*spaceHeight(), clicked.getStart().getRow()*spaceWidth());
			
			//if the car is on the winning space
			if (level.passedLevel())
			{
				removeAll();
				GLabel winM = new GLabel("CONGRATULATIONS YOU WON!", PROGRAM_WIDTH/6, PROGRAM_HEIGHT/6);
				winM.setFont(lABEL_FONT);
				add(winM);
			}
			
			level.setNumMoves(level.getNumMoves()+1);
			
		}
	}
	
	/**
	 * Given a xy coordinates, return the Vehicle that is currently at those
	 * x and y coordinates, returning null if no Vehicle currently sits at
	 * those coordinates.
	 * 
	 * @param x the x coordinate in pixels
	 * @param y the y coordinate in pixels
	 * @return the Vehicle object that currently sits at that xy location
	 */
	private Vehicle getVehicleFromXY(double x, double y) 
	{
		//TODO fix this implementation
		//get the spot from the given parameters
		Space spot = convertXYToRowColumn(x,y);
		//if theres a vehicle on that space on the board, return that vehicle
		if (level.getBoard().getVehicle(spot) != null)
		{
			return level.getBoard().getVehicle(spot);
		}
		
		//if not, return null
		else
		{
			return null;
		}
	}

	/**
	 * This is a useful helper function to help you calculate
	 * the number of spaces that a vehicle moved while dragging
	 * so that you can then send that information over as 
	 * numSpacesMoved to that particular Vehicle object.
	 * 
	 * @return the number of spaces that were moved
	 */
	private int calculateSpacesMoved() 
	{
		//tbh with you, im not 100% sure if this works... tbh not even 1% sure it works lol
		//create space objects with the coordinates of the before and after of the mosue
		Vehicle clicked = getVehicleFromXY(lastX, lastY);
		
		if (clicked != null)
		{
			if (clicked.isVert())
			{
				return(convertXYToRowColumn(newX, newY).getRow()-(convertXYToRowColumn(lastX, lastY).getRow()));
			}
			
			else
			{
				return(convertXYToRowColumn(newX, newY).getCol() - (convertXYToRowColumn(lastX, lastY).getCol()));
			}
		}
		else return 0;
		
	}

	/**
	 * Another helper function/method meant to return the rowcol
	 * given an x and y coordinate system.  Use this to help
	 * you write getVehicleFromXY
	 * 
	 * @param x x-coordinate (in pixels)
	 * @param y y-coordinate (in pixels)
	 * @return the RowCol associated with that x and y
	 */
	private Space convertXYToRowColumn(double x, double y) 
	{
		//TODO write this implementation hint (use helper methods below)
		int a1 = (int)(x/spaceWidth());	//round the double down to an integer
		int b1 = (int)(y/spaceHeight());
		Space box = new Space(b1, a1);	//put the new integers as the space
		return box;
	}

	/**
	 * 
	 * @return the width (in pixels) of a single space in the grid
	 */
	private double spaceWidth() 	//the size of the board, equally spaced into a 6x6 grid
	{
		return PROGRAM_WIDTH/NUM_COLS;		
	}

	/**
	 * 
	 * @return the height in pixels of a single space in the grid 
	 */
	private double spaceHeight() 
	{
		return PROGRAM_HEIGHT/NUM_ROWS;
	}
}
