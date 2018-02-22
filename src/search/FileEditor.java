package search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * This is the class that will index the selected directory.
 *
 * @author Shahzeb Khan
 */
public class FileEditor
{
  private static final String TITLE = "Title:";
  private static final String AUTHOR = "Author:";
  private File folder = new File("src/pirexData/");
  private File[] listOfFiles = folder.listFiles();
  private String[] authors = new String[listOfFiles.length];
  private String[] titles = new String[listOfFiles.length];
  private String title = "";
  private String author = "";
  private int x = -1;
  private String strCurrent;
  private String strCurrent2;

  /**
   * method sets the authors and titles to the variables in the class.
   * 
   * @param authors
   *          - array of all author names
   * @param titles
   *          - array of all title names
   */
  public void setEditor(String[] authors, String[] titles)
  {
    this.authors = authors;
    this.titles = titles;
  }

  /**
   * method gets the authors.
   * 
   * @return String array of all authors
   */
  public String[] getAuthors()
  {
    return this.authors;
  }

  /**
   * method gets the titles.
   * 
   * @return String array of all titles
   */
  public String[] getTitles()
  {
    return this.titles;
  }

  /**
   * findItem will take the header of line (Ex: "Author:") and return what follows the header.
   * 
   * @param name
   *          a header of a line of text.
   * @param file
   *          the file to be used
   * @return a String of what follows the header.
   */
  public static String findItem(String name, File file)
  {
    boolean here = false;
    String equals = "";
    Scanner input;
    try
    {
      input = new Scanner(file);

      while (input.hasNextLine() && !here)
      {
        if (input.next().equals(name))
        {
          if (name.equals(TITLE))
          {
            equals = input.nextLine().trim();

            while (!(input.hasNext(AUTHOR)))
            {
              equals = equals + " " + input.nextLine().trim();
            }
          }
          else
          {
            equals = input.nextLine().trim();
          }
          here = true;
        }
      }
      input.close();
    }
    catch (FileNotFoundException e)
    {
      return "File Not Found";
    }

    return equals;
  }

  /**
   * Edits the author name and makes changes to file.
   * 
   * @param input
   *          a String of the original name of the author
   * @param changeName
   *          a String of the changed name of the author
   * 
   * @throws IOException
   */
  public void editAuthor(String input, String changeName) throws IOException
  {

    for (File file : listOfFiles)
    {

      File originalFile = new File(file.getAbsolutePath());
      BufferedReader br = new BufferedReader(new FileReader(originalFile));

      File tempFile = new File("tempfile1.txt");
      PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

      String line = null;
      while ((line = br.readLine()) != null)
      {

        if (line.contains("Author: " + input))
        {
          strCurrent2 = line.substring(line.lastIndexOf(" "), line.length());
          if (strCurrent2 != null || !strCurrent2.trim().isEmpty())
          {
            line = "Author: " + changeName;
          }

        }
        pw.println(line);
        pw.flush();
      }
      pw.close();
      br.close();

      originalFile.delete();

      tempFile.renameTo(originalFile);

    }
  }

  /**
   * Edits the title name and makes changes to file.
   * 
   * @param input
   *          a String of the original name of the title
   * @param changeName
   *          a String of the changed name of the title
   * 
   * @throws IOException
   *           if the file does not exist
   */
  public void editTitle(String input, String changeName) throws IOException
  {

    for (File file : listOfFiles)
    {
      File originalFile = new File(file.getAbsolutePath());
      BufferedReader br = new BufferedReader(new FileReader(originalFile));

      File tempFile = new File("tempfile.txt");
      PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

      String line = null;

      while ((line = br.readLine()) != null)
      {

        if (line.contains("Title: " + input))
        {
          strCurrent = line.substring(line.lastIndexOf(" "), line.length());
          if (strCurrent != null || !strCurrent.trim().isEmpty())
          {
            line = "Title: " + changeName;
          }

        }
        pw.println(line);
        pw.flush();
      }
      pw.close();
      br.close();

      originalFile.delete();

      tempFile.renameTo(originalFile);

    }
  }

  /**
   * Finds that author and title name and sends it if user wants to edit it.
   * 
   * @throws IOException
   *           if the file does not exist.
   */
  public void editAll() throws IOException
  {
    int x = -1;

    for (File file : listOfFiles)
    {
      x++;

      title = findItem("Title:", file);
      author = findItem("Author:", file);

      authors[x] = author;
      titles[x] = title;
    }

    setEditor(authors, titles);
  }

}
