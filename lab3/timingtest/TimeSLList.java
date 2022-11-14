package timingtest;

import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        int[] listint = new int[]{1000,2000,4000,8000,16000,32000,64000,128000};
        AList<Integer> Ns = new AList<>();
        for (int a: listint)
        {
            Ns.addLast(a);
        }
        int[] listops = new int[]{10000,10000,10000,10000,10000,10000,10000,10000};
        AList<Integer> ops = new AList<>();
        for (int a: listops)
        {
            ops.addLast(a);
        }
        double[] listdou = new double[]{timeToEachN(1000),timeToEachN(2000),timeToEachN(4000),timeToEachN(8000),timeToEachN(16000),timeToEachN(32000),timeToEachN(64000),timeToEachN(128000)};
        AList<Double> times = new AList<>();
        for (double b: listdou)
        {
            times.addLast(b);
        }

        printTimingTable(Ns,times,ops);
    }


    public static double timeToEachN(int n)
    {
        SLList<Integer> L = new SLList<>();

        for (int i =0;i <n;i++)
        {
            L.addLast(1);
        }
        Stopwatch sw = new Stopwatch();
        for (int i =0;i <10000;i++)
        {
            L.getLast();
        }
        double timeInSeconds = sw.elapsedTime();
        return timeInSeconds;
    }

}
