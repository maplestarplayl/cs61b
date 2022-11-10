/** Class that prints the Collatz sequence starting from a given number.
 *  @author YOUR NAME HERE
 */
public class BreakContinue {
    public static void windowPosSum(int[] a, int n) {
        for (int i = 0; i <= a.length - 1 ; i++)
        {
            if (a[i] > 0 && i+n <= a.length - 1 )
            {
                for (int j = i+1;j<=i+n ; j++)
                {
                    a[i] = a[i] + a[j];
                }
            }
            else if (a[i] < 0)
            {
                continue;
            }
            else if (a[i] > 0 && i + n > a.length - 1)
            {
                for(int k = i+1;k<=a.length-1;k++)
                {
                    a[i] = a[i] +a[k];
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] a = {1, 2, -3, 4, 5, 4};
        int n = 3;
        windowPosSum(a, n);

        // Should print 4, 8, -3, 13, 9, 4
        System.out.println(java.util.Arrays.toString(a));
    }
}