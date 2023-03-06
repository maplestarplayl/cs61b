package randomizedtest;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void test1()
    {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> M = new BuggyAList<>();

        int N = 500;
        for (int j = 0;j < 500;j++){
            int operationNumber = edu.princeton.cs.introcs.StdRandom.uniform(0, 4);

            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                M.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int size0 = M.size();
                assertEquals(size,size0);
                System.out.println("size: " + size);
            } else if (operationNumber == 2 && L.size() > 0) {
                //getlast
                int randVal1 = L.getLast();
                int randVal2 = M.getLast();
                System.out.println("getLast(" + randVal1 + ")");
                assertEquals(randVal1,randVal2);
            } else if (operationNumber == 3 && L.size() > 0) {
                //removelast
                int rand = L.removeLast();
                int rand1 = M.removeLast();
                System.out.println("removeLast(" + rand + ")");
                assertEquals(rand,rand1);

            }

        }
    }
    // YOUR TESTS HERE
}
