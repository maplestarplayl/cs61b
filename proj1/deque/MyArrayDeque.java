package deque;

public class MyArrayDeque<SomeKind> {
    private SomeKind[] items;
    private int leftsize;
    private int rightsize;


    public MyArrayDeque()
    {
        items = (SomeKind[])  new Object[8];
        leftsize = 0;
        rightsize = 0;
    }

    private void resize (int capacity)
    {
        int totalsize = leftsize + rightsize;
        SomeKind[] a = (SomeKind[]) new Object[capacity];
        if (leftsize == 0 & rightsize != 0)
        {
            int j = returnFirstIndex(items);
            for (int i = capacity / 2 + returnFirstIndex(items) - items.length / 2 ; i <= totalsize;i++,j++)
            {
                a[i] = items[j];
            }
            items = a;
        }
        else {
            for (int i = capacity / 2 - leftsize, j = returnFirstIndex(items); i < totalsize+capacity / 2 - leftsize; i++, j++) {
                a[i] = items[j];
            }
            items = a;
        }

    }
    private int returnFirstIndex(SomeKind[] items)
    {
        for (int i = 0; i < items.length ;i++)
        {
            if (items[i] != null)
            {
                return i;
            }
        }
        return -1;
    }

    public void addFirst(SomeKind x)
    {
        if (leftsize + rightsize == 0)
        {
            items[items.length/2 - 1] = x;
            return;
        }

        else if (leftsize == 0 &rightsize != 0 & items[items.length/2] == null)
        {
            int index = returnFirstIndex(items);
            items[index - 1] = x;
            rightsize += 1;
        }


        else if (leftsize == items.length / 2)
        {
            resize(items.length *2);
            items[items.length - leftsize - 1] = x;
            leftsize += 1;
        }
        else if (items[items.length / 2 -leftsize ] == null)
        {
            items[items.length / 2 -leftsize ] = x;
            leftsize += 1;
        }
        else
        {
            items[items.length / 2 -leftsize -1] = x;
            leftsize += 1;
        }
    }

    public void addLast (SomeKind x)
    {
        if (leftsize + rightsize == 0)
        {
            items[items.length/2] = x;
            return;
        }


        else if (rightsize == 0 && leftsize != 0 && items[items.length/2 -1] == null)
        {
            int index = returnFirstIndex(items);
            items[index+leftsize] = x;
            leftsize += 1;
        }

        else if (rightsize == items.length / 2)
        {
            resize(items.length *2);
            items[items.length/2 + rightsize ] = x;
            rightsize += 1;
        }
        else if (items[items.length / 2 + rightsize -1] == null)
        {
            items[items.length / 2 + rightsize -1] = x;
            rightsize += 1;
        }
        else
        {
            items[items.length / 2 + rightsize ] = x;
            rightsize += 1;
        }
    }

    public boolean isEmpty()
    {
        if (leftsize ==0 && rightsize == 0)
        {
            return true;
        }
        return false;
    }

    public int size()
    {
        return leftsize + rightsize;
    }

    public SomeKind removeFirst()
    {
        if (leftsize + rightsize == 0)
        {
            return null;
        }
        else if (leftsize == 0 && rightsize != 0)
        {
            int index = returnFirstIndex(items);
            SomeKind x = items[index];
            items[index] = null;
            rightsize -= 1;
            return x;
        }

        else {
            if (leftsize + rightsize <= items.length /4 && leftsize + rightsize >2)
            {
                resize(items.length/4);
            }
            SomeKind x = items[items.length / 2 - leftsize ];
            items[items.length / 2 - leftsize ] =null;
            leftsize -= 1;
            return x;
        }
    }

    public SomeKind removeLast()
    {
        if (leftsize + rightsize == 0)
        {
            return null;
        }
        else if (rightsize == 0 && leftsize != 0 )
        {
            int index = returnFirstIndex(items) + leftsize - 1;
            SomeKind x = items[index];
            items[index] = null;
            leftsize -= 1;
            return x;

        }

        else {
            if (leftsize + rightsize <= items.length /4 && leftsize + rightsize >2)
            {
                resize(items.length/4);
            }
            SomeKind x = items[items.length / 2 + rightsize-1];
            items[items.length / 2 + rightsize-1] =null;
            rightsize -= 1;
            return x;
        }
    }
    public SomeKind get(int index)
    {
        int index1 = returnFirstIndex(items);
        return items[index+index1];

    }




}
