package medleySimulation;

public class EntranceDoor {
    private boolean occupied = false; // Indicates if the entrance door is occupied

    public synchronized void enter(Swimmer swimmer) throws InterruptedException {
        while (occupied) {
            wait(); // Wait until the door is free
        }
        occupied = true; // Mark the door as occupied
        try {
            // Simulate the swimmer entering
            System.out.println(swimmer + " is entering through the entrance door.");
            Thread.sleep(100); // Adjust time as needed
        } finally {
            occupied = false; // Mark the door as free
            notifyAll(); // Notify other swimmers that the door is now free
        }
    }
}
