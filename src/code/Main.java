/*
 * Assignment 4 Compressed Literature 2
 * 
 * Louis Yang, Michael Wilson
 * Extra Credits Finished please check!
 *
 */
package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The main class for the Compressed Literature Program.
 * 
 * @author Louis Yang (jeho1994)
 * @version 1.0
 */
public class Main {
	/** The book file to be read. */
	private static final String BOOK_FILE = "./txt/WarAndPeace.txt";
	/** The encoded file destination. */
	private static final String ENCODED_FILE = "./txt/compressed.txt";
	/** The encoded key destination. */
	private static final String ENCODED_KEY = "./txt/codes.txt";
	/** The decoded file destination. */
	private static final String DECODED_FILE = "./txt/decoded.txt";
	/** The coding used for file encoding end decoding. */
	private static CodingTree myCodingTree;
	/** Main program executor. */
	public static void main(String[] args) {
		//**** Uncomment to view testing***** 
		//testCodingTree();
		//testMyHashTable();
		
		long startTime = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		//reads in the book or text file.
		File file = new File(BOOK_FILE);
		try {
			BufferedReader bf = new BufferedReader(new FileReader(file));
			String line;
			try {
				while ((line = bf.readLine()) != null) {
					sb.append(line);
					sb.append("\r\n");
				}
				bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//encodes the book or text file.
		myCodingTree = new CodingTree(sb.toString());
		String encodedMessage = myCodingTree.encodedFile();
		byte[] encodeBytes = messageToBytes(encodedMessage);
		File outputFile = new File(ENCODED_FILE);
		//prints the encoded file and its key mapping.
		printEncodedFile(encodeBytes, outputFile);
		printCodePath();
		
		//prints the encoding statistics.
		System.out.println("Orginal File Size: " 
				+ file.length() / 1024 + " kilobytes");
		System.out.println("Encoded File Size: " 
				+ outputFile.length() / 1024 + " kilobytes (Expected: 1001)");
		System.out.println("Compressed Ratio: " + (1 - ((double)outputFile.length()) 
				/ ((double)file.length())) + "%");
		long time = System.currentTimeMillis() - startTime;
		System.out.println("Encoding Process took : " + time + " ms");
		
		//used solely for decoding purposes.
		String codedMessage = decodeFile();
		String decodedMessage = myCodingTree.decode(codedMessage, myCodingTree.mapping());
		printDecoded(decodedMessage);
	
	}
	
	/**
	 * Prints the encoded file into an output file.
	 * 
	 * @param byte the bytes of the encoded file.
	 * @param encoded file destination.
	 */
	private static void printEncodedFile(byte[] bytes, File encoded) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(encoded);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			fos.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fos.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Changes the message contents of the file into byte form.
	 * 
	 * @param the message the contents of the file.
	 * @return the bytes format of the contents of the file.
	 */
	private static byte[] messageToBytes(String message) {
		int leftover = message.length() % 8;
		if (leftover != 0) {
			for (int i = 0; i < leftover; i++) {
				message = message + 0;
			}
		}
		int iterations = message.length() / 8;	
		byte bytes[] = new byte[iterations];
		for (int i = 0; i < iterations; i++) {
			int temp = 	Integer.parseInt(message.substring(i * 8, i * 8 + 8), 2);
			bytes[i] = (byte) temp;
		}
		return bytes;
	}
	
	/** 
	 * Prints the Encoded Coding Key and Mapping.
	 */
	private static void printCodePath() {
		try(PrintWriter out = new PrintWriter(ENCODED_KEY)){
			out.write(myCodingTree.mapping().toString());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Decodes the encoded file into binary form.
	 * 
	 * @return the binary representation of the decoded encoded file.
	 */
	private static String decodeFile() {
		File file = new File(ENCODED_FILE);
		FileInputStream in = null;
		StringBuilder reader = new StringBuilder();
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int fileLength = (int)file.length();
		byte fileContent[] = new byte[fileLength];
		try {
			in.read(fileContent);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < fileLength; i++) {
			String temp = Integer.toBinaryString((int)fileContent[i] & 0xff);
			while (temp.length() < 8) {
				temp = "0" + temp;
			}
			reader.append(temp);
		}
		return reader.toString();
	}
	
	/**
	 * Prints out the decoded file into an ouput file. 
	 */
	private static void printDecoded (String message) {
		try(PrintWriter out = new PrintWriter(DECODED_FILE)){
		    out.write(message);
		    out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/** Tests the CodingTree Class for validity and errors. */
	public static void testCodingTree() {
		StringBuilder sb = new StringBuilder();
		sb.append("LET us Test This Coding Tree.Test Some Characterstoo& * (*&^%0187/");
		sb.append(" * * ^ &  & & #  # 3 %% && afwegaw#  & 8  *88");
		CodingTree testing = new CodingTree(sb.toString());
		System.out.println(testing.mapping().toString());
	}
	
	/** Tests the MyHashTable Class for validity and errors. */
	public static void testMyHashTable() {
		MyHashTable<String, String> test = new MyHashTable<String, String>(10);
		test.put("a", "1");
		test.put("b10", "2");
		test.put("c9", "3");
		test.put("d8", "4");
		test.put("e", "5");
		test.put("f", "6");
		test.put("g", "7");
		test.put("h", "8");
		test.put("&", "8");
		System.out.println(test.toString());
		String b = test.get("b");
		System.out.println(b);
		System.out.println(test.containsKey("a"));
		System.out.println(test.containsKey("*"));
		test.stats();
	}
}
