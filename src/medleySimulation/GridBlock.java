//M. M. Kuttel 2024 mkuttel@gmail.com
// GridBlock class to represent a block in the grid.
// only one thread at a time "owns" a GridBlock - this must be enforced

package medleySimulation;


public class GridBlock {
	
	private int isOccupied; 
	
	private final boolean isStart;  //is this a starting block?
	private int [] coords; // the coordinate of the block.
	
	GridBlock(boolean startBlock) throws InterruptedException {
		isStart=startBlock;
		isOccupied= -1; // -1 means unoccupied
	}
	
	GridBlock(int x, int y, boolean startBlock) throws InterruptedException {
		this(startBlock);
		coords = new int [] {x,y};
	}
	
	public   int getX() {return coords[0];}  
	
	public   int getY() {return coords[1];}

	// Method to attempt to occupy the block
	public synchronized boolean occupy(int swimmerID) {
		if (isOccupied==-1) { // block is not occupied
			isOccupied=swimmerID; // occupy the block
			return true;
		}
		return false; // block is occupied
	}

	//Get a block
	public synchronized boolean get(int threadID) throws InterruptedException {
		if (isOccupied==threadID) return true; //thread Already in this block
		if (isOccupied>=0) return false; //space is occupied
		isOccupied= threadID;  //set ID to thread that had block
		return true;
	}


	//release a block
	public  void release() {
		isOccupied= -1; // Mark
	}
	

	//is a bloc already occupied?
	public  boolean occupied() {
		if(isOccupied==-1) return false;
		return true;
	}
	
	
	//is a start block
	public  boolean isStart() {
		return isStart;	
	}

}
