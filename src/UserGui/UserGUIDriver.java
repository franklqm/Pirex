package UserGui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import search.Document;
import search.IndexMap;

/**
 * Start of the Application.
 * 
 * @author Quentin Franklin, Jazmine Kelly
 *
 */
public class UserGUIDriver
{

  /**
   * Serializes the object.
   * 
   * @param ind
   *          the index
   * @throws IOException
   *           if the file isnt found.
   */
  public static void serialize(IndexMap ind) throws IOException
  {
    FileOutputStream fileOut = new FileOutputStream("index.ser");
    ObjectOutputStream out = new ObjectOutputStream(fileOut);
    out.writeObject(ind);

    out.close();
    fileOut.close();
  }

  /**
   * Deserializes an object.
   * 
   * @return the map
   * @throws java.lang.ClassNotFoundException
   *           if it is not found
   */
  public static IndexMap deserialize() throws java.lang.ClassNotFoundException
  {
    IndexMap index = new IndexMap();
    try
    {
      FileInputStream fileIn = new FileInputStream("index.ser");
      System.out.println("Deserializing index data");
      ObjectInputStream in = new ObjectInputStream(fileIn);
      index = (IndexMap) in.readObject();
      System.out.println("Deserialized index data");
    }
    catch (java.io.IOException e)
    {

    }

    return index;
  }

  /**
   * Main.
   * 
   * @param args
   *          unused
   * @throws ClassNotFoundException
   *           if the class is not found
   * @throws IOException
   *           if the file is not found
   */
  public static void main(String[] args) throws IOException
  {
    UserGUI gui = new UserGUI();

    String dir_path = "src/pirexData/";
  }

  /**
   * Prints the results.
   * 
   * @param docs
   *          the documents
   * @param allParagraphs
   *          the paragraph
   */
  public static void printRes(ArrayList<Document> docs,
      HashMap<String, ArrayList<String>> allParagraphs)
  {
    int ind = 1;

    for (Document d : docs)
    {
      System.out.println("Result " + ind);
      System.out.println(d);
      System.out.println("Opus: " + allParagraphs.get(d.getFileName()).get(d.getParaNum()));
      System.out.println("");
      ind++;
    }
  }
}
