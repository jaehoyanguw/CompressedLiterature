/*
 * Assignment 4 Compressed Literature 2
 * 
 * Louis Yang, Michael Wilson
 * Extra Credits Finished please check!
 *
 */
package code;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * The Hash Table implementation for the Compress
 * 
 * @author Louis Yang
 * @version 1.0
 */
public class MyHashTable<K,V> {
	/** The Hash Table. */
	private ArrayList<TableContents> myTable;
	/** The statistic results for the hash table.*/
	private ArrayList<Integer> myStatistic;
	/** Used only once for implementing the keySet method. */
	private Set<K> mySet;
	/** Number of entries. */
	private int myEntries;
	/** Total number of probes. */
	private int myTotalProbe;
	/** Maximum bucket size of the hash table. */
	private int myCapacity;
	/** Maximum probing done. */
	private int myMaxProbe;
	/**
	 * Constructor for the MyHashTable Class.
	 * 
	 * @param the bucket capacity of the hash table.
	 */
	public MyHashTable(int capacity) {
		myTable = new ArrayList<TableContents>(capacity);
		myStatistic = new ArrayList<Integer>();
		mySet = new TreeSet<K>();
		myEntries = 0;
		myTotalProbe = 0;
		myCapacity = capacity;
		myMaxProbe = 0;
		for (int i = 0; i < myCapacity; i++) {
			myTable.add(null);
		}
		myStatistic.add(0);
	}
	
	/**
	 * Put the elements into the corresponding bucket in the hash table.
	 * 
	 * @param searchKey the search key.
	 * @param newValue the new value value.
	 */
	public void put(K searchKey, V newValue) {
		myEntries++;
		TableContents addition = new TableContents(searchKey, newValue);
		int originalIndex = hash(searchKey);
		int offCount = 0;
		while(myTable.get(originalIndex) != null 
				&& !myTable.get(originalIndex).myKey.equals(searchKey)
				&& offCount != myCapacity) {
			originalIndex++;
			offCount++;
			if (originalIndex == myCapacity) {
				originalIndex = 0;
			}
		}
		myTable.set(originalIndex, addition);
		mySet.add(searchKey);
		
		myTotalProbe += offCount;
		if (offCount > myMaxProbe) {
			for(int i = 0; i < offCount - myMaxProbe - 1; i++) {
				myStatistic.add(0);
			}
			myStatistic.add(1);
			myMaxProbe = offCount;
		} else {
			myStatistic.set(offCount, myStatistic.get(offCount) + 1);
		}
	}
	
	//******************** UNCOMMENT TO TEST AND USE *********************************
	//******************** QUADRATIC PROBING METHOD  *********************************
	
//	public void quadraticPut(K searchKey, V newValue) {
//		myEntries++;
//		TableContents addition = new TableContents(searchKey, newValue);
//		int originalIndex = hash(searchKey);
//		int offCount = 0;
//		while (myTable.get(originalIndex) != null 
//				&& !myTable.get(originalIndex).myKey.equals(searchKey) 
//				&& offCount != myCapacity) {
//			offCount++;
//			originalIndex = (originalIndex + (offCount * offCount)) % myCapacity;
//		}
//		myTable.set(originalIndex, addition);
//		mySet.add(searchKey);
//		
//		myTotalProbe += offCount;
//		if(offCount > myMaxProbe) {
//			for (int i  = 0; i < offCount - myMaxProbe - 1; i ++) {
//				myStatistic.add(0);
//			}
//			myStatistic.add(1);
//			myMaxProbe = offCount;
//		} else {
//			myStatistic.set(offCount, myStatistic.get(offCount) + 1);
//		}
//		
//	}
	
	/** 
	 * Gets the value of the search key.
	 * 
	 * @param searchKey the search key.
	 * @return the value;
	 */
	public V get(K searchKey) {
		int originalIndex = hash(searchKey);
		int offcount = 0;
		while (myTable.get(originalIndex) != null 
				&& !myTable.get(originalIndex).myKey.equals(searchKey) 
				&& offcount != myCapacity) {
			originalIndex++;
			offcount++;
			if (originalIndex == myCapacity) {
				originalIndex = 0;
			}
		}
		if (offcount == myCapacity || myTable.get(originalIndex) == null) {
			return null;
		} else {
			return myTable.get(originalIndex).myValue;
		}
	}
	
	//******************** UNCOMMENT TO TEST AND USE *********************************
	//******************** QUADRATIC PROBING METHOD  *********************************
	
//	public V quadraticGet(K searchKey) {
//		int originalIndex = hash(searchKey);
//		int offcount = 0;
//		while (myTable.get(originalIndex) != null 
//				&& !myTable.get(originalIndex).myKey.equals(searchKey) 
//				&& offcount != myCapacity) {
//			offcount++;
//			originalIndex = (originalIndex + (offcount * offcount)) % myCapacity;
//		}
//		if (offcount == myCapacity || myTable.get(originalIndex) == null) {
//			return null;
//		} else {
//			return myTable.get(originalIndex).myValue;
//		}
//	}
	
	/** Prints the statistics of the histogram. */
	public void stats() {
        System.out.println("Hash Table Stats");
        System.out.println("================");
        System.out.println("Number of Entries: " + myEntries);
        System.out.println("Number of Buckets: " + myCapacity);
        System.out.println("Histogram of Probes: " + myStatistic.toString());
        System.out.println("Fill Percentage: " + getFillPercentage() + "%");
        System.out.println("Max Linear Prob: " + myMaxProbe);
        System.out.println("Average Linear Prob: " + getAverageProbing());
    }
	
	//******************** UNCOMMENT TO TEST AND USE *********************************
	//******************** QUADRATIC PROBING METHOD  *********************************
//	public void quadraticStats() {
//        System.out.println("Hash Table Stats");
//        System.out.println("================");
//        System.out.println("Number of Entries: " + myEntries);
//        System.out.println("Number of Buckets: " + myCapacity);
//        System.out.println("Histogram of Probes: " + myStatistic.toString());
//        System.out.println("Fill Percentage: " + getFillPercentage() + "%");
//        System.out.println("Max Quadratic Prob: " + myMaxProbe);
//        System.out.println("Average Quadratic Prob: " + getAverageProbing());
//    }
	
	/**
	 * Gets the fill percentage of the hash table.
	 *
	 * @return the string representation of the fill percentage of the hash table.
	 */
	private String getFillPercentage() {
		double fillPercentage = myEntries * 100.0 / myCapacity;
		return String.format("%.6f", fillPercentage);
	}
	
	/**
	 * Gets the average probing operations.
	 * 
	 * @return the string representation of the average probing done on the hash table.
	 */
	private String getAverageProbing() {
		double averageProbing = myTotalProbe * 1.0 / myEntries;
		return String.format("%.6f", averageProbing);
	}
	
	/** 
	 * Checks to see if the key exists in the hash table.
	 * 
	 * @param searchKey the search key.
	 * @return whether or not the key exist.
	 */
	public boolean containsKey(K searchKey) {
		return get(searchKey) != null;
//		return mySet.contains(searchKey);
	}
	
	/**
	 * gets the key set of the hash table.
	 * 
	 * @return the key set of the hash table. 
	 */
	public Set<K> keySet() {
		return mySet;
	}
	
	/** 
	 * Gets the string representation of the hash table.
	 * 
	 * @return the string representation of the hash table.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		if (myTable.size() > 1) {
			sb.append("[");
			while(myTable.get(i) ==  null && i < myTable.size()) {
				i++;
			}
			sb.append(myTable.get(i));
			for (int s = i + 1; s < myTable.size(); s++) {
				if (myTable.get(s) != null) {
					sb.append(", ");
					sb.append(myTable.get(s).toString());
				}
			}
			sb.append("]");
		}
		return sb.toString();
	}
	
	/**
	 * Gets the hash value of the key.
	 *
	 * @param the key the key.
	 * @return the hash value of the key.
	 */
	private int hash(K key) {
		return Math.abs(key.hashCode() % myCapacity);
	}
	
	/** Inner Class for the HashTable bucket contents. */
	private class TableContents {
		/** The key. */
		private K myKey;
		/** The value. */
		private V myValue;
		/**
		 * The constructor of the TableContents inner class.
		 * 
		 * @param the key.
		 * @param the value.
		 */
		public TableContents(K key, V value) {
			myKey = key;
			myValue = value;
		}
		
		/**
		 * Gets the string representation.
		 *
		 * @return the string representation of the TableContent.
		 */
		@Override 
		public String toString() {
			return "(" + myKey + ", " + myValue + ")";
		}	
	}
}
