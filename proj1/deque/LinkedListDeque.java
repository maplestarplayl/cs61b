package deque;

import java.util.Iterator;

public class LinkedListDeque<SomeKind> implements Deque<SomeKind>, Iterable<SomeKind> {
    private  class StuffNode
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
        sentFront = new StuffNode(null,null,null);
        sentFront.next = new StuffNode(x,sentFront,null);
        sentBack = new StuffNode(null,null,null);
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
    public int getIndex(SomeKind x)
    {
        return 100;
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

    public void printDeque()
    {
        StuffNode p = sentFront;
        while (p.next != null)
        {
            System.out.print(p.next.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    public Iterator<SomeKind> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<SomeKind> {
        private int wizPos;
        private LinkedListIterator() {
            wizPos = 0;
        }

        public boolean hasNext() {
            return wizPos < size;
        }

        public SomeKind next() {
            SomeKind item = get(wizPos);
            wizPos += 1;
            return item;
        }
    }
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<SomeKind> ol = (Deque<SomeKind>) o;
        if (ol.size() != this.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!(ol.get(i).equals(this.get(i)))) {
                return false;
            }
        }
        return true;
    }
}










