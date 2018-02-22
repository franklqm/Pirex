package search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * PirexStorage will store the files in a specific directory.
 * 
 * @author Kenny Boadu
 */
public class PirexData
{

  private static File Destination;
  private static File source;
  private static final int BYTE_SIZE = 1024;

  /**
   * Creates the the pirex folder.
   * 
   * @param sourcePath
   *          the source path
   * @param destPath
   *          the dest path
   * @throws IOException
   *           if there path is invalid
   * @return the file
   */
  public static File pirexFolder(String sourcePath, String destPath) throws IOException
  {
    source = new File(sourcePath);
    Destination = new File(destPath);
    //InputStream in = ClassLoader.getSystemResourceAsStream("src/pirexData/");

    InputStream in = new FileInputStream(source);
    OutputStream out = new FileOutputStream(Destination + "/" + source.getName());
    // Copy the bits from input stream to output stream
    byte[] buf = new byte[BYTE_SIZE];
    int len;

    try
    {
      while ((len = in.read(buf)) > 0)
      {
        out.write(buf, 0, len);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    in.close();
    out.close();
    return source;
  }

  /**
   * Gets the list of files.
   * 
   * @param dirPath
   *          the path
   * @return the file
   */
  public static File[] getFilesList(String dirPath)
  {
    File folder = new File(dirPath);
    return folder.listFiles();

  }
}
