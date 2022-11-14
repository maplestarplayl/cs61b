package deque;

public class LinkedListDeque<SomeKind> {
    private class StuffNode
    {
        public SomeKind item;
        public StuffNode before;
        public StuffNode next;

        public StuffNode(SomeKind i,StuffNode bef,StuffNode nex)
        {
            item = i;
            before = bef;
            next = nex;
        }
    }

    private StuffNode sentFront;
    private int size;
    private StuffNode sentBack;

    public LinkedListDeque ()
    {
        sentFront = new StuffNode(null,null,null);
        sentBack = new StuffNode(null,null,null);
    }

    public LinkedListDeque(SomeKind x)
    {
        sentFront = new StuffNode(x,null,null);
        sentFront.next = new StuffNode(x,sentFront,null);
        sentBack = new StuffNode(x,null,null);
        sentBack.next = sentFront.next;
        size = 1;
    }

    public void addFirst (SomeKind x)
    {
        int nowsize = size;
        if (nowsize == 0)
        {
            sentFront.next = new StuffNode(x,sentFront,null);
            sentBack.next = sentFront.next;
            size += 1;
        }
        else
        {
            StuffNode newly = new StuffNode(x,sentFront,sentFront.next);
            sentFront.next.before = newly;
            sentFront.next = newly;
            size += 1;
        }

    }

    public void addLast (SomeKind x)
    {
        if (size == 0)
        {
            addFirst(x);
        }
        else
        {
            StuffNode nowlast = sentBack.next;
            nowlast.next = new StuffNode(x,nowlast,null);
            sentBack.next = nowlast.next;
            size += 1;
        }
    }

    public boolean isEmpty()
    {
        if (size == 0)
        {
            return true;
        }
        return false;
    }

    public int size()
    {
        return size;
    }
    public SomeKind removeFirst()
    {
        if (size <= 0)
        {
            return null;
        }
        if (size == 1)
        {
            SomeKind num = sentFront.next.item;
            sentFront.next.before = null;
            sentFront.next = null;
            sentBack.next = null;
            size -= 1;
            return num;
        }
        else
        {
            SomeKind num = sentFront.next.item;
            StuffNode p = sentFront.next;
            p.next.before = sentFront;
            sentFront.next = p.next;
            //lose this p node
            p.before =null;
            p.next = null;
            size -= 1;
            return num;
        }
    }

    public SomeKind removeLast()
    {
        if (size == 0)
        {
            return null;
        }
        else if (size == 1)
        {
            return removeFirst();
        }
        else
        {
            SomeKind num = sentBack.next.item;
            StuffNode p = sentBack.next;
            sentBack.next = p.before;
            p.before.next = null;
            //lose the last node
            p.next =null;
            p.before = null;
            size -= 1;
            return num;
        }

    }

    public SomeKind get(int index)
    {
        if (index >= size)
        {
            return null;
        }
        StuffNode p = sentFront;
        while (index != 0)
        {
            index -= 1;
            p = p.next;
        }
        return p.next.item;
    }

    public void getRecursive(int index)
    {
        // i don not know
    }







}
