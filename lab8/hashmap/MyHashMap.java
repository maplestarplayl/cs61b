package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }
    private Node getNode(K key){
        int hash = hash(key);
        for (Node node : buckets[hash]){
            if (node.key.equals(key)){
                return node;
            }
        }
        return null;
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private double loadFactor;

    private int NodeNumber;
    private int size;
    private Set<K> keys;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        buckets = new Collection[16];
        for (int i = 0; i < 16; i++) {
            buckets[i] = createBucket();
        }
        loadFactor = 0.75;
        NodeNumber = 16;
        keys = new HashSet<>();
    }

    public MyHashMap(int initialSize) {
        buckets = new Collection[initialSize];
        for (int i = 0; i < initialSize; i++) {
            buckets[i] = createBucket();
        }
        loadFactor = 0.75;
        NodeNumber = initialSize;
        keys = new HashSet<>();
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = new Collection[initialSize];
        for (int i = 0; i < initialSize; i++) {
            buckets[i] = createBucket();
        }
        loadFactor = maxLoad;
        NodeNumber = initialSize;
        keys = new HashSet<>();
    }

    private int hash(K key){
        int hash = key.hashCode();
        return Math.floorMod(hash,NodeNumber);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key,value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return null;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    public void clear(){
        for (int i = 0; i < NodeNumber; i++) {
            buckets[i].clear();
        }
        keys.clear();
        size = 0;
    }

    public boolean containsKey(K key){
        int hash = hash(key);
        for(Node node : buckets[hash]){
            if (node.key.equals(key)){
                return true;
            }
        }
        return false;
    }
    private void resizeTime2(){
        MyHashMap<K,V> now = new MyHashMap<>(NodeNumber*2,loadFactor);
        for (K key: keys){
            now.put(key,get(key));
        }
        this.buckets = now.buckets;
        this.NodeNumber = now.NodeNumber;
    }
    public void put(K key, V value){
        int hash = hash(key);
        if (containsKey(key)){
            getNode(key).value = value;
        }
        else{
            buckets[hash].add(createNode(key,value));
            size += 1;
            keys.add(key);
            if (size * 1.0 / NodeNumber >= loadFactor) {
                resizeTime2();
            }
        }
    }

    public int size(){
        return size;
    }
    public V get(K key){
        if (containsKey(key)){
            return getNode(key).value;
        }
        return null;
    }

    public Set<K> keySet(){
        return keys;
    }
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
