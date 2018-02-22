package search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * This is the class that will index the selected directory.
 *
 * @author
 */

public class IndexMap implements Serializable
{
  private static java.util.List<String> stopWords = Arrays.asList("a", "an", "and", "are", "but",
      "did", "do", "does", "for", "had", "has", "is", "it", "its", "of", "or", "that", "the",
      "this", "to", "were", "which", "with");
  private static int startingLine = 0;
  private int newIndexTerms, postings, totalInd, totalPostings, opusSize, opNum;
  private String[] processAuthor, processTitle, opusText;
  private String summarizeDisplay;
  private ArrayList<File> indexedFiles = new ArrayList<File>();
  private NavigableMap<String, ArrayList<Document>> index 
      = new TreeMap<String, ArrayList<Document>>();
  private int change = 0;
  private HashMap<String, ArrayList<String>> allParagraphs 
      = new HashMap<String, ArrayList<String>>();

  /**
   * 
   */
  public IndexMap()
  {
    setOpNum(-1);
    setSummarizeDisplay("");
  }

  /**
   * Retrieves the paragraph.
   * 
   * @param fname
   *          the file name
   * @param linenum
   *          the line number
   * @return the paragraph
   * @throws IOException
   *           if the name is not found
   */
  public ArrayList<String> getParagraph(String fname, int linenum) throws IOException
  {
    FileReader in = new FileReader(fname);
    BufferedReader br = new BufferedReader(in);
    int lineNum = 1, started = -1;
    String line;
    ArrayList<String> paragraphs = new ArrayList<String>();

    while ((line = br.readLine()) != null)
    {

      if (lineNum < linenum)
      {
        lineNum++;
        continue;
      }
      if (line.equals("") && started == -1)
      {
        lineNum++;
        continue;
      }
      else if (line.equals("") && started == 1)
        break;
      if (started == -1)
        startingLine = lineNum;
      paragraphs.add(line);
      started = 1;
      lineNum++;
    }
    if (started == -1)
      startingLine = -1;
    br.close();
    return paragraphs;
  }

  /**
   * Makes a string object.
   * 
   * @param paragraphs
   *          the paragraphs
   * @return the String
   */
  public static String makeString(ArrayList<String> paragraphs)
  {
    StringBuilder sb = new StringBuilder();
    for (String s : paragraphs)
    {
      sb.append(s);
      sb.append("\n");
    }
    String st = sb.toString();
    st = st.substring(0, st.length() - 1);
    return st;
  }

  /**
   * Indexes the files.
   * 
   * @param fileList
   *          the list of files.
   * @throws IOException
   *           if the files are not found.
   */
  public void indexFiles(File[] fileList) throws IOException
  {

    FileReader in;
    BufferedReader br;
    Document info;

    int lineNum = 1, paraNum = 0, newInd = 0, newPostings = 0;

    setChange(0);
    
    processAuthor = new String[fileList.length];
    processTitle = new String[fileList.length];
    opusText = new String[fileList.length];
    ArrayList<Document> val;
    ArrayList<String> paragraphs;
    String nextWordTerm, prevWord = "", prevWordTerm;

    for (int i = 0; i < fileList.length; i++)
    {
      info = Document.getDocInfo(fileList[i].getPath());
      processAuthor[i] = info.getAuthor();
      processTitle[i] = info.getTitle();

      if (fileList[i].isFile() && indexedFiles.indexOf(fileList[i]) == -1)
      {
        setChange(1);
        newInd = 0;
        paraNum = 0;

        allParagraphs.put(fileList[i].getName(), new ArrayList<String>());

        in = new FileReader(fileList[i].getPath());
        br = new BufferedReader(in);

        lineNum = 1;
        prevWord = "";

        while ((br.readLine()) != null)
        {
          if (lineNum > info.getStartMark() && lineNum < info.getEndMark())
          {
            paragraphs = getParagraph(fileList[i].getPath(), lineNum);
            paraNum += 1;

            allParagraphs.get(fileList[i].getName()).add(makeString(paragraphs));

            if (startingLine != -1)
              lineNum = startingLine;

            for (String currLine : paragraphs)
            {

              String[] wordsi = currLine.split("\\s+");
              java.util.List<String> wordsList = new ArrayList<String>(Arrays.asList(wordsi));
              java.util.List<String> finalWords = new ArrayList<String>();
              for (String word : wordsList)
              {
                word = word.replaceAll("[^A-Za-z0-9 ]", "").toLowerCase();
                if (!(word.equals("") || stopWords.indexOf(word) != -1))
                  finalWords.add(word);
              }
              String[] words = finalWords.toArray(new String[finalWords.size()]);
              for (int j = 0; j < words.length; j++)
              {
                nextWordTerm = "";
                prevWordTerm = "";

                Document curr = new Document(info);
                curr.setFileName(fileList[i].getName());
                curr.setOpusNum(getOpNum());

                if (j == 0 && !prevWord.isEmpty())
                {
                  prevWordTerm = prevWord + "_" + words[j];
                  prevWord = "";
                }

                if (j != words.length - 1)
                  nextWordTerm = words[j] + "_" + words[j + 1];
                else
                  prevWord = words[j];

                curr.setParaNum(paraNum - 1);
                curr.setFirstLine(paragraphs.get(0).length());
                val = index.get(words[j]);
                if (val == null)
                {
                  newInd += 1;
                  index.put(words[j], new ArrayList<Document>());
                }
                if (index.get(words[j]).indexOf(curr) == -1)
                  index.get(words[j]).add(curr);
                newPostings += 1;

                if (!prevWordTerm.isEmpty())
                {
                  val = index.get(prevWordTerm);
                  if (val == null)
                    index.put(prevWordTerm, new ArrayList<Document>());
                  if (index.get(prevWordTerm).indexOf(curr) == -1)
                    index.get(prevWordTerm).add(curr);
                }

                if (nextWordTerm.length() > 0)
                {
                  val = index.get(nextWordTerm);
                  if (val == null)
                    index.put(nextWordTerm, new ArrayList<Document>());
                  if (index.get(nextWordTerm).indexOf(curr) == -1)
                    index.get(nextWordTerm).add(curr);
                }
              }
              lineNum += 1;
            }
            if (startingLine == -1)
            {
              break;
            }
          }
          if (startingLine == -1)
          {
            setOpNum(getOpNum() + 1);
            break;
          }

          lineNum += 1;

          if (lineNum >= info.getEndMark())
          {
            setOpNum(getOpNum() + 1);
            setTotalInd(getTotalInd() + newInd);
            setTotalPostings(getTotalPostings() + newPostings);
            indexedFiles.add(fileList[i]);
            break;
          }
        }

      }
      setSummarizeDisplay(getSummarizeDisplay() + "Opus " + getOpNum() + ":" + info.getAuthor()
          + "  " + info.getTitle() + "  " + paraNum + " documents\n" + "       "
          + fileList[i].getAbsolutePath() + "\n");
      System.out.println();
      opusText[i] = Integer.toString(paraNum);
    }
    setOpusSize(paraNum);
    setPostings(newPostings);
    setNewIndexTerms(newInd);
  }

  public String[] getProcessAuthor()
  {
    return processAuthor;
  }
  
  public String[] getProcessTitle()
  {
    return processTitle;
  }
  
  public String[] getProcessOpus()
  {
    return opusText;
  }
  
  
  /**
   * Sorts the map.
   * 
   * @param term
   *          the term
   * @param index
   *          the index
   * @return the sorted map
   */
  public static ArrayList<Document> search(String term,
      NavigableMap<String, ArrayList<Document>> index)
  {
    String[] wordsi = term.split("\\s+");
    java.util.List<String> words_list = new ArrayList<String>(Arrays.asList(wordsi));
    java.util.List<String> final_words = new ArrayList<String>();
    for (String word : words_list)
    {
      word = word.replaceAll("[^A-Za-z0-9 ]", "").toLowerCase();
      if (!(word.equals("") || stopWords.indexOf(word) != -1))
        final_words.add(word);
    }
    String search_term;
    ArrayList<Document> results = new ArrayList<Document>(), curr_results;
    for (int i = 0; i < final_words.size() - 1; i++)
    {
      search_term = final_words.get(i) + "_" + final_words.get(i + 1);
      if (i == 0)
      {
        curr_results = index.subMap(search_term, search_term + 1).get(search_term);
        if (curr_results != null)
          results = new ArrayList<>(curr_results);
        else
        {
          results.clear();
          break;
        }
      }
      else
      {
        if (results.isEmpty())
        {
          results.clear();
          break;
        }
        else
        {
          curr_results = index.subMap(search_term, search_term + 1).get(search_term);
          if (curr_results != null && !curr_results.isEmpty())
            results.retainAll(index.subMap(search_term, search_term + 1).get(search_term));
          else
          {
            results.clear();
            break;
          }
        }
      }
    }
    if (final_words.size() == 1)
    {
      search_term = final_words.get(0);
      results = index.subMap(search_term, search_term + 1).get(search_term);
    }
    return results;
  }

  /**
   * This method returns the numbers of indexed terms.
   * 
   * @return int the number of indexed terms
   */
  public int getNewIndexTerms()
  {
    return newIndexTerms;
  }

  /**
   * This method sets the newIndexTerm.
   * 
   * @param newIndexTerms
   *          the number of new index terms
   */
  public void setNewIndexTerms(int newIndexTerms)
  {
    this.newIndexTerms = newIndexTerms;
  }

  /**
   * This method returns the number of postings.
   * 
   * @return int the number of postings
   */
  public int getPostings()
  {
    return postings;
  }

  /**
   * This method sets the number of postings.
   * 
   * @param postings
   *          the number of postings
   */
  public void setPostings(int postings)
  {
    this.postings = postings;
  }

  /**
   * This method returns the total number of indexed documents???***.
   * 
   * @return int total number of index ??
   */
  public int getTotalInd()
  {
    return totalInd;
  }

  /**
   * This method sets totalInd to ??
   * 
   * @param totalInd
   */
  public void setTotalInd(int totalInd)
  {
    this.totalInd = totalInd;
  }

  /**
   * Returns the total number of postings.
   * 
   * @return int total number of postings
   */
  public int getTotalPostings()
  {
    return totalPostings;
  }

  /**
   * Sets the total number of postings.
   * 
   * @param totalPostings
   *          total number of postings
   */
  public void setTotalPostings(int totalPostings)
  {
    this.totalPostings = totalPostings;
  }

  /**
   * Returns the size of the opus.
   * 
   * @return int size of opus
   */
  public int getOpusSize()
  {
    return opusSize;
  }

  /**
   * Sets the opus size.
   * 
   * @param opusSize
   *          size of the opus
   */
  public void setOpusSize(int opusSize)
  {
    this.opusSize = opusSize;
  }

  /**
   * Returns a summary of the opuses. ???
   * 
   * @return String summary of opuses
   */
  public String getSummarizeDisplay()
  {
    return summarizeDisplay;
  }

  /**
   * Sets the summary to display.
   * 
   * @param summarizeDisplay
   *          the summary to display
   */
  public void setSummarizeDisplay(String summarizeDisplay)
  {
    this.summarizeDisplay = summarizeDisplay;
  }

  /**
   * This method returns getIndexedFiles.
   * 
   * @return ArrayList the array list of indexed files
   */
  public ArrayList<File> getIndexedFiles()
  {
    return indexedFiles;
  }

  /**
   * This method sets the indexed files.
   * 
   * @param indexedFiles
   *          ArrayList of files
   */
  public void setIndexedFiles(ArrayList<File> indexedFiles)
  {
    this.indexedFiles = indexedFiles;
  }

  /**
   * This method returns the index.
   * 
   * @return NavigableMap the map of the index
   */
  public NavigableMap<String, ArrayList<Document>> getIndex()
  {
    return index;
  }

  /**
   * This method sets the index.
   * 
   * @param index
   *          ArrayList of documents
   */
  public void setIndex(NavigableMap<String, ArrayList<Document>> index)
  {
    this.index = index;
  }

  /**
   * Returns the change variable.
   * 
   * @return int
   */
  public int getChange()
  {
    return change;
  }

  /**
   * Sets the change variable.
   * 
   * @param change
   */
  public void setChange(int change)
  {
    this.change = change;
  }

  /**
   * This method returns the opus number.
   * 
   * @return int Opus number
   */
  public int getOpNum()
  {
    return opNum;
  }

  /**
   * This method sets the opus number.
   * 
   * @param opNum
   *          the number of the opus
   */
  public void setOpNum(int opNum)
  {
    this.opNum = opNum;
  }

  /**
   * This method returns the paragraphs.
   * 
   * @return HashMap the map of all paragraphs in opus
   */
  public HashMap<String, ArrayList<String>> getAllParagraphs()
  {
    return allParagraphs;
  }

  /**
   * This method sets all of the paragraphs.
   * 
   * @param allParagraphs
   *          map of all the paragraphs
   */
  public void setAllParagraphs(HashMap<String, ArrayList<String>> allParagraphs)
  {
    this.allParagraphs = allParagraphs;
  }

}
