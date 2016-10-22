/*
 * Assignment 4 Compressed Literature 2
 * 
 * Louis Yang, Michael Wilson
 * Extra Credits Finished please check!
 *
 */
package code;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Priority Class implementation.
 * 
 * DID ALL THE EXTRA CREDIT!!!
 * @author Louis Yang Michael Wilson
 * @version 1.0
 */
public class MyPriorityQueue<E extends Comparable<E>> {
	/** Arraylist Implementation to Replicate the Heap Structure.*/
	private ArrayList<E> myPq;
	/** The size of the priority queue.*/
	private int mySize;
	
	/** Constructor for the priority queue. */
	public MyPriorityQueue() {
		myPq = new ArrayList<E>();
		mySize = 0; 
	}
	
	/** Adding the element to the priority queue.*/
	public void add(E element) {
		mySize++;
		myPq.add(element);
		if (mySize > 1) {
			int index = mySize - 1;
			int stop = 0;
			while(index != 0 && stop == 0) {
				int root = (index - 1) / 2;
				if ((index - 1) % 2 == 0) {
					index = purculateUp(index, root);
				} else {
					if (myPq.get(index).compareTo(myPq.get(index - 1)) <= 0) {
						index = purculateUp(index, root);
					} else {
						index = purculateUp(index - 1, root);
					}
				}
				if (index != root) {
					stop = 1;
				}
			}
		}
	}
	
	/** Helper method for the priority queue add.*/
	private int purculateUp(int node, int root) {
		int escalated = node;
		if (myPq.get(node).compareTo(myPq.get(root)) < 0) {
			E temp = myPq.get(root);
			myPq.set(root, myPq.get(node));
			myPq.set(node, temp);
			escalated = root;
		}
		return escalated;
	}
	
	/** Removing the top element from the priority queue.*/
	public E remove() {
		if (mySize == 0) {
			throw new NoSuchElementException();
		} 
		
		E temp = myPq.get(0);
		if (mySize > 1) {
			myPq.set(0, myPq.get(mySize - 1));
			myPq.remove(mySize - 1);
			mySize--;
			int position = 0;
			int stop = 0;
			while (checkPosition(position * 2 + 1) && stop == 0) {
				int positionCopy = position;
				if (checkPosition(position * 2 + 2)) {
					if (myPq.get(position * 2 + 2).compareTo(myPq.get(position * 2 + 1 )) 
							<= 0) {
						position = purculateDown(position, position * 2 + 2);
					} else {
						position = purculateDown(position, position * 2 + 1);
					}
				} else {
					position = purculateDown(position, position * 2 + 1);
				}
				if (positionCopy == position) {
					stop = 1;
				}
			}
		} else {
			myPq.remove(0);
			mySize = 0;
		}
		return temp;
	}
	
	/** Checks the position of the current queue.*/
	private boolean checkPosition(int node) {
		return node < mySize; 
	}
	/** Helper method to assist with the removing the element from the priority queue. */
	private int purculateDown(int root, int node) {
		int movedDown = root;
		if (myPq.get(node).compareTo(myPq.get(root)) < 0) {
			E temp = myPq.get(root);
			myPq.set(root, myPq.get(node));
			myPq.set(node, temp);
			movedDown = node;
		}
		return movedDown;
	}
	
	/**
	 * Gets the size of the priority queue.
	 * 
	 * @return the size of the current array.
	 */
	public int size() {
		return mySize;
	}
	
	/**
	 * Gets the string representation of the priority queue.
	 * 
	 * @return the string representation of the priority queue.
	 */
	public String toString() {
		return myPq.toString();
	}
}
