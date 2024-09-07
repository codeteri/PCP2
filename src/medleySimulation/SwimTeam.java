	//M. M. Kuttel 2024 mkuttel@gmail.com
	//Class to represent a swim team - which has four swimmers
	package medleySimulation;

	import medleySimulation.Swimmer.SwimStroke;
	//import java.util.concurrent.*;

	public class SwimTeam extends Thread {

		public static StadiumGrid stadium; // shared
		private Swimmer[] swimmers;
		private int teamNo; // team number

		public static final int sizeOfTeam = 4;

		SwimTeam(int ID, FinishCounter finish, PeopleLocation[] locArr) {
			this.teamNo = ID;

			swimmers = new Swimmer[sizeOfTeam];
			SwimStroke[] strokes = SwimStroke.values(); // Get all enum constants
			stadium.returnStartingBlock(ID);

			for (int i = teamNo * sizeOfTeam, s = 0; i < ((teamNo + 1) * sizeOfTeam); i++, s++) { // initialise swimmers in team
				locArr[i] = new PeopleLocation(i, strokes[s].getColour());
				int speed = (int) (Math.random() * (3) + 30); // range of speeds
				swimmers[s] = new Swimmer(i, this, locArr[i], finish, speed, strokes[s]); // hardcoded speed for now
			}
		}

		public void run() {
			try {
				for (int s = 0; s < sizeOfTeam; s++) { 
					swimmers[s].start(); // Start each swimmer thread
				}

				for (int s = 0; s < sizeOfTeam; s++)
					swimmers[s].join(); // Wait for each swimmer to finish

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//get the team number
		public int getTeamNo() {
			return this.teamNo;
		}

		// obtain the previous swimmer in team
		public Swimmer getPreviousSwimmer(int i) {
			return swimmers[i - 1];
		}
}

