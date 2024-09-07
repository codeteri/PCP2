// Simple class to record when someone has crossed the line first and wins
package medleySimulation;

public class FinishCounter {
	private boolean firstAcrossLine; //flag
	private int winner; //who won
	private int winningTeam; //counter for patrons who have left the club
	private int secondPlace; // second place swimmer
	private int secondTeam; // second place team
	private int thirdPlace; // third place swimmer
	private int thirdTeam; // third place team
	
	FinishCounter() { 
		firstAcrossLine= true;//no-one has won at start
		winner=-1; //no-one has won at start
		winningTeam=-1; //no-one has won at start
		secondPlace = -1; // no-one in second place at start
		secondTeam = -1; // no team in second place at start
		thirdPlace = -1; // no-one in third place at start
		thirdTeam = -1; // no team in third place at start
	}

    // This is called by a swimmer when they touch the finish line
    public synchronized void finishRace(int swimmer, int team) {
			if (firstAcrossLine) {
					firstAcrossLine = false;
					winner = swimmer;
					winningTeam = team;
			} else if (secondPlace == -1) {
					secondPlace = swimmer;
					secondTeam = team;
			} else if (thirdPlace == -1) {
					thirdPlace = swimmer;
					thirdTeam = team;
			}
	}

	//Has race been won?
	public boolean isRaceWon() {
		return !firstAcrossLine;
	}

	public int getWinner() { return winner; }
	
	public int getWinningTeam() { return winningTeam;}

	public int getSecondPlace() {return secondPlace;}

	public int getSecondTeam() {return secondTeam;}

	public int getThirdPlace() {return thirdPlace;}

	public int getThirdTeam() {return thirdTeam;}
}
