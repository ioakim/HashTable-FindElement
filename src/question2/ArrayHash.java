package question2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author ioakim
 */
public class ArrayHash extends HashTable {
    
    int chainSize = 5;
    int[] counts = new int[capacity];
    Object[][] table = new Object[capacity][];
    
    public ArrayHash(){
        for (int i = 0; i < capacity; i++){
            table[i] = null;
            counts[i] = 0;
        }
    }
    
    @Override
    boolean add(Object obj) {
        int pos = obj.hashCode() % capacity;
        if (table[pos] == null){
            Object[] chain = new Object[chainSize];
            // Add chain in pos
            table[pos] = chain;
            chain[0] = obj;
            counts[pos]++;
            return true;            
        } else {
            if (counts[pos] == table[pos].length){
                // if it's full
                Object[] chainNew = new Object[table[pos].length * 2];
                // copy values over
                System.arraycopy(table[pos], 0, chainNew, 0, table[pos].length);
                table[pos] = chainNew;
                // add obj
                table[pos][counts[pos]] = obj;
                // update count
                counts[pos]++;
                //System.out.println("Chain full, created a new one and added obj");
                return true;
            } else {
                // if there isnt a duplicate
                if (!contains(obj)){
                    // add it in the null position
                    table[pos][counts[pos]] = obj;
                    counts[pos]++;
                    //System.out.println("Added");
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    boolean contains(Object obj) {
        
        boolean found = false;
        int pos = obj.hashCode() % capacity;
        
        for (Object o : table[pos]) {
            if ( o != null){
                // if it's a duplicate
                if(o.equals(obj)) {
                    found = true;
                    break;
                }
            }
        }     
        return found;
    }

    @Override
    boolean remove(Object obj) {
        
        int pos = obj.hashCode() % capacity;
        
        for (int i = 0; i < table[pos].length - 1; i++) {
            if(table[pos][i].equals(obj)){
                table[pos][i] = null;//remove by setting element to null
                counts[pos]--;//decreaase count
                
                //shift elements up copying the value of next item into the
                //removed position and setting the next position to null
                for (int j = i; j < table[pos].length - 1; j++){
                    table[pos][j] = table[pos][j+1];
                    if (table[pos][j+1] == null) break;
                    table[pos][j+1] = null;
                }
                return true;
            }
        }
        return false;
    }
    
    public static int[] generateArray(int n){
        int[] A = new int[n];
        Random r = new Random();
        for (int i = 0; i < n; i++){
            A[i] = Math.abs(r.nextInt());
        }
        return A;
    }
    
    public static void timingExperiment(int[] A, ArrayHash h, int n){
        for (int i = 0; i < n; i++){
            h.add(A[i]);
        }
        //h.remove(34);
    }
    
    public static void main(String[] args){
        ArrayHash ht = new ArrayHash();
//        System.out.println(ht.add(1));
//        System.out.println(ht.add(1));
//        System.out.println(ht.contains(1));
//        System.out.println(ht.remove(1));

        for (int i=0; i<50; i++){
            ht.add(i);
           if(i==49){
               System.out.println("hi");
           };
        }
        
        
        
//        int n = 50000;
//        ArrayHash ht = new ArrayHash();
//        HashSet hs = new HashSet();
//        int[] A = generateArray(n);
//        timingExperiment(A, ht, n);
    }
}
