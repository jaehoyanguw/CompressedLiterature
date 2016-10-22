/*
 * Assignment 4 Compressed Literature 2
 * 
 * Louis Yang
 * Extra Credits Finished please check!
 *
 */
package code;

/**
 * Coding Tree class used for Compressed Literature Program.
 * 
 * @author Louis Yang
 * @version 1.0
 *
 */
public class CodingTree {
	/** Coding Tree root. */
	private CodingNode myRoot;
	/** Priority Queue Implementation. */
	private MyPriorityQueue<CodingNode> myPq;
	/** Hash Table implementation to store the word and its paths in the coding tree. */
	private MyHashTable<String, String> myTable;
	/** Hash Table implementation to store the word and its frequency. */
	private MyHashTable<String, Integer> myFrequency;
	/** The binary encoded message of the file contents. */
	private String encodedMessage;
	/**
	 * The constructor for the Coding Tree Class
	 * 
	 * @param message the contents of the file.
	 */
	public CodingTree(String message) {		
		myPq = new MyPriorityQueue<CodingNode>();
		encodedMessage = "";
		myTable = new MyHashTable<String, String>(32768);
		myFrequency = new MyHashTable<String, Integer>(32768);
		
		readMessage(message);
		makeNodes();
		makeTree();
		makePaths(myRoot, "");
		encodedMessage = codedMessage(message);
		myTable.stats();
//****Disable the comments to use the quadraticStats********** 
//		myTable.quadraticStats();
	}
	
	/**
	 * Gives the string representation of the encoded binary contents of the file.
	 * 
	 * @return the string representation of the encoded binary contents.
	 */
	public String encodedFile() {
		return encodedMessage;
	}
	
	/**
	 * Gives the map of the string componenents of the file with its corresponding location.
	 * 
	 * @return the map of the string and its corresponding location in the coding tree.
	 */
	public MyHashTable<String, String> mapping() {
		return myTable;
	}
	
	/**
	 * Decodes the encoded file message into its original content form.
	 * 
	 * @param bits the binary presentation of the encoded file.  
 	 * @param codes the mapping of the strings to its corresponding location.
	 */
	public String decode (String bits, MyHashTable<String, String> codes) {
		MyHashTable<String, String> flippedCodes = new MyHashTable<String, String>(32768);
		StringBuilder sb = new StringBuilder();
		for (String key : codes.keySet()) {
			String keyChar = codes.get(key);
			flippedCodes.put(keyChar, key);
		}
		int begin = 0;
		int end = 0;
		while (end <= bits.length()) {
			String pathes = bits.substring(begin, end);
			if (flippedCodes.containsKey(pathes)) {
				begin = end;
				sb.append(flippedCodes.get(pathes));
				end++;
			} else {
				end++;
			}
		}
		if (begin <= bits.length()) {
			String lastBits = bits.substring(begin);
			if (flippedCodes.containsKey(lastBits)) {
				sb.append(flippedCodes.get(lastBits));
			}
		}
		return sb.toString();
	}
	
	/**
	 * Reads the contents of the file and adds different words and its frequency.
	 * 
	 * @param message the string contents of the file.
	 */
	private void readMessage(String message) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			char temp = message.charAt(i);
			if (wordComponent(temp)) {
				sb.append(temp);
			} else {
				String word = sb.toString();
				if (word.length() != 0) {
					if (myFrequency.containsKey(word)) {
						int wordFrequency = myFrequency.get(word);
						myFrequency.put(word, wordFrequency + 1);
					} else {
						myFrequency.put(word, 1);
					}
				}
				String nonWord = String.valueOf(temp);
				if (myFrequency.containsKey(nonWord)) {
					int charFrequency = myFrequency.get(nonWord);
					myFrequency.put(nonWord, charFrequency + 1);
				} else {
					myFrequency.put(nonWord, 1);
				}
				sb = new StringBuilder();
			}
		}
		//System.out.println(myFrequency.toString());
	}
	
	/**
	 * Checks to see if the the character is part of a word component.
	 *
	 * @param a the character to be checked for word component.
	 * @return whether or not the character is part of a word component.
	 */
	private boolean wordComponent (char a) {
//		boolean isWordComponent = false;
//		int ascii = a;
//		if ((ascii >= 48 && ascii <= 57) || (ascii >= 65 && ascii <= 90) 
//				|| (ascii >= 97 && ascii <= 122) || (ascii == 39) || (ascii == 45)) {
//			isWordComponent = true;
//		}
//		return isWordComponent;
		return (Character.isLetter(a)) || (Character.isDigit(a)) || (a=='\'') || (a=='-');
	}
	
	/**
	 * Creates the nodes with the frequency of the different words. 
	 */
	private void makeNodes() {
		for (String key : myFrequency.keySet()) {
			CodingNode root = new CodingNode(key, myFrequency.get(key));
			myPq.add(root);
		}
	}
	
	/**
	 *	Makes the tree out of the nodes created.
	 */
	private void makeTree() {
		while(myPq.size() > 1) {
			CodingNode left = myPq.remove();
			CodingNode right = myPq.remove();
			CodingNode empty = new CodingNode(null, left.weight + right.weight, left, right);
			myPq.add(empty);
		}
		myRoot = myPq.remove();
	}
	
	/**
	 * Makes the paths for the location of each word in its corresponding location in the tree.
	 *  
	 * @param root the root of the Coding Tree.
	 * @param paths the paths of the different nodes. 
	 */
	private void makePaths(CodingNode root, String pathes) {
		if (root.right == null && root.left == null) {
			myTable.put(root.letter, pathes);
		} else {
			//searching for other codes
			makePaths(root.left, pathes + 0);
			makePaths(root.right, pathes + 1);
		}
	}
	
	/**
	 * Takes the file contents and changes it into a binary representation. 
	 *
	 * @param message the string representation of the original file.
	 * @return the binary representation of the encoded original file contents.
	 */
	private String codedMessage(String message) {
		StringBuilder builder = new StringBuilder();
		StringBuilder word = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			char temp = message.charAt(i);
			if(wordComponent(temp)) {
				word.append(temp);
			} else {
				if (word.length() != 0) {
					builder.append(myTable.get(word.toString()));
				}
				String notWord = "" + temp;
				builder.append(myTable.get(notWord));
				word = new StringBuilder();
			}
		}
		return builder.toString();
	}

	/** 
	 *	Inner Class for the Nodes for the Coding Tree. 
	 */
	public class CodingNode implements Comparable<CodingNode> {
		/** The word. */
		public String letter;
		/** Frequency of the word.*/
		public int weight;
		/** The left word. */
		public CodingNode left;
		/** The right word. */
		public CodingNode right;
		/**
		 * The constructor for the Coding Node Class.
		 * 
		 * @param letter the word.
		 * @param weight the frequency of the word.
		 */
		public CodingNode(String letter, int weight) {
			this(letter, weight, null, null);
		}
		/**
		 * The overloaded constructor for the Coding Node Class.
		 * 
		 * @param letter the word.
		 * @param weight the frequency of the word.
		 * @param the left word.
		 * @param the right word.
		 */
		public CodingNode(String letter, int weight, CodingNode left, CodingNode right) {
			this.letter = letter;
			this.weight = weight;
			this.left = left;
			this.right = right;
		}
		
		/**
		 * Compares two CodingNodes.
		 * 
		 * @param other the other Coding Node for comparison.
		 */
		public int compareTo(CodingNode other) {
			return weight - other.weight;
		}
	}
}

