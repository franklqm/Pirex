package testing;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import search.PirexData;

/**
 * Tests PirexData.
 * 
 * @author Anna Romness and Cassie Fox
 *
 */
public class PirexDataTest extends TestCase
{
  /**
   * Tests PirexFolder.
   * 
   * @throws IOException
   *           if the file is not found
   */

  public void test1() throws IOException
  {
    PirexData.pirexFolder("src/pirexData/The Creature Inside.txt", ".");
    File f = new File("The Creature Inside.txt");
    f.delete();
    assertFalse(f.exists());

  }

  /**
   * Tests getFilesList.
   */
  public void test2()
  {
    PirexData.getFilesList("src/pirexData");
    File f = new File("Dorpsgenooten.txt");
    f.delete();
    assertFalse(f.exists());

  }

}
