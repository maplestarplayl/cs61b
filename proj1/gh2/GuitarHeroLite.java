package gh2;
import deque.ArrayDeque;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;
import deque.Deque;
import deque.LinkedListDeque;


/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHeroLite {
    public static final double CONCERT_A = 440.0;
    public static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);
    public static final double CONCERT_X = 1000;
    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        HarpString stringA = new HarpString(CONCERT_A);
        HarpString stringC = new HarpString(CONCERT_C);
        GuitarString stringX = new GuitarString(CONCERT_X);
        char[] key = new char[]{'q','2','w','e','4','r','5','t','y','7','u','8','i','9','o','p','-','[','=','z','x','d','c','f','v','g','b','n','j','m','k',',','.',';','/','’'};
        Deque<Character> keys = new LinkedListDeque<>();
        for (char x : key)
        {
            keys.addLast(x);
        }
        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char keya = StdDraw.nextKeyTyped();
                for (int i = 0; i < keys.size();i++)
                {
                    if (keya == key[i])
                    {
                        int index = i;
                        stringX = new GuitarString(440.0*Math.pow(2,(index-24)/12));
                        stringX.pluck();
                        break;
                    }

                }

            }

            /* compute the superposition of samples */
            double sample =stringX.sample();

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            stringX.tic();
        }
    }
}

