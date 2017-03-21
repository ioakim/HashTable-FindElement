 package question1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ioakim Ioakim
 */
public class FindElement {

    public static boolean FindElementD(int [][] A,int n, int p){
        
        for(int i=0; i<n; i++){
            for (int j=0; j<n; j++){
                if (A[i][j] == p){
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean FindElementD1(int [][] A, int n, int p){
        
        boolean found = false;
        
        for(int i=0; i<n; i++){
            found = recursionD1(A[i], 0, n, p);
            
            if (found) break;
        }
        
        return found;
    }
    
    public static boolean recursionD1(int [] row, int startPos, int endPos, int p){
        
        // If p is not in the range of this row then check the next one
        if (p < row[0]  || p > row[row.length - 1]){
            return false;
        }
        if (startPos > endPos) return false;
        
        int pivot = (endPos + startPos)/2;
        
        if (row[pivot] == p){
            return true;
        }else if(p > row[pivot]){
            startPos = pivot +1;
        }else{
            endPos = pivot -1;
        }
        return  recursionD1(row, startPos, endPos, p);
    }
    
    public static boolean FindElementD2(int [][] A, int n, int p){
        
        boolean found = false;
        
        //top right position
        int x = 0; //row
        int y = n-1; //column  
        
        found = recursionD2(A, x, y, p, n);
                
        return found;        
    }
    
    public static boolean recursionD2(int [][] A, int x, int y, int p, int n){
        
        //int startPos = A[x][y];
        
//        if (x==n-1 && y==0){//if it has reached bottom left element
//            if (A[x][y] != p)//if element is found
//                return false;
//        }
        
        if (A[x][y] < p && x <A.length -1){
                x++; //go down by only increasing row number
                return recursionD2(A, x, y, p, n);
        }else if (A[x][y] > p && y > 0){
                y--; //go left by only decreasing column number
                return recursionD2(A, x, y, p, n);
        }else{
            return A[x][y] == p;
        }
    }
    
    public static void timingExperiment(int[][] A, int n, int p){
        double[] data = new double[4];
        int reps = 1000;
        double sum = 0;
        double sumSquared = 0;
        
        for (int i = 0; i < reps; i++){
            long t1 = System.nanoTime();
            //FindElementD(A, n, p);
            //FindElementD1(A, n, p);
            FindElementD2(A, n, p);
            long t2 = System.nanoTime() - t1;
            sum += (double)t2/1000000.0;
            sumSquared += (t2/1000000.0) * (t2/1000000.0);
        }
              
        double mean = sum/reps;
        double variance = sumSquared / reps - (mean*mean);
        double stdDev = Math.sqrt(variance);
        data[0] = n;
        data[1] = mean;
        data[2] = variance;
        data[3] = stdDev;
        System.out.format(mean + " \t|\t " + variance + " \t|\t " + stdDev + "\n");
        try {
            generateCSV(data);
        } catch (IOException ex) {
            Logger.getLogger(FindElement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static int[][] generateA(int n){
        
        Random r = new Random();
        int[][] A = new int[n][n];
        int max = 10;
        int min = 5;
        
        int random = r.nextInt(max - min + 1) + min;
        int randomIncrement;
        
        //algorithm for ensuring that the array is 
        //meeting the non-decreasing requirement
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                
                if (i-1 < 0 && j>0){//only check left element
                    while(random < A[i][j-1]){
                        randomIncrement = r.nextInt(10);
                        random += randomIncrement;
                    }
                    min = A[i][j-1];
                    max = min + 5;
                }else if (j-1 < 0 && i>0){//only check element above
                    while(random < A[i-1][j]){
                        randomIncrement = r.nextInt(10);
                        random += randomIncrement;
                    }
                    min = A[i-1][j];
                    max = min + 5;
                }else if (j>0 && i>0){
                    while (random < A[i][j-1] || random < A[i-1][j]){
                        randomIncrement = r.nextInt(10);
                        random += randomIncrement;
                    }
                    min = A[i][j-1];
                    max = min + 5;
                }
                A[i][j] = random;
                random = r.nextInt(max - min + 1) + min;
            }
            random = r.nextInt(max - min + 1) + min;
        }
        /*
        //printing the array
        StringBuilder sb = new StringBuilder();
        for (int[] row : A){
            sb.append(Arrays.toString(row)).append("\n");
        }
        System.out.println(sb.toString());
        */
        return A;
    }
    
    public static void generateCSV(double[] data) throws IOException{
        FileWriter fw = new FileWriter("results.csv", true);
        StringBuilder sb = new StringBuilder();
        for (double t : data){
            sb.append(t).append(",");
        }
        sb.append("\n");
        fw.write(sb.toString());
        fw.flush();
        fw.close();
    }
    
    public static void main(String[] args) {
        
        int [][] A = {
            { 1,  3,  7,   8,   8,   9,  12},
            { 2,  4,  8,   9,  10,  30,  38},
            { 4,  5, 10,  20,  29,  50,  60},
            { 8, 10, 11,  30,  50,  60,  61},
            {11, 12, 40,  80,  90, 100, 111},
            {13, 15, 50, 100, 110, 112, 120},
            {22, 27, 61, 112, 119, 138, 153}     
        };
        
        int n = A.length;
        
        int [] pValues = {4, 12, 110, 5, 6, 111};
        
        for (int i=0; i < pValues.length; i++){
            int p = pValues[i];
            System.out.println(FindElementD(A ,n, p));
        }
        


//        int n = 100;
//        int p = 101;
//        int[][] A = generateA(n);
//        System.out.println(FindElementD2(A, n, p));



//        int p =153;
//        for (int n = 0; n <= 4000;){
//            if (n < 100){
//                n += 10;
//            } else if (n >= 100 && n < 1000){
//                n += 100;
//            } else {
//                n += 1000;
//            }
//            int[][] A = generateA(n);
//            timingExperiment(A, n, p);
//        }
    
    }
}