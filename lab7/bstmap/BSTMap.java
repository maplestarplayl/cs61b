package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends  Comparable,V> implements Map61B<K,V>{

    public int size;
    private BST bst;
    private class BST<K extends Comparable,V> {
        private class node{
            K keys;
            V vals;
            node(K key ,V val){
                keys = key;
                vals = val;
            }
            V returnValue(){
                return this.vals;
            }
            K returnKey(){
                return this.keys;
            }
        }
        node Node;
        BST left;
        BST right;

        BST(K key,V val,BST left0,BST right0){

            Node = new node(key,val);
            left = left0;
            right = right0;
        }

        V returnVal(){
            return this.Node.returnValue();
        }
        K returnKey(){
            return this.Node.returnKey();
        }
        void changeVal(V val){
            this.Node.vals = val;
        }
        BST get(K key){
            if (key != null && key.equals(this.returnKey())){
                return this;
            }
            else if (this.left != null && key.compareTo(this.returnKey()) < 0 ){
                return left.get(key);
            }
            else if (this.right != null && key.compareTo(this.returnKey()) >0 ){
                return right.get(key);
            }
            else{
                return null;
            }
        }



    }




    public void clear(){
        size = 0;
        bst = new BST(null,null,null,null);
    }

    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key){
        if (size() == 0) {
            return false;
        }
        return bst.get(key) != null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key){
        if (size() == 0){
            return null;
        }
        else if (bst.get(key) == null)
        {
            return null;
        }
        else {
            BST needbst = bst.get(key);
            return (V) needbst.returnVal();
        }
    }

    /* Returns the number of key-value mappings in this map. */
    public int size(){
        return this.size;
    }

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value){
        if (bst == null){
            bst = new BST(key,value,null,null);
            size += 1;
        }
        else{
            putHelper(bst,key,value);
            size += 1;
        }
    }
    private BST putHelper(BST t,K key,V val) {
        if (t == null) {
            t  = new BST(key, val, null, null);
            return t;
        } else if (key.compareTo(t.returnKey()) < 0 ) {
            t.left = putHelper(t.left, key, val);
        } else if (key.compareTo(t.returnKey()) > 0 ) {
            t.right = putHelper(t.right, key, val);
        }
        return t;
    }

    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    public Set<K> keySet(){
        throw new UnsupportedOperationException();
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key){
        throw new UnsupportedOperationException();
    }

    public V remove(K key, V value){
        throw new UnsupportedOperationException();
    }

    public Iterator<K> iterator() {
        return new BSTMapIter();
    }
    private class BSTMapIter implements Iterator<K>{
        public boolean hasNext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public K next() {
            throw new UnsupportedOperationException();
        }
    }

}
