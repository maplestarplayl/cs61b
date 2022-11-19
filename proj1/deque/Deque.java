package deque;

public interface Deque<SomeKind> {

    void addFirst (SomeKind item);
    void addLast(SomeKind item);
    int size();
    default boolean isEmpty() {
        return size() == 0;
    }

    SomeKind removeFirst();
    SomeKind removeLast();
    SomeKind get(int index);

    int getIndex(SomeKind x);
}

