import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.JLabel;

import acm.graphics.*;
import acm.program.*;

public class GraphicsGame extends GraphicsProgram {
	/**
	 * Here are all of the constants
	 */
	public static final int PROGRAM_WIDTH = 500;
	public static final int PROGRAM_HEIGHT = 500;
	public static final String LABEL_FONT = "Arial-Bold-22";
	public static final String EXIT_SIGN = "EXIT";
	public static final String IMG_FILENAME_PATH = "images/";
	public static final String IMG_EXTENSION = ".png";
	public static final String VERTICAL_IMG_FILENAME = "_vert";

	//TODO declare your instance variables here
	private Level level;
	private GObject toDrag;
	
	public void init() {
		setSize(PROGRAM_WIDTH, PROGRAM_HEIGHT);
		level = new Level(6,6);
	}

	public void run() {
		//TODO write this part, which is like your main function
		drawLevel();
		addMouseListeners();
	}

	private void drawLevel() {
		//TODO write the code to draw the entire level, which should
		//mostly be calls to some of your helper functions
		drawGridLines();
		drawCars();
		drawWinningTile();
		drawCounter();
	}

	/**
	 * This should draw the label EXIT and add it to the space that 
	 * represents the winning tile.
	 */
	private void drawWinningTile() {
		Space s = new Space(level.getWinSpace().getRow(), level.getWinSpace().getCol());
		GLabel exit = new GLabel(EXIT_SIGN, s.getCol() * spaceWidth(), s.getRow() * spaceHeight());
		exit.setFont(LABEL_FONT);
		add(exit);
	}

	private void drawCounter(){
		GLabel counter = new GLabel("0", spaceWidth() - 50, spaceHeight() - 50);
		counter.setFont(LABEL_FONT);
		add(counter);
	}
	
	/**
	 * draw the lines of the grid.  Test this out and make sure you
	 * have it working first.  Should draw the number of grids
	 * based on the number of rows and columns in Level
	 */
	private void drawGridLines() {
		for (int i = 0; i < level.getColumns() - 1; i++){
			GLine line = new GLine((i+1) * spaceWidth(), 0, (i+1) * spaceWidth(), PROGRAM_HEIGHT);
			add(line);
		}
		for (int i = 0; i < level.getRows() - 1; i++){
			GLine line = new GLine(0, (i+1) * spaceHeight(), PROGRAM_WIDTH, (i+1) * spaceHeight());
			add(line);
		}
	}

	/**
	 * Maybe given a list of all the cars, you can go through them
	 * and call drawCar on each?
	 */
	private void drawCars() {
		for (Vehicle  v : level.getVehiclesOnLevel()){
			drawCar(v);
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
	private void drawCar(Vehicle v) {
		String filename = IMG_FILENAME_PATH;
		Space s = v.getStart();
		if (v.getVehicleType() == VehicleType.TRUCK) filename += "truck";
		if (v.getVehicleType() == VehicleType.AUTO) filename += "auto";
		if (v.getVehicleType() == VehicleType.MYCAR) filename += "car";
		if (v.isVertical()) filename += VERTICAL_IMG_FILENAME;
		filename += IMG_EXTENSION;
		GImage piece = new GImage(filename, s.getCol() * spaceWidth(), s.getRow() * spaceHeight());
		if (v.isVertical()){
			piece.setSize(spaceWidth(), v.getSize() * spaceHeight());
		}
		else{
			piece.setSize(spaceWidth() * v.getSize(), spaceHeight());
		}
		add(piece);
	}
	
	//TODO implement the mouse listeners here
	public void mousePressed(MouseEvent e){
		Vehicle v = getVehicleFromXY(e.getX(), e.getY());
		if (toDrag == null) return;
	}
	
	public void mouseDragged(MouseEvent e){
		toDrag.setLocation(e.getX(), e.getY());
	}

	public void mouseReleased(MouseEvent e){
		
	}
	
	private boolean isVert(){
		return false;
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
		Space s = new Space (convertXYToRowColumn(x, y).getRow(), convertXYToRowColumn(x,y).getCol());
		return level.getVehicle(s);
	}

	/**
	 * This is a useful helper function to help you calculate
	 * the number of spaces that a vehicle moved while dragging
	 * so that you can then send that information over as 
	 * numSpacesMoved to that particular Vehicle object.
	 * 
	 * @return the number of spaces that were moved
	 */
	private int calculateSpacesMoved(MouseEvent e) {
		return 0;
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
		double temp = x % level.getColumns();
		x = (x / spaceWidth()) + temp;
		temp = y % level.getColumns();
		y = (y /spaceWidth()) + temp;
		Space s = new Space((int)x, (int)y);
		return s;
	}

	/**
	 * 
	 * @return the width (in pixels) of a single space in the grid
	 */
	private double spaceWidth() {
		return PROGRAM_WIDTH/level.getColumns();
	}

	/**
	 * 
	 * @return the height in pixels of a single space in the grid 
	 */
	private double spaceHeight() {
		return PROGRAM_HEIGHT/level.getRows();
	}
}
