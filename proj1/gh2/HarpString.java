package gh2;
import deque.Deque;
import deque.LinkedListDeque;

public class HarpString {

    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor
    private Deque<Double> buffer = new LinkedListDeque<>();

    public HarpString(double frequency) {
        // TODO: Create a buffer with capacity = SR / frequency. You'll need to
        //       cast the result of this division operation into an int. For
        //       better accuracy, use the Math.round() function before casting.
        //       Your should initially fill your buffer array with zeros.
        double capacity = SR / frequency * 2;
        int capacity1 = (int)Math.round(capacity);
        buffer = new LinkedListDeque<>();
        for (int i = 0; i < capacity1;i++)
        {
            buffer.addLast(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        // TODO: Dequeue everything in buffer, and replace with random numbers
        //       between -0.5 and 0.5. You can get such a number by using:
        //       double r = Math.random() - 0.5;
        //
        //       Make sure that your random numbers are different from each
        //       other. This does not mean that you need to check that the numbers
        //       are different from each other. It means you should repeatedly call
        //       Math.random() - 0.5 to generate new random numbers for each array index.
        for (int i = 0; i < buffer.size();i++)
        {
            double r = Math.random() - 0.5;
            buffer.removeFirst();
            buffer.addLast(r);
        }

    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        // TODO: Dequeue the front sample and enqueue a new sample that is
        //       the average of the two multiplied by the DECAY factor.
        //       **Do not call StdAudio.play().**
        double rm = buffer.removeFirst();
        double nowfirst = buffer.get(0);
        double result = DECAY * 0.5 *(rm + nowfirst) * -1.0;
        buffer.addLast(result);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        // TODO: Return the correct thing.
        return buffer.get(0);
    }
}
