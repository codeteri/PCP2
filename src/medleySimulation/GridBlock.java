//M. M. Kuttel 2024 mkuttel@gmail.com
// GridBlock class to represent a block in the grid.
// only one thread at a time "owns" a GridBlock - this must be enforced

package medleySimulation;

public class GridBlock {
    
    private int isOccupied; // ID of the swimmer occupying the block, -1 if none
    private final boolean isStart;  // is this a starting block?
    private int[] coords; // the coordinate of the block.
    
    // Constructor for start block
    GridBlock(boolean startBlock) throws InterruptedException {
        isStart = startBlock;
        isOccupied = -1; // -1 means unoccupied
    }
    
    // Constructor with coordinates
    GridBlock(int x, int y, boolean startBlock) throws InterruptedException {
        this(startBlock);
        coords = new int[]{x, y};
    }
    
    // Get the X coordinate
    public int getX() {
        return coords[0];
    }
    
    // Get the Y coordinate
    public int getY() {
        return coords[1];
    }
    
    // Method to attempt to occupy the block
    public synchronized boolean occupy(int swimmerID) {
        if (isOccupied == -1) { // block is not occupied
            isOccupied = swimmerID; // occupy the block
            return true;
        }
        return false; // block is occupied
    }
    
    // Release the block
    public synchronized void release() {
        isOccupied = -1; // Mark block as unoccupied
    }
    
    // Check if the block is occupied
    public synchronized boolean isOccupied() {
        return isOccupied != -1;
    }
    
    // Check if this is a starting block
    public boolean isStart() {
        return isStart;
    }
}
