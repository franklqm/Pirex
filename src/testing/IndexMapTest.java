/**
 * 
 */
package testing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.TreeMap;

import junit.framework.TestCase;
import search.Document;
import search.IndexMap;

/**
 * @author kelly2jk
 *
 */
public class IndexMapTest extends TestCase
{
  File[] files = new File[1];
  File notFile = new File("src/pirexData/Art.txt");
  String file = "src/pirexData/Art.txt";
  IndexMap indexMap = new IndexMap();
  
  /**
   * Test method for {@link search.IndexMap#getParagraph(java.lang.String, int)}.
   * @throws IOException 
   */
  public void testGetParagraph() throws IOException
  {
    ArrayList<String> expected = new ArrayList<String>();
    String actualStr;
    String expectedStr;
    expected.add(0, " The Project Gutenberg EBook of Nietzsche and Art, by Anthony M. Ludovici");//;
    ArrayList<String> actual   = indexMap.getParagraph(file, 0);
    actualStr = " " + actual.get(0);
    expectedStr = expected.get(0);
//    for(int ii = 0;ii <  actual.get(0).length();ii++){
//      if(expected.get(0).charAt(ii) == actual.get(0).charAt(ii) ){
//        System.out.println("Das True!" + " " + expected.get(0).indexOf(expected.get(0).charAt(ii)) + " " + actual.get(0).indexOf(actual.get(0).charAt(ii)));
//      }
//    }
    //sort(actual);
    assertSame(true, expectedStr.contains(actualStr));

    //assertEquals("Error: ", expected.toString(), actual.toString());
  }

  /**
   * Test method for {@link search.IndexMap#makeString(java.util.ArrayList)}.
   * @throws IOException 
   */
  public void testMakeString() throws IOException
  {
    String expected = "The Project Gutenberg EBook of Nietzsche and Art, by Anthony M. Ludovici";
    ArrayList<String> actually   = indexMap.getParagraph(file, 0);
    String actual = IndexMap.makeString(actually);
//    System.out.println(actual);
//    System.out.print(expected);
    //sort(actual);
    //assertSame(true, expectedStr.contains(actualStr));
   // assertFalse(expected == actual);  
    
    assertEquals("Error: ", expected.trim(), actual.trim());
  }

  /**
   * Test method for {@link search.IndexMap#indexFiles(java.io.File[])}.
   * @throws IOException 
   */
  public void testIndexFiles() throws IOException
  {
    files[0] = notFile;
    indexMap.indexFiles(files); 
  }

  /**
   * Test method for {@link search.IndexMap#search(java.lang.String, java.util.NavigableMap)}.
   */
  public void testSearch()
  {
    
    NavigableMap<String, ArrayList<Document>> index = new TreeMap<String, ArrayList<Document>>();
    ArrayList<Document> docs = IndexMap.search("hello", index);
    //System.out.println(docs);
    ArrayList<Document> doc2 = IndexMap.search("", index);
  }

}
