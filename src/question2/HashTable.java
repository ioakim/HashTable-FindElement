/*
Hash Table interface for the DS&A labs, week 1, semester 2. Note the emphasis here
is to get you to implement the algorithms, not write fancy code (although feel free to
do so! There are exercises for Programming 2 to engineer it more.
 */
package question2;

/**
 *
 * @author ajb
 */
public abstract class HashTable {
    int capacity=10;
    int size=0;
    
    public int size(){ return size;}
    
/**
 * Adds the specified element to this hash table if it is not already present 
 * 
 * @param obj
 * @return true if the element is successfully added
 */    
   abstract boolean add(Object obj);
    
/**
 * 
 * @param obj
 * @return  true if this hash table contains the specified element
 */    
   abstract boolean contains(Object obj);
/**
 * 
 * @param obj
 * @return Removes the specified element from this set if it is present 
 */
  abstract  boolean remove(Object obj);
    
}
