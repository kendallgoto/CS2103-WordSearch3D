import java.util.Random;
import java.util.*;
import java.io.*;

/**
 * Implements a 3-d word search puzzle program.
 */
public class WordSearch3D {
	final private Random _rng = new Random();

	final private Coordinate[] _allDirections = new Coordinate[26];
	public WordSearch3D () {
		//generate + populate allDirections array
		int counter = 0;
		for(int dirX = -1; dirX < 2; dirX++) {
			for (int dirY = -1; dirY < 2; dirY++) {
				for (int dirZ = -1; dirZ < 2; dirZ++) {
					if (dirX == 0 && dirY == 0 && dirZ == 0)
						continue; //ignore self (0 0 0 offset)
					_allDirections[counter++] = new Coordinate(dirX, dirY, dirZ);
				}
			}
		}

	}

	/**
	 * Searches for all the words in the specified list in the specified grid.
	 * You should not need to modify this method.
	 * @param grid the grid of characters comprising the word search puzzle
	 * @param words the words to search for
	 * @return a list of lists of locations of the letters in the words
	 */
	public int[][][] searchForAll (char[][][] grid, String[] words) {
		final int[][][] locations = new int[words.length][][];
		for (int i = 0; i < words.length; i++) {
			locations[i] = search(grid, words[i]);
		}
		return locations;
	}

	/**
	 * Searches for the specified word in the specified grid.
	 * @param grid the grid of characters comprising the word search puzzle
	 * @param word the word to search for
	 * @return If the grid contains the
	 * word, then the method returns a list of the (3-d) locations of its letters; if not, 
	 */
	public int[][] search (char[][][] grid, String word) {
		//base case:
		if(word == null)
			return null;
		if(word.length() == 0)
			return new int[0][3];
		//start with finding the first letter of "word"
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				for(int k = 0; k < grid[i][j].length; k++) {
					if(grid[i][j][k] == word.charAt(0)) {
						//Our first letter is right, so let's start searching the nearby area.
						//Process recursively
						final Coordinate initialCoordinate = new Coordinate(i, j, k);
						for(int dir = 0; dir < _allDirections.length; dir++) {
							//in each direction, recursively move and test
							final ArrayList<Coordinate> resulting = new ArrayList<Coordinate>(1);
							resulting.add(new Coordinate(i, j, k));
							final Coordinate direction = _allDirections[dir];
							if(adjacentRecursion(resulting, initialCoordinate, direction, word.substring(1), grid, false)) {
								//we got a true result, that means we found the target word, with the coordinates in "resulting"
								return Coordinate.expandCoordinateArray(resulting);
							}
						}
					}
				}
			}
		}
		return null; //if we reached here and never hit our prior return, we've failed
	}

	//Given an initial coordinate, direction coordinate, grid and word, move across that grid by summing the initial + direction coordinates (= target),
	//and verify that each recursive new target is equal to the first letter of word (or blank if plant=true), keeping track of successful coordinates
	//via an arrayList 'successArr'.
	private boolean adjacentRecursion(ArrayList<Coordinate> successArr, Coordinate initial, Coordinate direction, String word, char[][][] grid, boolean plant) {
		//base:
		if(word.length() == 0)
			return true;

		final Coordinate target = Coordinate.addCoordinates(initial, direction);
		final char currentChar = Coordinate.charAtComplexIndex(grid, target);
		boolean requiredCondition = currentChar == word.charAt(0);
		if(plant) {
			requiredCondition = (requiredCondition || currentChar == 0);
		}
		if (requiredCondition) {
			//successful! let's continue in this direction unless the word is complete
			successArr.add(target);
			return adjacentRecursion(successArr, target, direction, word.substring(1), grid, plant);
		}
		return false;
	}

	/**
	 * Tries to create a word search puzzle of the specified size with the specified
	 * list of words.
	 * @param words the list of words to embed in the grid
	 * @param sizeX size of the grid along first dimension
	 * @param sizeY size of the grid along second dimension
	 * @param sizeZ size of the grid along third dimension
	 * @return a 3-d char array if successful that contains all the words, or <tt>null</tt> if
	 * no satisfying grid could be found.
	 */
	public char[][][] make (String[] words, int sizeX, int sizeY, int sizeZ) {
		if(sizeX <= 0 || sizeY <= 0 || sizeZ <= 0)
			return null;
		eachBoardTry:
		for(int totalTries = 0; totalTries < 1000; totalTries++) {
			final char[][][] grid = new char[sizeX][sizeY][sizeZ];
			for(int i = 0; i < words.length; i++) { //perhaps ideally: place largest words first
				String wordToPlace = words[i];
				if(_rng.nextInt(2) == 1) //reverse word randomly
					wordToPlace = new StringBuilder().append(wordToPlace).reverse().toString();
				boolean successfulPlant = false;
				wordAttempts:
				for(int perWordAttempts = 0; perWordAttempts < 1000; perWordAttempts++) {
					final Coordinate randomStart = new Coordinate(_rng.nextInt(sizeX), _rng.nextInt(sizeY), _rng.nextInt(sizeZ));
					if(Coordinate.charAtComplexIndex(grid, randomStart) != 0)
						continue; //our starting point was already filled
					final ArrayList<Coordinate> ourDirections = new ArrayList<Coordinate>(Arrays.asList(_allDirections));
					Collections.shuffle(ourDirections); //resort randomly
					for(int eachDirection = 0; eachDirection < ourDirections.size(); eachDirection++) {
						final Coordinate thisDirection = ourDirections.get(eachDirection);
						final ArrayList<Coordinate> resulting = new ArrayList<Coordinate>(1);
						resulting.add(randomStart);
						if(adjacentRecursion(resulting, randomStart, thisDirection, wordToPlace.substring(1), grid, true)) {
							for(int insertLetters = 0; insertLetters < resulting.size(); insertLetters++) {
								final Coordinate successfulCharacter = resulting.get(insertLetters);
								final char insertationCharacter = wordToPlace.charAt(insertLetters);
								grid[successfulCharacter.getI()][successfulCharacter.getJ()][successfulCharacter.getK()] = insertationCharacter;
							}
							successfulPlant = true;
							break wordAttempts;
						}
					}
				}
				if(!successfulPlant)
					continue eachBoardTry;
			}
			fillEmptySpaces(grid);
			return grid;
		}
		return null;
	}
	private void fillEmptySpaces(char[][][] grid) {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				for(int k = 0; k < grid[i][j].length; k++) {
					if(grid[i][j][k] == 0)
						grid[i][j][k] = (char) (_rng.nextInt(26) + 'a'); // provided
				}
			}
		}
	}
	/**
	 * Exports to a file the list of lists of 3-d coordinates.
	 * You should not need to modify this method.
	 * @param locations a list (for all the words) of lists (for the letters of each word) of 3-d coordinates.
	 * @param filename what to name the exported file.
	 */
	public static void exportLocations (int[][][] locations, String filename) {
		// First determine how many non-null locations we have
		int numLocations = 0;
		for (int i = 0; i < locations.length; i++) {
			if (locations[i] != null) {
				numLocations++;
			}
		}

		try (final PrintWriter pw = new PrintWriter(filename)) {
			pw.print(numLocations);  // number of words
			pw.print('\n');
			for (int i = 0; i < locations.length; i++) {
				if (locations[i] != null) {
					pw.print(locations[i].length);  // number of characters in the word
					pw.print('\n');
					for (int j = 0; j < locations[i].length; j++) {
						for (int k = 0; k < 3; k++) {  // 3-d coordinates
							pw.print(locations[i][j][k]);
							pw.print(' ');
						}
					}
					pw.print('\n');
				}
			}
			pw.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
	}

	/**
	 * Exports to a file the contents of a 3-d grid.
	 * You should not need to modify this method.
	 * @param grid a 3-d grid of characters
	 * @param filename what to name the exported file.
	 */
	public static void exportGrid (char[][][] grid, String filename) {
		try (final PrintWriter pw = new PrintWriter(filename)) {
			pw.print(grid.length);  // height
			pw.print(' ');
			pw.print(grid[0].length);  // width
			pw.print(' ');
			pw.print(grid[0][0].length);  // depth
			pw.print('\n');
			for (int x = 0; x < grid.length; x++) {
				for (int y = 0; y < grid[0].length; y++) {
					for (int z = 0; z < grid[0][0].length; z++) {
						pw.print(grid[x][y][z]);
						pw.print(' ');
					}
				}
				pw.print('\n');
			}
			pw.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
	}

	/**
	 * Creates a 3-d word search puzzle with some nicely chosen fruits and vegetables,
	 * and then exports the resulting puzzle and its solution to grid.txt and locations.txt
	 * files.
	 */
	public static void main (String[] args) {
		final WordSearch3D wordSearch = new WordSearch3D();
		final String[] words = new String[] { "apple", "orange", "pear", "peach", "durian", "lemon", "lime", "jackfruit", "plum", "grape", "apricot", "blueberry", "tangerine", "coconut", "mango", "lychee", "guava", "strawberry", "kiwi", "kumquat", "persimmon", "papaya", "longan", "eggplant", "cucumber", "tomato", "zucchini", "olive", "pea", "pumpkin", "cherry", "date", "nectarine", "breadfruit", "sapodilla", "rowan", "quince", "toyon", "sorb", "medlar" };
		final int xSize = 10, ySize = 10, zSize = 10;
		final char[][][] grid = wordSearch.make(words, xSize, ySize, zSize);
		exportGrid(grid, "grid.txt");

		final int[][][] locations = wordSearch.searchForAll(grid, words);
		exportLocations(locations, "locations.txt");
	}
}
