//M. M. Kuttel 2024 mkuttel@gmail.com
// Simple Thread class to update the display of a text field
package medleySimulation;

import java.awt.Color;

import javax.swing.JLabel;

//You don't need to change this class
public class CounterDisplay implements Runnable {

	private FinishCounter results;
	private JLabel win;

	CounterDisplay(JLabel w, FinishCounter score) {
        this.win=w;
        this.results=score;
				this.win.setHorizontalAlignment(JLabel.CENTER);
    }
	
	public void run() { //this thread just updates the display of a text field
        while (true) {
        	//test changes colour when the race is won
					if (results.isRaceWon()) {
						win.setForeground(Color.BLACK);
						win.setText("<html><div style='text-align: center;'>"
							+ "Winning Team: " + results.getWinningTeam() + " (Gold) 🥇<br>" 
							+ "Second Place Team: " + results.getSecondTeam() + " (Silver) 🥈<br>"
							+ "Third Place Team: " + results.getThirdTeam() + " (Bronze) 🥉"
							+ "</div></html>");

							// For creativity we added Gold,Silver and Bronze Unicode chars to the end results 
						}
					else {
						win.setForeground(Color.BLACK);
						win.setText("------"); 
					}	
        }
    }
}
