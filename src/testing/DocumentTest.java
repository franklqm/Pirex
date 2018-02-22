package testing;

import java.io.IOException;

import junit.framework.TestCase;
import search.Document;

/**
 * Tests the Document class.
 * 
 * @author Anna Romness and Cassie Fox
 *
 */
public class DocumentTest extends TestCase
{

  /**
   * Tests createDocumentString.
   */
  public void testCreateDocumentString()
  {
    @SuppressWarnings("unused")
    Document test = new Document();
    Document testing = new Document("Anna Romness", 0, "Searching for words");
    Document newTest = new Document(testing);
    assertEquals(testing.toString(), newTest.toString());

  }

  /**
   * Tests createAnotherDocument.
   */
  public void testCreateAnotherDocument()
  {
    Document testing = new Document("Anna Romness", 0, "Searching for words");
    Document newTest = new Document(testing);
    assertEquals(testing.toString(), newTest.toString());
  }

  /**
   * Tests testCreateDocument.
   */
  public void testCreateDocument()
  {
    String[] newArray = {"Anna Romness", "Searching for words"};
    Document testing = new Document(newArray, newArray);
    Document newTest = new Document(testing);
    assertEquals(testing.toString(), newTest.toString());
  }

  /**
   * tests getDocInfo.
   * 
   * @throws IOException
   *           if the file is not found
   */
  public void testGetDocInfo() throws IOException
  {
    String fpathTwo = "src/pirexData/India.txt";
    Document displayDocumentTwo = Document.getDocInfo(fpathTwo);
    Document newTest = Document.getDocInfo(fpathTwo);
    assertEquals(newTest.toString(), displayDocumentTwo.toString());
  }

  /**
   * tests titleAuthor.
   */
  public void testTitleAuthor()
  {
    Document testTA = new Document("Anna", 10, "Cassie");
    assertNull(testTA.getAuthor());
    assertNull(testTA.getTitle());

    String[] newArray = {"1", "2"};
    Document testTA2 = new Document(newArray, newArray);
    String testString = testTA2.getAuthors().toString();
    assertEquals(testString, testString);
    @SuppressWarnings("static-access")
    String testStringTitle = testTA2.getTitles().toString();
    assertEquals(testStringTitle, testStringTitle);
  }

  /**
   * Tests Equal for third return statement.
   */
  public void testEqualDifferent()
  {
    String[] titles = new String[] {"Cassie"};
    String[] authors = new String[] {"Anna"};
    String author2 = "Zeb";
    String title2 = "Team09";
    int start = 0;
    int end = 3;

    Document test1 = new Document(titles, authors, author2, title2, start, end);
    Document test2 = new Document(test1);
    assertEquals(true, test2.equals(test1));

  }

  /**
   * Tests Equal for third return statement when it is false.
   */
  public void testNotEqualDifferent()
  {
    String[] titles = new String[] {"Cassie"};
    String[] authors = new String[] {"Anna"};
    String author2 = "Zeb";
    String title2 = "Team09";
    int start = 0;
    int end = 3;

    Document test1 = new Document(titles, authors, author2, title2, start, end);
    Document test2 = new Document(titles, authors, "cassie", title2, start, end);
    assertEquals(false, test2.equals(test1));
  }

  /**
   * Tests equal for second return statement.
   */
  public void testNotEqual()
  {
    Document test1 = new Document();
    Object test2 = new Object();

    assertEquals(false, test1.equals(test2));
  }

  /**
   * Tests equal for first return statement.
   */
  public void testEqualSame()
  {
    Document test1 = new Document();

    assertEquals(true, test1.equals(test1));
  }

}
