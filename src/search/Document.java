package search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

/**
 * Document gets the title and author.
 * 
 * @author Kenny Boadu and Shazeb Khan
 *
 */
public class Document implements Serializable
{
  
  private static final int TITLE_MAX = 100;
  private static final int AUTHOR_MAX = 100;
  private static final String AUTHOR_HEADER = "Author";
  private static final String TITLE_HEADER = "Title";
  private static final String STARS_HEADER = "***";
  private static final String START_OF = "START OF";
  private static final String END_OF = "END OF";
  private static String[] authors;
  private static String[] titles;
  private String author;
  private String title;
  private String fileName;
  private int startMark;
  private int endMark;
  private int opusNum;
  private int paraNum;
  private int firstLine;

  /**
   * Document constructor.
   */
  public Document()
  {

  }

  /**
   * Constructs a Document object.
   * 
   * @param titles
   *          the title
   * @param authors
   *          the author
   * @param author2
   *          the second author
   * @param title2
   *          the second title
   * @param startMark
   *          the start
   * @param endMark
   *          the end
   */
  public Document(String[] titles, String[] authors, String author2, String title2, int startMark,
      int endMark)
  {
    this.titles = titles;
    this.authors = authors;
    this.author = author2;
    this.title = title2;
    this.setFileName("");
    this.setStartMark(startMark);
    this.setEndMark(endMark);
    this.setOpusNum(0);
    this.setParaNum(0);
  }

  /**
   * Constructs a document object.
   * 
   * @param line
   *          the line
   * @param lineNum
   *          the line number
   * @param fileName
   *          the file name
   */
  public Document(String line, int lineNum, String fileName)
  {
    this.setFileName(fileName);
    this.setStartMark(0);
    this.setEndMark(0);
    this.setOpusNum(0);
    this.setParaNum(0);
  }

  /**
   * Constructs a document object with another document object.
   * 
   * @param other
   *          the other object.
   */
  public Document(Document other)
  {
    this.author = other.author;
    this.title = other.title;
    this.setFileName(other.getFileName());
    this.setStartMark(other.getStartMark());
    this.setEndMark(other.getEndMark());
    this.setOpusNum(other.getOpusNum());
    this.setParaNum(other.getParaNum());
    this.setFirstLine(other.getFirstLine());
  }

  /**
   * Constructs a document object.
   * 
   * @param titles2
   *          the titles.
   * @param authors2
   *          the authors.
   */
  public Document(String[] titles2, String[] authors2)
  {
    this.authors = authors2;
    this.titles = titles2;
  }

  /**
   * A toString method for Document.
   * 
   * @return the string.
   */
  public String toString()
  {
    String res = "Title: " + title + " Author: " + author + " Opus_num: " + getOpusNum()
        + " DocumentNum: " + getParaNum() + "\n";
    return res;
  }

  /**
   * A getter method for the author.
   * 
   * @return the string.
   */
  public String getAuthor()
  {
    return author;
  }

  /**
   * A getter method for title.
   * 
   * @return the string.
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * A getter method for the authors.
   * 
   * @return the string.
   */
  public String[] getAuthors()
  {
    return authors;
  }

  /**
   * A getter method for the titles.
   * 
   * @return the string.
   */
  public static String[] getTitles()
  {
    return titles;
  }

  /**
   * Retrieves the information from the document.
   * 
   * @param fpath
   *          the path
   * @return the Document
   * @throws IOException
   *           if the file is not found
   */
  public static Document getDocInfo(String fpath) throws IOException
  {
    FileReader in;
    BufferedReader br;
    String line, author = "", title = "";
    in = new FileReader(fpath);
    br = new BufferedReader(in);
    titles = new String[TITLE_MAX];
    authors = new String[AUTHOR_MAX];
    int x = 0;
    int y = 0;
    int lineNum = 1, startMark = -1, endMark = -1;
    while ((line = br.readLine()) != null)
    {
      if (line.startsWith(TITLE_HEADER))
      {
        title = line.replace("Title:", "").trim();
        titles[x] = title;
        // System.out.println("Here" + titles[x]);
        x++;
      }
      else if (line.startsWith(AUTHOR_HEADER))
      {
        author = line.replace("Author:", "").trim();
        authors[y] = author;
        // System.out.println("Here A" + authors[x]);
        y++;
      }
      else if (line.startsWith(STARS_HEADER) && line.contains(START_OF))
      {
        startMark = lineNum;
      }
      else if (line.startsWith(STARS_HEADER) && line.contains(END_OF) && startMark != -1)
      {
        endMark = lineNum;
        break;
      }
      lineNum++;
    }
    br.close();
    in.close();

    return new Document(titles, authors, author, title, startMark, endMark);
  }



  public String getFileName()
  {
    return fileName;
  }

  /**
   * Set the fileName of the file.
   * 
   * @param fileName
   */
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }

  /**
   * The paragraph number of the text file.
   * 
   * @return paraNum
   */
  public int getParaNum()
  {
    return paraNum;
  }

  /**
   * Setting the paragraph number of the text file.
   * 
   * @param paraNum
   */

  public void setParaNum(int paraNum)
  {
    this.paraNum = paraNum;
  }

  /**
   * Get the start mark of the paragraph.
   * 
   * @return startMark.
   */
  public int getStartMark()
  {
    return startMark;
  }

  /**
   * setting the startMark of the paragraph.
   * 
   * @param startMark
   */

  public void setStartMark(int startMark)
  {
    this.startMark = startMark;
  }

  /**
   * Getting the endMark of the paragraph.
   * 
   * @return endMark.
   */

  public int getEndMark()
  {
    return endMark;
  }

  /**
   * Setting the endMark of the paragraph.
   * 
   * @param endMark.
   */

  public void setEndMark(int endMark)
  {
    this.endMark = endMark;
  }

  /**
   * To get the Opus Number.
   * 
   * @return opusNum.
   */

  public int getOpusNum()
  {
    return opusNum;
  }

  /**
   * Setting the Opus number.
   * 
   * @param opusNum
   */

  public void setOpusNum(int opusNum)
  {
    this.opusNum = opusNum;
  }

  /**
   * Getting the First line.
   * 
   * @return firstLine.
   */

  public int getFirstLine()
  {
    return firstLine;
  }

  /**
   * setting the firstLine.
   * 
   * @param firstLine.
   */

  public void setFirstLine(int firstLine)
  {
    this.firstLine = firstLine;
  }


  /**
   * Comparing another document.
   * 
   * @return true.
   * @param obj.
   */
  

  public boolean equals(Object obj)
  {
    if (!(obj instanceof Document))

      return false;
    if (obj == this)
      return true;

    Document other = (Document) obj;
    return this.author.equals(other.author) && this.title.equals(other.title)
        && this.fileName.equals(other.fileName) && this.opusNum == other.opusNum
        && this.paraNum == other.paraNum;

  }

}
