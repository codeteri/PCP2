//M. M. Kuttel 2024 mkuttel@gmail.com
//Class to represent a swimmer swimming a race
//Swimmers have one of four possible swim strokes: backstroke, breaststroke, butterfly and freestyle
package medleySimulation;

import java.awt.Color;

import java.util.Random;

import java.util.concurrent.*;

public class Swimmer extends Thread {

	public static StadiumGrid stadium; // shared
	private FinishCounter finish; // shared

	GridBlock currentBlock;
	private Random rand;
	private int movingSpeed;

	private PeopleLocation myLocation;
	private int ID; // thread ID
	private SwimTeam team; // team ID
	private GridBlock start;

	// private CountDownLatch[] order;

	private static CyclicBarrier barrier = new CyclicBarrier(10);

	public enum SwimStroke {
		Backstroke(1, 2.5, Color.black),
		Breaststroke(2, 2.1, new Color(255, 102, 0)),
		Butterfly(3, 2.55, Color.magenta),
		Freestyle(4, 2.8, Color.red);

		private final double strokeTime;
		private final int order; // in minutes
		private final Color colour;

		SwimStroke(int order, double sT, Color c) {
			this.strokeTime = sT;
			this.order = order;
			this.colour = c;
		}

		public int getOrder() {
			return order;
		}

		public Color getColour() {
			return colour;
		}
	}

	private final SwimStroke swimStroke;

	// Constructor
	Swimmer(int ID, SwimTeam t, PeopleLocation loc, FinishCounter f, int speed, SwimStroke s) {
		this.swimStroke = s;
		this.ID = ID;
		movingSpeed = speed; // range of speeds for swimmers
		this.myLocation = loc;
		this.team = t;
		start = stadium.returnStartingBlock(team.getTeamNo());
		finish = f;
		rand = new Random();
		// this.order = order;
	}

	// getter
	public int getX() {
		return currentBlock.getX();
	}

	// getter
	public int getY() {
		return currentBlock.getY();
	}

	// getter
	public int getSpeed() {
		return movingSpeed;
	}

	public SwimStroke getSwimStroke() {
		return swimStroke;
	}


	// swimmer enters stadium area
	public void enterStadium() throws InterruptedException {
		currentBlock = stadium.enterStadium(myLocation); //
		sleep(200); // wait a bit at door, look around
	}

	// go to the starting blocks
	public void goToStartingBlocks() throws InterruptedException {
		int x_st = start.getX();
		int y_st = start.getY();
		
		while (currentBlock != start) {
			
			sleep(movingSpeed * 3); // not rushing
			currentBlock = stadium.moveTowards(currentBlock, x_st, y_st, myLocation); // head towards starting block
		}
		System.out.println(
				"-----------Thread " + this.ID + " at start " + currentBlock.getX() + " " + currentBlock.getY());
	}

	// dive in to the pool
	private void dive() throws InterruptedException {
		int x = currentBlock.getX();
		int y = currentBlock.getY();
		currentBlock = stadium.jumpTo(currentBlock, x, y - 2, myLocation);
	}


	private void swimRace() throws InterruptedException {
		int x = currentBlock.getX();
		while ((boolean) ((currentBlock.getY()) != 0)) {
			currentBlock = stadium.moveTowards(currentBlock, x, 0, myLocation);
			sleep((int) (movingSpeed * swimStroke.strokeTime)); // swim
			System.out.println("Thread " + this.ID + " swimming  at speed" + movingSpeed);
		}

		while ((boolean) ((currentBlock.getY()) != (StadiumGrid.start_y - 1))) {
			currentBlock = stadium.moveTowards(currentBlock, x, StadiumGrid.start_y, myLocation);
			sleep((int) (movingSpeed * swimStroke.strokeTime)); // swim
		}

	}


	// after finished the race
	public void exitPool() throws InterruptedException {
		int bench = stadium.getMaxY() - swimStroke.getOrder(); // they line up
		int lane = currentBlock.getX() + 1;// slightly offset
		currentBlock = stadium.moveTowards(currentBlock, lane, currentBlock.getY(), myLocation);
		while (currentBlock.getY() != bench) {
			currentBlock = stadium.moveTowards(currentBlock, lane, bench, myLocation);
			sleep(movingSpeed * 3); // not rushing
		}
	}

	public void run() {
		try {

			// Swimmer arrives
			sleep(movingSpeed + (rand.nextInt(10))); // arriving takes a while
			myLocation.setArrived();


			if (this.swimStroke.order != 1) {

				// Gets the number of the previous swimmer
				int prevOrder = swimStroke.order - 1;

				synchronized (team) {
					// Gets the previous swimmer in the team and checks if they have entered
					Swimmer prevSwimmer = team.getPreviousSwimmer(prevOrder);
					while (prevSwimmer.currentBlock == null) {
						team.wait(); // Tells the simmwer to wait if the previous team member has not entered
					}
				}
			}

			enterStadium();

			// Notifies the team members
			synchronized (team) {
				team.notifyAll();
			}

			goToStartingBlocks();

			// sets the first swimmer at the barrier
			try {
				barrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				System.out.println(e);
			}

			// Prevents the rest of the swim team from getting into the pool while there is still a swimmer 
			synchronized (team) {
				dive();
				swimRace();
			}

			if (swimStroke.order == 4) {
				finish.finishRace(ID, team.getTeamNo()); // fnishline
			} else {
				// System.out.println("Thread "+this.ID + " done " + currentBlock.getX() + " "
				// +currentBlock.getY() );
				exitPool();// if not last swimmer leave pool
			}

		} catch (InterruptedException e1) { // do nothing
		}
	}

}
