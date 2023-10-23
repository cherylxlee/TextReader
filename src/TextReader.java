import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The class is an interactive program allowing users to find information from a given text file.
 * Capabilities include: finding the frequency of a given word, listing all words that have a 
 * frequency greater than an amount entered, finding the total number of words repeated more than once,
 * listing the most repeated word(s), and returning the lines numbers a given word appears in.
 * 
 * @author Cheryl Lee <cher.lee@bellevuecollege.edu>
 * @version 1
 */
public class TextReader {
	// Initialize class constants
	public static final int MAX_LENGTH = 1000000;
	public static final int NOT_FOUND = -1;
	
	/**
	 * The main driver method which leads the user through the program. Uses while loops to allow
	 * the user to repeatedly user the program until they enter 0.
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// Initialize the arrays, scanner, and filename (to reduce redundancy)
		String[] words = new String[MAX_LENGTH];
		int[] frequencies = new int[MAX_LENGTH];
		
		Scanner scanner = new Scanner(System.in);
		String filename = "WarAndPeace.txt";
		
		// Initialize the count variable, which calls the readWordFromFile method
		int count = readWordFromFile(filename,words,frequencies);
		
		displayMenu();
		
		System.out.print("Please enter option [0-3]: ");
		
		// Get option (value) from the scanner
		int option = scanner.nextInt();
		
		// Set up while loop to repeatedly loop through the program until 0 is entered
		while (option > 0) {
			if (option == 1) {
				System.out.print("Enter the word to find its frequency: ");
				String word = scanner.next();
				int index = getWordIndexFromArray(word, words, count);
				
				// Use if statement to match word's index to its corresponding spot in the frequencies array
				if (index == NOT_FOUND)
					System.out.println("\"" + word + "\" not found in text file");
				else
					System.out.println("The frequency of the word \"" + word + "\" is: " + frequencies[index]);	
			}

			else if (option == 2) {
				System.out.print("Enter the least frequency to list all words: ");
				int least = scanner.nextInt();
				
				// Initialize string variable to store the results of the for loop
				String results = "";
				
				// Use for loop to traverse the frequencies array to finds words less than the given frequency
				for (int i = 0; i < count; i++) {
					if (frequencies[i] > least)
						results += words[i] + " ";
				}

				System.out.println("The list of words that have frequency greater than " + least + ": " + results);
			}
			
			// Extra credit option calls from 3 methods to allow for the extra credit capabilities
			else if (option == 3) {
				System.out.println("a) Total  number of words repeated more than once: " + calculateTotals(frequencies, count)); 
				findMostRepeated(frequencies, words, count);
				
				System.out.print("c) Enter a word you would like to search for: " );
				String word = scanner.next();
				locateLinesInTextFileWithWord(filename,word,count); 
			 }
			
			// Else statement if user enters number greater than 3
			else {
				System.out.println("Sorry, I could not find the function for the option " + option);	
			}
			
			// re-select option: create loop
			displayMenu();

			System.out.print("Please enter option [0-3]: ");
			option = scanner.nextInt();	
		}

		System.out.println("Thank you for using the program");
		
		// Close out the scanner and empty the arrays
		scanner.close();
		Arrays.fill(words, null);
		Arrays.fill(frequencies, 0);
		
	}

	/**
	 * The method displays the menu to the console
	 * Includes Extra Credit capabilities
	 */
	public static void displayMenu() {
		System.out.println("======================================");
		System.out.println("Please select option below to operate:");
		System.out.println("  0. Quit the program");
		System.out.println("  1. Print out the frequency of an entered word");
		System.out.println("  2. Print out all words that have a frequency that is greater than an entered value");
		System.out.println("  3. Reserved for extra credit:\n    a) Print out total number of words repeated more than once"
				+ "\n    b) Print out the most repeated word(s)\n    c) Print out the lines an entered word appears in");
	
	}
	
	/**
	 * The method read the words from the input file into array of strings
	 * @param filename the file name
	 * @param words the array of words (strings)
	 * @param frequencies the array of frequencies (integers)
	 * @return the number of unique words in the text file
	 */
	public static int readWordFromFile(String filename, String[] words, int[] frequencies) throws FileNotFoundException {
		Scanner input = new Scanner(new File(filename));
		// Initialize temporary arrays, these will just be used to store text information temporarily
		String[] temp = new String[MAX_LENGTH];
		int[] temp2 = new int[MAX_LENGTH];

		int count = 0;
		
		// Use while loop to read all the tokens in the text file, put into the array without punctuation and lower-case
		int i = 0;		
		while (input.hasNext()) {
			String next = removePunctuations(input.next().toLowerCase());
			temp[i] = next;
			i++;
		}
		
		// Use for loop to count the frequency of each word in the "temp" words array, store into the "temp2" frequencies array
		for (int j = 0; j < temp.length; j++) {
			int index = getWordIndexFromArray(temp[j], temp, i);
			
			if (index >= 0) 
				temp2[index]++;
		}
		
		// Use for loop to put ONLY unique words into the words array, and have the frequencies array match
		int index = 0;
		for (int k = 0; k < temp.length; k++) {
			if (temp2[k] > 0) {
				words[index] = temp[k];
				frequencies[index] = temp2[k];
				index++;
				count++;				
			}
		}
		
		return count;

	}

	/**
	 * The method remove all punctuation from a word
	 * @param word the given word
	 * @return the word that is removed all punctuations
	 */
	public static String removePunctuations(String word) {
		word = word.replaceAll("\\p{Punct}","");
		return word;
		
	}
		
	/**
	 * The method return the index of the given word in the array of words
	 * @param word the given word that need to check its index in the array
	 * @param words the array of words
	 * @param size the size of unique words
	 * @return the index of the word (-1 if the word is not in the array)
	 */
	public static int getWordIndexFromArray(String word, String[] words, int size) {
		for (int i = 0; i < size; i++) {
			// Uses equalsIgnoreCase function to make the method more versatile
			if (words[i].equalsIgnoreCase(word))
				return i;
		}
		
		return NOT_FOUND;
		
	}
	
	/**
	 * The method calculates the total number of words repeated more than once
	 * @param frequencies the array of frequencies (integers)
	 * @param count the number of unique words
	 * @return the total number of repeated words
	 */
	public static int calculateTotals(int[] frequencies, int count) {
		int total = 0;
		for (int i = 0; i < count; i++)
			if (frequencies[i] > 1)
				total++;
		
		return total;
		
	}
	
	/**
	 * The method prints out the most repeated word(s) in the text file
	 * @param frequencies the array of frequencies (integers)
	 * @param words the array of words
	 * @param count the number of unique words
	 */
	public static void findMostRepeated(int[] frequencies, String[] words, int count) {
		// Initialize temporary array, max variable, and String to store results
		int[] temp = new int[MAX_LENGTH];
		int max = 0;
		String result = "";

		// Use for loop to store maximum values into the "temp" array
		for (int i = 0; i < count-1; i++) {
			if (frequencies[i] > max) {
				max = frequencies[i];
				temp[i] = max;
			}
			else if (frequencies[i] == max)
				temp[i] = frequencies[i];
		}
		
		// Use for loop to append all the values in the array to the results String
		for (int j = 0; j < temp.length; j++) {
			if (temp[j] > 0)
				result += words[j] + " ";
		}
		
		// Print out the results
		System.out.println("b) The most repeated word(s) in the file are: " + result);
	
	}
	
	/**
	 * The method prints out the line numbers where a given word appears in
	 * @param filename the file name
	 * @param word the given word that need to check its index in the array
	 */
	public static void locateLinesInTextFileWithWord(String filename, String word, int count) throws FileNotFoundException {		
		Scanner input = new Scanner(new File(filename));
		
		// Initialize temporary array to store all the line tokens
		String[] temp = new String[MAX_LENGTH];
	
		// Initialize String variable to store all the final results and counter to count the number of lines appeared in
		String result = "";
		int counter = 0;
		
		// Use while loop to read all the tokens in the text file, put into the array without punctuation and lower-case
		int i = 0;		
		while (input.hasNextLine()) {
			String next = removePunctuations(input.nextLine().toLowerCase());
			temp[i] = next;
			i++;
		}
			
		// Initialize new array which will count up each time a given word appears in one of the lines
		int[] tally = new int[MAX_LENGTH];
		// Inner for loops to break each line into individual "tempi" arrays, then traverse those arrays to find given word
		for (int k = 0; k < i; k++) {
			for (int j = 0; j < temp[k].length(); j++) {
				String tempi[] = temp[k].split(" ");
				for (int c = 0; c < tempi.length; c++)
					if (tempi[c].equalsIgnoreCase(word))
						tally[k] = 1;	
			}
		}
				
		// Store the results of the array into results variable (+1 due to zero-based indexing)
		for (int j = 0; j < tally.length; j++) {
			if (tally[j] == 1) {
				counter++;
				result += (1+j) + " ";
			}
		}
		
		// Empty out the array
		Arrays.fill(tally, 0);
		
		// Print the results
		System.out.println("The word \""+ word + "\" appears on " + counter + " line(s) total. The line number(s): " + result);
		
	}
	
}
