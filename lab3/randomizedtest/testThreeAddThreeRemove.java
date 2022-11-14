package randomizedtest;

import static org.junit.Assert.*;

import org.junit.Test;
import timingtest.AList;

public class testThreeAddThreeRemove {
    @Test
    public void test1()
    {
        AList<Integer> l1 = new AList<>();
        BuggyAList<Integer> l2 = new BuggyAList<>();
        l1.addLast(4);
        l1.addLast(5);
        l1.addLast(6);
        l2.addLast(4);
        l2.addLast(5);
        l2.addLast(6);
        assertEquals(l1,l2);

    }





}
