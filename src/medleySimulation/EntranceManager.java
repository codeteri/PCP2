package medleySimulation;

import java.util.LinkedList;
import java.util.Queue;

public class EntranceManager {
    private final Queue<Swimmer> queue = new LinkedList<>();
    private final EntranceDoor entranceDoor = new EntranceDoor();

    public synchronized void addSwimmer(Swimmer swimmer) {
        queue.offer(swimmer); // Add swimmer to the queue
        notifyAll(); // Notify waiting threads
    }

    public synchronized void processEntries() throws InterruptedException {
        while (!queue.isEmpty()) {
            Swimmer swimmer = queue.poll(); // Retrieve the next swimmer in order
            entranceDoor.enter(swimmer); // Allow the swimmer to enter
        }
    }
}
