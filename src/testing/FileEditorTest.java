package testing;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import search.FileEditor;

public class FileEditorTest extends TestCase {

	public void testAuthors() {
		FileEditor test = new FileEditor();

		String[] authors = new String[6];
		authors[0] = new String("Anna");
		authors[1] = new String("Kenny");
		authors[2] = new String("Zeb");
		authors[3] = new String("Que");
		authors[4] = new String("Cassie");
		authors[5] = new String("Jasmyn");

		String[] titles = new String[6];
		titles[0] = new String("Anna");
		titles[1] = new String("Kenny");
		titles[2] = new String("Zeb");
		titles[3] = new String("Que");
		titles[4] = new String("Cassie");
		titles[5] = new String("Jasmyn");

		test.setEditor(authors, titles);
		assertEquals(authors.toString(), test.getAuthors().toString());
	}

	public void testTitles() {
		FileEditor test = new FileEditor();

		String[] authors = new String[6];
		authors[0] = new String("Anna");
		authors[1] = new String("Kenny");
		authors[2] = new String("Zeb");
		authors[3] = new String("Que");
		authors[4] = new String("Cassie");
		authors[5] = new String("Jasmyn");

		String[] titles = new String[6];
		titles[0] = new String("Anna");
		titles[1] = new String("Kenny");
		titles[2] = new String("Zeb");
		titles[3] = new String("Que");
		titles[4] = new String("Cassie");
		titles[5] = new String("Jasmyn");

		test.setEditor(authors, titles);
		assertEquals(titles.toString(), test.getTitles().toString());
	}

	public void testEditAll() throws IOException {
		FileEditor test = new FileEditor();

		String[] authors = new String[6];
		authors[0] = new String("Anna");
		authors[1] = new String("Kenny");
		authors[2] = new String("Zeb");
		authors[3] = new String("Que");
		authors[4] = new String("Cassie");
		authors[5] = new String("Jasmyn");

		String[] titles = new String[6];
		titles[0] = new String("Anna");
		titles[1] = new String("Kenny");
		titles[2] = new String("Zeb");
		titles[3] = new String("Que");
		titles[4] = new String("Cassie");
		titles[5] = new String("Jasmyn");

		test.editAll();
		String expected = test.getTitles().toString();
		String actual = test.getTitles().toString();
		assertEquals(expected,actual);
	
		String expectedAuthor = test.getAuthors().toString();
		String actualAuthor = test.getAuthors().toString();
		assertEquals(expectedAuthor,actualAuthor);
	
	}
	
	public void testFindItem(){
		FileEditor test = new FileEditor();
		
		File newFile = new File("../pirex09/src/pirexData/Art.txt");
		String expected = "for freedom's sake without a purpose or without an";
		assertEquals(expected, test.findItem("freedom", newFile));
		assertEquals("Anthony M. Ludovici", test.findItem("Author:", newFile));
		
		File newFileTwo = new File("/pirexData/Art.txt");
		String expectedTwo = "File Not Found";
		assertEquals(expectedTwo, test.findItem("freedom", newFileTwo));
		
	}
	
	public void testEditAuthor() throws IOException{
		FileEditor test = new FileEditor();
		test.editAuthor("James W. Donovan", "David Bernstein");
		String expected = test.getAuthors().toString();
		String actual = test.getAuthors().toString();
		assertEquals(expected,actual);
		
		test.editAuthor("David Bernstein", "James W. Donovan");
		String expectedBack = test.getAuthors().toString();
		String actualBack = test.getAuthors().toString();
		assertEquals(expectedBack,actualBack);
	}
	
	public void testEditTitle() throws IOException{
		FileEditor test = new FileEditor();
		test.editTitle("Don't Marry or, Advice on How, When and Who to Marry", "David Bernstein marries Sharon");
		String expected = test.getTitles().toString();
		String actual = test.getTitles().toString();
		assertEquals(expected,actual);
		
		test.editTitle("David Bernstein marries Sharon", "Don't Marry or, Advice on How, When and Who to Marry");
		String expectedBack = test.getTitles().toString();
		String actualBack = test.getTitles().toString();
		assertEquals(expected,actual);
	}
	
	

}
