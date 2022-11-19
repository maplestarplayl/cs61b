package deque;
import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

public class MyownTest {
    @Test
    public void test1()
    {
        LinkedListDeque<Integer> L = new LinkedListDeque<>(1);
        ArrayDeque<Integer> M = new ArrayDeque<Integer>();
        int N = 500;
        for (int j = 0;j < 500;j++){
            int operationNumber = edu.princeton.cs.introcs.StdRandom.uniform(0, 6);

            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                M.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int size1 = M.size();
                System.out.println("size: " + size);
            } else if (operationNumber == 2 && L.size() > 0 &&M.size() > 0) {
                //removefirst
                int randVal1 = L.removeFirst();
                 M.removeFirst();
                System.out.println("removefirst(" + randVal1 + ")");
            } else if (operationNumber == 3 &&L.size()>0 && M.size()>0) {
                //removelast
                int rand = L.removeLast();
                M.removeLast();
                System.out.println("removeLast(" + rand + ")");
            }else if (operationNumber == 4 ) {
                //addfirst
                int randVal = StdRandom.uniform(0, 100);
                L.addFirst(randVal);
                M.addFirst(randVal);
                System.out.println("addFirst(" + randVal + ")");
            }else if (operationNumber == 5){
                L.printDeque();
            }

        }




    }






}
