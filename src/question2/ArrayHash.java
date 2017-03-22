package question2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import question1.FindElement;
import static question1.FindElement.FindElementD2;
import static question1.FindElement.generateCSV;

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
        if (table[pos] == null){//if the pos is empty
            Object[] chain = new Object[chainSize];//create new chain
            table[pos] = chain;// Add chain in pos
            chain[0] = obj;//add the object in the first position of the chain
            counts[pos]++;//increase the position count
            return true;            
        } else {
            if (counts[pos] == table[pos].length){
                // if it's full a new chain double the sixe is created
                Object[] chainNew = new Object[table[pos].length * 2];
                // the values are copied into the new chain
                System.arraycopy(table[pos], 0, chainNew, 0, table[pos].length);
                table[pos] = chainNew;//the new chain ovverrides the previous one
                table[pos][counts[pos]] = obj;//add the element to the next null position
                counts[pos]++;;//increase the position count
                return true;
            } else {
                // if there isnt a duplicate
                if (!contains(obj)){
                    // add it in the next null position
                    table[pos][counts[pos]] = obj;
                    counts[pos]++;//increase the position count
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
        
        for (Object o : table[pos]) {//traverses through the chain
            if ( o != null){
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
        
        for (int i = 0; i < counts[pos]-1; i++) {
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
    
    public static int[] generateA(int n){
        int[] A = new int[n];
        Random r = new Random();
        for (int i = 0; i < n; i++){
            A[i] = Math.abs(r.nextInt());
        }
        return A;
    }
    
    public static void timingExperiment(int[] A, HashTable h, int n){
        double[] data = new double[5];
        int reps = 1000;
        double sumAdd = 0;
        double sumSquaredAdd = 0;
        double sumRemove = 0;
        double sumSquaredRemove = 0;
        for (int i = 0; i < reps; i++){
            long t1 = System.nanoTime();
            for (int j = 0; j < n; j++){
                h.add(A[j]);
            }
            long t2 = System.nanoTime() - t1;
            sumAdd += (double)t2/1000000.0;
            sumSquaredAdd += (t2/1000000.0) * (t2/1000000.0);
        
            long t3 = System.nanoTime();
            for (int j = 0; j < n; j++){
                h.remove(A[j]);
            }
            long t4 = System.nanoTime() - t3;
            sumRemove += (double)t4/1000000.0;
            sumSquaredRemove += (t4/1000000.0) * (t4/1000000.0);
        }
        double meanAdd = sumAdd/reps;
        double varianceAdd = sumSquaredAdd / reps - (meanAdd*meanAdd);
        double stdDevAdd = Math.sqrt(varianceAdd);
        double meanRemove = sumRemove/reps;
        double varianceRemove = sumSquaredRemove / reps - (meanRemove*meanRemove);
        double stdDevRemove = Math.sqrt(varianceRemove);
        data[0] = n;
        data[1] = meanAdd;
        data[2] = stdDevAdd;
        data[3] = meanRemove;
        data[4] = stdDevRemove;
        System.out.format(meanAdd + " \t|\t " + varianceAdd + " \t|\t " + stdDevAdd + "\n");
        System.out.format(meanRemove + " \t|\t " + varianceRemove + " \t|\t " + stdDevRemove + "\n");
        try {
            generateCSV(data);
            //h.remove(34);
        } catch (IOException ex) {
            Logger.getLogger(ArrayHash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void generateCSV(double[] data) throws IOException{
        FileWriter fw = new FileWriter("hashresults.csv", true);
        StringBuilder sb = new StringBuilder();
        for (double t : data){
            sb.append(t).append(",");
        }
        sb.append("\n");
        fw.write(sb.toString());
        fw.flush();
        fw.close();
    }
    
    public static void main(String[] args){
        ArrayHash ht = new ArrayHash();
        HashSet hs = new HashSet();
//        System.out.println(ht.add(1));
//        System.out.println(ht.add(1));
//        System.out.println(ht.contains(1));
//        System.out.println(ht.remove(1));

        for (int n = 0; n < 50000;){
            if (n < 10000){
                n += 1000;
            }else if (n >= 10000){
                n += 5000;
            }
            int[] A = generateA(n);
            timingExperiment(A,ht, n);
        }
        
        
        
//        int n = 50000;
//        ArrayHash ht = new ArrayHash();
//        HashSet hs = new HashSet();
//        int[] A = generateArray(n);
//        timingExperiment(A, ht, n);
    }
}
