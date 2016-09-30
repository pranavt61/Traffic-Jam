import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;

import acm.graphics.*;
import acm.program.*;

public class GraphicsGame extends GraphicsProgram {
	/**
	 * Here are all of the constants
	 */
	private GLabel currMove; // store move number
	private Vehicle curVehicleDragged; // store current vehicle being dragged
	private Level level;
	private GPoint last; /* The last mouse position */
	public static final int NUM_ROWS=6;
	public static final int NUM_COLUMNS=6; 
	public static final int PROGRAM_WIDTH = 500;
	public static final int PROGRAM_HEIGHT = 500;
	public static final String lABEL_FONT = "Arial-Bold-22";
	public static final String EXIT_SIGN = "EXIT";
	public static final String IMG_FILENAME_PATH = "images/";
	public static final String IMG_EXTENSION = ".png";
	public static final String VERTICAL_IMG_FILENAME = "_vert";

	//TODO declare your instance variables here

	public void init() {
		setSize(PROGRAM_WIDTH, PROGRAM_HEIGHT);
	}

	public void run() {
		//TODO write this part, which is like your main function
		drawLevel();
	}

	private void drawLevel() {
		//TODO write the code to draw the entire level, which should
		//mostly be calls to some of your helper functions.
		level = new Level(NUM_ROWS, NUM_COLUMNS);
		drawGridLines();	
		drawCars();
		drawWinningTile();
		drawMoveNumber();
	}

	/**
	 * This should draw the label EXIT and add it to the space that 
	 * represents the winning tile.
	 */
	private void drawWinningTile() {
		Space s = level.getBoard().getWinningSpace();
		GLabel glLabel = new GLabel(EXIT_SIGN,(spaceWidth()*s.getCol())+ spaceWidth()/2,(spaceHeight()*s.getRow())+spaceHeight()/2);
		add(glLabel);

	}

	/**
	 * draw the lines of the grid.  Test this out and make sure you
	 * have it working first.  Should draw the number of grids
	 * based on the number of rows and columsn in Level
	 */
	private void drawGridLines() {
		double x0=0;
		double x1=0;
		double y0=0;
		double y1=PROGRAM_HEIGHT;
//draw columns grid line
		for(int i=1;i<NUM_COLUMNS;i++){
			GLine line = new GLine (x0+spaceWidth()*i,y0,x1+spaceWidth()*i,y1);
			add(line);

		}
		x0=0;
		x1=PROGRAM_WIDTH;
		y0=0;
		y1=0;
//draw row grid lines
		for(int i=1;i<NUM_ROWS;i++){
			GLine line123 = new GLine (x0,y0+spaceHeight()*i,x1,y1+spaceHeight()*i);
			add(line123);
		}

	} 





	/**
	 * Maybe given a list of all the cars, you can go through them
	 * and call drawCar on each?
	 */
	private void drawCars() {
		level.getBoard().addVehicle(VehicleType.TRUCK, 0 ,2, 3 , true ); //adding initial vehicles by calling add vehicle and setting param values
		level.getBoard().addVehicle(VehicleType.MYCAR, 2,0, 2 , false );
		level.getBoard().addVehicle(VehicleType.AUTO, 0,4, 2, false );
		level.getBoard().addVehicle(VehicleType.AUTO, 1,4, 2, true );
		level.getBoard().addVehicle(VehicleType.AUTO, 1,5, 2, true );
		level.getBoard().addVehicle(VehicleType.AUTO, 3,3, 2, true );
		level.getBoard().addVehicle(VehicleType.AUTO, 4,4, 2, false );
		drawCar(level.getBoard().getVehicle(new Space(0,2)));
		drawCar(level.getBoard().getVehicle(new Space(2,0)));
		drawCar(level.getBoard().getVehicle(new Space(0,4)));
		drawCar(level.getBoard().getVehicle(new Space(1,4)));
		drawCar(level.getBoard().getVehicle(new Space(1,5)));
		drawCar(level.getBoard().getVehicle(new Space(3,3)));
		drawCar(level.getBoard().getVehicle(new Space(4,4)));






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
		String vert = ""; // string to store vertical suffix
		if(v.getVertical())
			vert=VERTICAL_IMG_FILENAME;
		String vehicleName = v.getVehicleType().toString(); 
		String fileName = IMG_FILENAME_PATH+vehicleName+vert+IMG_EXTENSION;
		Space s = v.getStartPos();
		GImage image = new GImage(fileName,s.getCol()*spaceWidth(),s.getRow()*spaceHeight()	); // create new image
		if(v.getVertical()){
			image.setSize(spaceWidth(),v.getSpaceOccupied()*spaceHeight());  // adjust image size
		}
		else
			image.setSize(spaceWidth()*v.getSpaceOccupied(),spaceHeight()); // adjust image size
		add(image);
		image.addMouseListener(this);  // mouse listner
		image.addMouseMotionListener(this); // mouse motion listner
		//TODO implement drawCar
	}

	//TODO implement the mouse listeners here

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
		//TODO fix this implementation
		return null;
	}

	/**
	 * This is a useful helper function to help you calculate
	 * the number of spaces that a vehicle moved while dragging
	 * so that you can then send that information over as 
	 * numSpacesMoved to that particular Vehicle object.
	 * 
	 * @return the number of spaces that were moved
	 */
	private int calculateSpacesMoved() {
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
		//TODO write this implementation hint (use helper methods below)
		int row = (int)(y/spaceHeight());
		int col = (int)(x/spaceWidth());
		return new Space(row, col);	    


	}

	/**
	 * 
	 * @return the width (in pixels) of a single space in the grid
	 */
	private double spaceWidth() {
		//TODO fix this method

		return PROGRAM_WIDTH/NUM_COLUMNS;
	}

	/**
	 * 
	 * @return the height in pixels of a single space in the grid 
	 */
	private double spaceHeight() {
		//TODO fix this method
		return PROGRAM_HEIGHT/NUM_ROWS; 
	}
	public void mousePressed(MouseEvent e) {
		last = new GPoint(e.getPoint());
		curVehicleDragged = level.getBoard().getVehicle(convertXYToRowColumn(last.getX(), last.getY()));
	}
	public void mouseReleased(MouseEvent e) {
		GObject gobj = (GObject) e.getSource();
		GRectangle bound = gobj.getBounds();
		Space finalLoc = null;
		int numberOfSpaces = 0;
		if(curVehicleDragged != null){
			if(curVehicleDragged.getVertical()){
			    // get final postion of vehicle after dragging
				finalLoc = convertXYToRowColumn(bound.getX(), bound.getY());
				Space startPos = new Space( curVehicleDragged.getStartPos().getRow(), curVehicleDragged.getStartPos().getCol());
				// calculate number of space being dragged
				numberOfSpaces = finalLoc.getRow()- startPos.getRow();
				boolean moved = level.getBoard().moveNumSpaces(startPos, numberOfSpaces); // try to move vehicle
				Space newSpace = new Space(startPos.getRow()+numberOfSpaces, startPos.getCol());
				if(moved){
					level.incrementMoveNum(); 
					drawMoveNumber();
					// set location of vehicle image
					gobj.setLocation(newSpace.getCol()*spaceWidth(),newSpace.getRow()*spaceHeight());
				}
				else{
					gobj.setLocation(startPos.getCol()*spaceWidth(),startPos.getRow()*spaceHeight());
				}
			}
			else{
				finalLoc = convertXYToRowColumn(bound.getX(), bound.getY());
				Space startPos = new Space( curVehicleDragged.getStartPos().getRow(), curVehicleDragged.getStartPos().getCol());
				numberOfSpaces = finalLoc.getCol()- startPos.getCol();
				boolean moved = level.getBoard().moveNumSpaces(startPos, numberOfSpaces);
				Space newSpace = new Space(startPos.getRow(), startPos.getCol()+numberOfSpaces);
				if(moved){
					level.incrementMoveNum();
					drawMoveNumber();
					gobj.setLocation(newSpace.getCol()*spaceWidth(),newSpace.getRow()*spaceHeight());
				}
				else{
					gobj.setLocation(startPos.getCol()*spaceWidth(),startPos.getRow()*spaceHeight());
				}

				//try

			}
		}
		if(level.getBoard().gameWin()){
			removeAll();
			drawWinningMessage();
		}


	}
	public void drawWinningMessage(){
		GLabel glLabel = new GLabel("Congrats you won game",PROGRAM_WIDTH/2,PROGRAM_HEIGHT/2);
		add(glLabel);
	}
	private void  drawMoveNumber(){
		if(currMove != null){
			remove(currMove);
		}
		currMove = new GLabel(String.valueOf(level.getNumberOfMoves()),spaceWidth()/2,spaceHeight()/2);
		add(currMove);

	}

	public void mouseDragged(MouseEvent e) {
		GObject gobj = (GObject) e.getSource();
		if(curVehicleDragged!=null)
		{
			if(curVehicleDragged.getVertical())
			{
				if(gobj.getY() < 0 )
				{
					gobj.move(0, 0);
					gobj.setLocation(gobj.getX(),0);
				}	 
				else if(gobj.getY()+gobj.getHeight() > PROGRAM_HEIGHT){
					gobj.move(0, PROGRAM_HEIGHT-gobj.getHeight());
					gobj.setLocation(gobj.getX(),PROGRAM_HEIGHT-gobj.getHeight());
				}
				else{
					GRectangle bound = gobj.getBounds();
					Space s1 = convertXYToRowColumn(bound.getX(), bound.getY());
					Space s2 = convertXYToRowColumn(bound.getX(),bound.getY()+bound.getHeight()-1);
					if((level.getBoard().getVehicle(s1)==null || level.getBoard().getVehicle(s1)==curVehicleDragged) && 
							(level.getBoard().getVehicle(s2)==null || level.getBoard().getVehicle(s2)==curVehicleDragged)){
						gobj.move(0, e.getY() - last.getY());
					}
				}

			}
			else
			{
				if(gobj.getX() < 0 )
				{
					gobj.move(0, 0);
					gobj.setLocation(0,gobj.getY());
				}	 
				else if(gobj.getX()+gobj.getWidth() > PROGRAM_WIDTH){
					gobj.move(PROGRAM_WIDTH-gobj.getWidth(), 0);
					gobj.setLocation(PROGRAM_WIDTH-gobj.getWidth(),gobj.getY());
				}
				else{
					GRectangle bound = gobj.getBounds();
					Space s1 = convertXYToRowColumn(bound.getX(), bound.getY());
					Space s2 = convertXYToRowColumn(bound.getX()+bound.getWidth()-1, bound.getY());
					if((level.getBoard().getVehicle(s1)==null || level.getBoard().getVehicle(s1)==curVehicleDragged) && 
							(level.getBoard().getVehicle(s2)==null || level.getBoard().getVehicle(s2)==curVehicleDragged)){
						gobj.move(e.getX() - last.getX(),0);
					}
				}
			}


		}

		last = new GPoint(e.getPoint());
	}

}
