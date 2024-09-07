//M. M. Kuttel 2024 mkuttel@gmail.com
// GridBlock class to represent a block in the grid.
// only one thread at a time "owns" a GridBlock - this must be enforced

package medleySimulation;

import java.util.concurrent.CountDownLatch;

public class GridBlock {
	private final CountDownLatch latch; // Latch to control access to the block
	private final boolean isStart;  // Indicates if this is a starting block
	private int[] coords; // Coordinates of the block
	private int isOccupied; // -1 if free, otherwise the ID of the occupying swimmer

	// Constructor for a block with a start flag
	GridBlock(boolean startBlock) {
		isStart = startBlock;
		latch = new CountDownLatch(1); // Only one permit
		isOccupied = -1; // Initially free
	}

	// Constructor for a block with coordinates and a start flag
	GridBlock(int x, int y, boolean startBlock) {
		this(startBlock);
		coords = new int[]{x, y};
	}

	// Getter for the X coordinate
	public int getX() {
		return coords[0];
	}

	// Getter for the Y coordinate
	public int getY() {
		return coords[1];
	}

	// Method to attempt to occupy the block
	public void occupy() throws InterruptedException {
		latch.await(); // Wait for the latch to be released
	}

	// Release the block
	public void release() {
		latch.countDown(); // Release the latch
	}

	// Check if the block is a start block
	public boolean isStart() {
		return isStart;
	}

	// Try to acquire the block for the swimmer
	public synchronized boolean get(int threadID) {
		if (isOccupied == threadID) {
			return true; // Swimmer already occupies this block
		}
		if (isOccupied == -1) { // Block is free
			isOccupied = threadID; // Swimmer occupies the block
			return true;
		}
		return false; // Block is occupied by another swimmer
	}
}
