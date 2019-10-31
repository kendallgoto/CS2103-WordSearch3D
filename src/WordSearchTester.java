import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

/**
 * Code to test <tt>WordSearch3D</tt>.
 */
public class WordSearchTester {
	private WordSearch3D _wordSearch;

	@Test
	/**
	 * Verifies that make can generate a very simple puzzle that is effectively 1d.
	 */
	public void testMake1D () {
		final String[] words = new String[] { "java" };
		// Solution is either java or avaj
		final char[][][] grid = _wordSearch.make(words, 1, 1, 4);
		final char[] row = grid[0][0];
		assertTrue((row[0] == 'j' && row[1] == 'a' && row[2] == 'v' && row[3] == 'a') ||
		           (row[3] == 'j' && row[2] == 'a' && row[1] == 'v' && row[0] == 'a'));
	}


	@Test
	/**
	 * Verifies that make returns null when it's impossible to construct a puzzle.
	 */
	public void testMakeImpossible () {
		//A couple of impossible cases discussed in class:
		/*
			 * (2 x 3 x 1)
			 * cac dad bab ded
			 * abc def cd ad
		 */
		//let's prove all of these cases as null
		final String[] words_4long = new String[] { "cac", "dad", "bab", "ded" };
		final String[] words_long2short = new String[] { "abc", "def", "cd", "ad" };
		final char[][][] grid_4long 	 = _wordSearch.make(words_4long, 2, 3, 1);
		final char[][][] grid_long2short = _wordSearch.make(words_long2short, 2, 3, 1);
		assertNull(grid_4long);
		assertNull(grid_long2short);
	}

	@Test
	/**
	 *  Verifies that search works correctly in a tiny grid that is effectively 2D.
	 */
	public void testSearchSimple () {
			// Note: this grid is 1x2x2 in size
			final char[][][] grid = new char[][][] { { { 'a', 'b', 'c' },
													   { 'd', 'f', 'e' } } };
			final int[][] location = _wordSearch.search(grid, "be");
			assertNotNull(location);
			assertEquals(location[0][0], 0);
			assertEquals(location[0][1], 0);
			assertEquals(location[0][2], 1);
			assertEquals(location[1][0], 0);
			assertEquals(location[1][1], 1);
			assertEquals(location[1][2], 2);
	}

	@Test
	/**
	 * Verifies that make can generate a grid when it's *necessary* for words to share
	 * some common letter locations.
	 */
	public void testMakeWithIntersection () {
		final String[] words = new String[] { "amc", "dmf", "gmi", "jml", "nmo", "pmr", "smu", "vmx", "yma", "zmq" };
		final char[][][] grid = _wordSearch.make(words, 3, 3, 3);
		assertNotNull(grid);
	}

	@Test
	/**
	 * Verifies that make returns a grid of the appropriate size.
	 */
	public void testMakeGridSize () {
		final String[] words = new String[] { "at", "it", "ix", "ax" };
		final char[][][] grid = _wordSearch.make(words, 17, 11, 13);
		assertEquals(grid.length, 17);
		for (int x = 0; x < 2; x++) {
			assertEquals(grid[x].length, 11);
			for (int y = 0; y < 2; y++) {
				assertEquals(grid[x][y].length, 13);
			}
		}
	}

	@Test
	/**
	 * Tests basic creation of a 2D grid
	 */
	public void testMake2D () {
		final String[] words = new String[] { "java", "lava" };
		final char[][][] grid = _wordSearch.make(words, 1, 2, 4);
		final char[] row1 = grid[0][0];
		final char[] row2 = grid[0][1];
		//java or avaj on first or second row
		assertTrue((row1[0] == 'j' && row1[1] == 'a' && row1[2] == 'v' && row1[3] == 'a') ||
				(row1[3] == 'j' && row1[2] == 'a' && row1[1] == 'v' && row1[0] == 'a') ||
				(row2[0] == 'j' && row2[1] == 'a' && row2[2] == 'v' && row2[3] == 'a') ||
				(row2[3] == 'j' && row2[2] == 'a' && row2[1] == 'v' && row2[0] == 'a'));
		//lava or aval on first or second row
		assertTrue((row1[0] == 'l' && row1[1] == 'a' && row1[2] == 'v' && row1[3] == 'a') ||
				(row1[3] == 'l' && row1[2] == 'a' && row1[1] == 'v' && row1[0] == 'a') ||
				(row2[0] == 'l' && row2[1] == 'a' && row2[2] == 'v' && row2[3] == 'a') ||
				(row2[3] == 'l' && row2[2] == 'a' && row2[1] == 'v' && row2[0] == 'a'));
	}
	@Test
	/**
	 * 	Tests creation of a 50x50x50 grid with 25 words, searching for all 25 words to ensure they can be found (tests make AND search)
	 */
	public void testMakeComplex_large () {
		final String[] words = new String[] { "123456", "password", "12345678", "qwerty", "123456789", "12345", "1234",
				"111111", "1234567", "dragon", "123123", "baseball", "abc123", "football", "monkey", "letmein", "696969",
				"shadow", "master", "666666", "qwertyuiop", "123321", "mustang", "1234567890", "michael"};
		final char[][][] grid = _wordSearch.make(words, 50, 50, 50);
		assertNotNull(grid);
		for(int i = 0; i < words.length; i++) {
			assertNotNull(_wordSearch.search(grid, words[i]));
		}
	}
	@Test
	/**
	 * 	Tests creation of a 25x25x25 grid with 25 words, searching for all 25 words to ensure they can be found
	 */
	public void testMakeComplex_medium () {
		final String[] words = new String[] { "123456", "password", "12345678", "qwerty", "123456789", "12345", "1234",
				"111111", "1234567", "dragon", "123123", "baseball", "abc123", "football", "monkey", "letmein", "696969",
				"shadow", "master", "666666", "qwertyuiop", "123321", "mustang", "1234567890", "michael"};
		final char[][][] grid = _wordSearch.make(words, 25, 25, 25);
		assertNotNull(grid);
		for(int i = 0; i < words.length; i++) {
			assertNotNull(_wordSearch.search(grid, words[i]));
		}
	}
	@Test
	/**
	 * 	Tests creation of a 10x10x2 grid with 25 words, searching for all 25 words to ensure they can be found
	 */
	public void testMakeComplex_small () {
		final String[] words = new String[] { "123456", "password", "12345678", "qwerty", "123456789", "12345", "1234",
				"111111", "1234567", "dragon", "123123", "baseball", "abc123", "football", "monkey", "letmein", "696969",
				"shadow", "master", "666666", "qwertyuiop", "123321", "mustang", "1234567890", "michael"};
		final char[][][] grid = _wordSearch.make(words, 10, 10, 2);
		assertNotNull(grid);
		for(int i = 0; i < words.length; i++) {
			assertNotNull(_wordSearch.search(grid, words[i]));
		}
	}
	@Test
	/**
	 * 	Test nonsensical makes to ensure failure (too small, zero grid size, negative grid size)
	 */
	public void testNonsenseMake() {
		final String[] words = new String[] { "hello" };
		final char[][][] grid_small = _wordSearch.make(words, 4, 4, 4);
		assertNull(grid_small);
		final char[][][] grid_empty = _wordSearch.make(words, 0, 0, 0);
		assertNull(grid_empty);
		final char[][][] grid_negative = _wordSearch.make(words, -1, -1, -1);
		assertNull(grid_negative);
	}
	@Test
	/**
	 * 	Tests to ensure that the created wordsearch is filling non-word space with random characters
	 */
	public void testMakeVoidFill() {
		final String[] words = new String[] { "hello" };
		final char[][][] grid_verylarge = _wordSearch.make(words, 100, 100, 100);
		assertNotEquals(grid_verylarge[0][0][0], 0); //could potentially false positive if we only check one square and "hello" is generated on that one square
		assertNotEquals(grid_verylarge[99][99][99], 0);
		assertNotNull(_wordSearch.search(grid_verylarge, "hello"));
	}

	@Test
	/**
	 * 	Test base cases for search according to specifications on assignment sheet
	 */
	public void testSearchSpecs() {
		final String[] words = new String[] { "hello" };
		final char[][][] grid = _wordSearch.make(words, 10, 10, 10);
		final int[][] grid_emptySearch = _wordSearch.search(grid, "");
		assertArrayEquals(grid_emptySearch, new int[0][3]);
		final int[][] grid_nullSearch = _wordSearch.search(grid, null);
		assertNull(grid_nullSearch);
	}
	@Test
	/**
	 *  Verifies that search works effectively in a simple 3D grid (2x2x2)
	 */
	public void testSearchSimple3D () {
		// Note: 2x2x2 grid - all letters touch and make a word w/ another letter
		final char[][][] grid = new char[][][] { { {'a', 'b'}, {'c', 'd'}}, {{'e', 'f'},{'g', 'h'}}};
		final char[] test = {'a', 'b', 'c', 'd', 'e', 'f', 'h'};
		for(int i = 0; i < test.length; i++) {
			for(int j = 0; j < test.length; j++) {
				if(i == j)
					continue;
				assertNotNull(_wordSearch.search(grid, ""+test[i]+test[j]));
			}
		}
		//no doubles possible, no 3 letter chains
		assertNull(_wordSearch.search(grid, "bb"));
		assertNull(_wordSearch.search(grid, "ee"));
		assertNull(_wordSearch.search(grid, "abc"));
	}
	@Test
	/**
	 * Test that searching for a non-inserted word fails w/ null
	 */
	public void testMissingWordSearch() {
		final String[] words = new String[] { "hello" };
		final char[][][] grid = _wordSearch.make(words, 10, 10, 10);
		final int[][] grid_nullSearch = _wordSearch.search(grid, "world");
		assertNotNull(grid);
		assertNull(grid_nullSearch);
	}
	@Test
	/**
	 * Test partial search for word succeeds
	 */
	public void testPartialWordSearch() {
		final String[] words = new String[] { "world" };
		final char[][][] grid = _wordSearch.make(words, 10, 10, 10);
		final int[][] grid_p1 = _wordSearch.search(grid, "orld");
		final int[][] grid_p2 = _wordSearch.search(grid, "rld");
		final int[][] grid_p3 = _wordSearch.search(grid, "ld");
		final int[][] grid_p4 = _wordSearch.search(grid, "d");
		assertNotNull(grid);
		assertNotNull(grid_p1);
		assertNotNull(grid_p2);
		assertNotNull(grid_p3);
		assertNotNull(grid_p4);
	}

	@Before
	public void setUp () {
		_wordSearch = new WordSearch3D();
	}
}
