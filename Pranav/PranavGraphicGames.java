import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;

import acm.graphics.*;
import acm.program.*;

public class GraphicsGame extends GraphicsProgram {
	
	/**
	 * Here are all of the constants
	 */	
	private static final long serialVersionUID = 1L;
	public static final int PROGRAM_WIDTH = 1000;
	public static final int PROGRAM_HEIGHT = 700;
	public static int NUM_COLS = 8;
	public static int NUM_ROWS = 5;
	public static int EXIT_ROW = 2; // row of the Exit Space (Level) default values, changed in init()
	public static int EXIT_COL = 5; // col of the Exit Space (Level) default values, changed in init()
	public static final String lABEL_FONT = "Arial-Bold-22";
	public static final String EXIT_SIGN = "EXIT";
	public static final String IMG_FILENAME_PATH = "images/";
	public static final String IMG_EXTENSION = ".png";
	public static final String VERTICAL_IMG_FILENAME = "_vert";

	static Level level;
	static Vehicle vehicleSelected = null;
	static GImage imageSelected = null;
	static GLabel movesCountView;
	static ArrayList<GImage> vehicleImages; // list of all of the vehicle images on board to move 
	static double mouseOffset = 0; // used to make sure the dragging image does not jump
	static Space[] spacesOccupiedOnBoard;
	static int lastMousePosX = 0; // calculated in Mouse Dragged
	static int lastMousePosY = 0; // calculated in Mouse Dragged



	public void init() {
		//init variables
		vehicleImages = new ArrayList<GImage>();
		movesCountView = new GLabel("Moves: 0", 10, 10);
		level = new Level(NUM_ROWS, NUM_COLS);
		level.setupLevel(NUM_ROWS, NUM_COLS);
		spacesOccupiedOnBoard = level.getSpacesOccupied();

		//EXIT_ROW: put exit row where ever the player car is
		for(Vehicle v: level.getVehiclesOnLevel())
			if(v.getType() == VehicleType.MYCAR)
				EXIT_ROW = v.getStart().getRow();
		
		//EXIT_COL: put at the right most col
		EXIT_COL = NUM_COLS -1;
		
		setSize(PROGRAM_WIDTH, PROGRAM_HEIGHT);
		requestFocus();
		addMouseListeners();
	}

	public void run() {
		drawLevel();
	}

	public static Space getSpaceFromBoard(int row, int col) {
		return level.getSpace(row, col);
	}
	
	private void drawNumMove() {
		
		movesCountView.setLabel("Moves:" + Integer.toString(level.getNumMoves()));
		add(movesCountView);
	}

	private void drawLevel() {
		drawGridLines();
		drawWinningTile();
		drawCars();
		drawNumMove();
	}

	/**
	 * This should draw the label EXIT and add it to the space that 
	 * represents the winning tile.
	 */
	private void drawWinningTile() {
		GLabel exitSign = new GLabel("EXIT", (EXIT_COL * spaceWidth()) + spaceWidth()/2
				, (EXIT_ROW * spaceHeight()) + spaceHeight()/2);
		exitSign.setColor(Color.red);
		add(exitSign);
	}

	/**
	 * draw the lines of the grid.  Test this out and make sure you
	 * have it working first.  Should draw the number of grids
	 * based on the number of rows and cols in Level
	 */
	private void drawGridLines() {

		GLine toDrawLine;

		// vertical lines
		for(int cols = 0; cols <= NUM_COLS; cols ++)
		{
			toDrawLine = new GLine(cols * spaceWidth(), 0, cols * spaceWidth(), PROGRAM_HEIGHT);
			toDrawLine.setColor(Color.black);
			add(toDrawLine);
		}

		// horizontal lines
		for(int rows = 0; rows <= NUM_ROWS; rows ++)
		{
			toDrawLine = new GLine(0, rows * spaceHeight(), PROGRAM_WIDTH, rows * spaceHeight());
			toDrawLine.setColor(Color.black);
			add(toDrawLine);
		}
	}

	/**
	 * Maybe given a list of all the cars, you can go through them
	 * and call drawCar on each?
	 */
	private void drawCars() {
		for(Vehicle v : level.getVehiclesOnLevel())
			drawCar(v);

		for(GImage i : vehicleImages)
			add(i);
	}

	//clears all vehicles from board
	private void clearCars() {

		for(GImage i : vehicleImages)
			remove(i);

		vehicleImages.clear();
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
	private void drawCar(Vehicle v) {

		double imageWidth = spaceWidth(),
				imageHeight = spaceHeight();

		GImage vehicleImage = new GImage(getImageFilePath(v), 
				convertSpaceToXY(v.getStart()).getX(),
				convertSpaceToXY(v.getStart()).getY());

		//resize image to fit space
		if(v.isVertical())
			imageHeight *= v.getLength();
		else
			imageWidth *= v.getLength();

		vehicleImage.setSize(imageWidth, imageHeight);

		// add to array list
		vehicleImages.add(vehicleImage);
	}

	private String getImageFilePath(Vehicle v) {

		String imagePath = IMG_FILENAME_PATH;

		switch (v.getType()) {
		case MYCAR:
			imagePath += "car";
			break;
		case TRUCK:
			imagePath += "truck";
			break;
		case AUTO:
			imagePath += "auto";
			break;
		}

		if(v.isVertical())
			imagePath += VERTICAL_IMG_FILENAME;

		imagePath += IMG_EXTENSION;
		return imagePath;
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
	private Vehicle getVehicleFromXY(double x, double y) {		
		return level.getVehicle(convertXYToRowColumn(x, y));
	}

	/**
	 * This is a useful helper function to help you calculate
	 * the number of spaces that a vehicle moved while dragging
	 * so that you can then send that information over as 
	 * numSpacesMoved to that particular Vehicle object.
	 * 
	 * @param v vehicle to move
	 * @param s space to move to
	 * @return the number of spaces that were moved
	 */
	private int calculateSpacesMoved() {

		// find the difference of spaces between the starting position of the image to the ending position
		if(vehicleSelected.isVertical())
			return (convertXYToRowColumn(imageSelected.getX(), imageSelected.getY()).getRow() - vehicleSelected.getStart().getRow());
		
		return (convertXYToRowColumn(imageSelected.getX(), imageSelected.getY()).getCol() - vehicleSelected.getStart().getCol());

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
	private Space convertXYToRowColumn(double x, double y) {
		return level.getSpace((int)Math.floor(y/spaceHeight()), (int)Math.floor(x/spaceWidth()));
	}

	private GPoint convertSpaceToXY(Space s)
	{
		return new GPoint((int)(s.getCol() * spaceWidth()),
				(int)(s.getRow() * spaceHeight()));
	}

	/**
	 * returns a GImage from the X Y pixel coordinates if the image is vehicle
	 * 
	 * @param x x-coordinate (in pixels)
	 * @param y y-coordinate (in pixels)
	 * @return GImage of vehicle
	 */
	private GImage getVehicleImage(int x, int y)
	{		
		for(GImage i: vehicleImages)
			if(i.equals(getElementAt(x, y)))
				return i;

		return null;
	}

	//Mouse Event Methods
	@Override
	public void mousePressed(MouseEvent e) {

		vehicleSelected = getVehicleFromXY(e.getX(), e.getY());
		imageSelected = getVehicleImage(e.getX(), e.getY());

		// get mouse offset
		if(imageSelected != null)
			if(vehicleSelected.isVertical())
				mouseOffset = e.getY() - imageSelected.getY();				

			else
				mouseOffset = e.getX() - imageSelected.getX();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		int mouseVelX = e.getX() - lastMousePosX,
				mouseVelY = e.getY() - lastMousePosY;

		// Monster collision system XD
		if(imageSelected != null){
			if(vehicleSelected.isVertical()){
				if(mouseVelY > 0 && !collisionBotRight(spacesOccupiedOnBoard)){
				imageSelected.setLocation(imageSelected.getX(),
						e.getY() - mouseOffset);
				}
			
				else if(mouseVelY < 0 && !collisionTopLeft(spacesOccupiedOnBoard)){
					imageSelected.setLocation(imageSelected.getX(),
							e.getY() - mouseOffset);
				}
			}
		
			else{
				if(mouseVelX > 0 && !collisionBotRight(spacesOccupiedOnBoard)){
					imageSelected.setLocation(e.getX() - mouseOffset,
						imageSelected.getY());
				}
				else if(mouseVelX < 0 && !collisionTopLeft(spacesOccupiedOnBoard))
				{
					imageSelected.setLocation(e.getX() - mouseOffset,
							imageSelected.getY());
				}
			}
		}
		
		lastMousePosX = e.getX();
		lastMousePosY = e.getY();

	}

	@Override
	public void mouseReleased(MouseEvent e) {

		// can move?
		// spaces Calculated uses the imageSelected X and Y positions to see where the image was dragged to
		if(vehicleSelected != null){

			int spacesMoved = calculateSpacesMoved();

			if(level.canMoveNumSpaces(vehicleSelected.getStart(),spacesMoved))
			{
				// move space 
				level.moveNumSpaces(vehicleSelected.getStart(), spacesMoved);
				
				// update spaces occupied (for collision purposes)
				spacesOccupiedOnBoard = level.getSpacesOccupied();
			}
		}		
		// reset mouse select variables
		vehicleSelected = null;
		imageSelected = null;

		//redraw vehicles in new positions
		clearCars();
		
		if(level.passedLevel())
			System.out.println("Level Passed");
		else 	
			drawCars();
		
		drawNumMove();

	}

	/*
	 * returns true if imageSelected's top left corner has collided with another image or screen edge
	 * */
	private boolean collisionTopLeft(Space[] spacesOccupied)
	{
		Space imageSpace;
		double collisionPointX = imageSelected.getX(),
				collisionPointY = imageSelected.getY();

		if(collisionPointX - 2 < 0)
			return true;

		if(collisionPointY - 2  < 0)
			return true;

		// 2 pixel offset inwards for space collision
		imageSpace = convertXYToRowColumn(collisionPointX + 4, collisionPointY + 4); 

		for(Space s : spacesOccupied)
			if(s.equals(imageSpace)
					&& level.getVehicle(s).getID() != vehicleSelected.getID())
				return true;
		
		// no collision
		return false;
	}

	/*
	 * returns true if imageSelected's bottom right corner has collided with another image or screen edge
	 * */
	private boolean collisionBotRight(Space[] spacesOccupied)
	{
		Space imageSpace;
		double collisionPointX = imageSelected.getX() + imageSelected.getWidth(), 
				collisionPointY = imageSelected.getY() + imageSelected.getHeight();
		
		if( collisionPointX + 4 > PROGRAM_WIDTH)
			return true;

		if(collisionPointY + 4 > PROGRAM_HEIGHT)
			return true;
		

		// 5 pixel offset inwards for space collision (subtract out the outward offset)
		imageSpace = convertXYToRowColumn(collisionPointX - 4 , collisionPointY - 4); 


		for(Space s : spacesOccupied)
			if(s.equals(imageSpace)
					&& level.getVehicle(s).getID() != vehicleSelected.getID())
				return true;

		// no collision
		return false;
	}


	/**
	 * 
	 * @return the width (in pixels) of a single space in the grid
	 */
	private double spaceWidth() {
		return (PROGRAM_WIDTH / NUM_COLS);
	}

	/**
	 * 
	 * @return the height in pixels of a single space in the grid 
	 */
	private double spaceHeight() {
		return (PROGRAM_HEIGHT / NUM_ROWS);
	}
}
