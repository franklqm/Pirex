package UserGui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import search.FileEditor;
import search.IndexMap;
import search.PirexData;

/**
 * The GUI.
 * 
 * @author Quentin Franklin
 *
 */
public class UserGUI extends JFrame
    implements DocumentListener, ActionListener, ListSelectionListener
{
  private File dir;
  private Container contentPane;
  private JLabel queryLabel, retrieveLabel, txtFile, type, author, title, line;
  private JTextArea lngDisplay, loadSearch, loadDisplay, summarizeText;
  private JPanel jpanel, jpanel1;
  private JButton clearButton, process, browse;
  private JScrollPane lngDsplyPne;
  private JScrollPane shortDisplay;
  private JScrollPane summarizeDisplay;
  private JComboBox<String> typeList;
  private JTextField searchSearch, loadAuthor, loadTitle;
  private IndexMap inde;
  private NavigableMap<String, ArrayList<search.Document>> index;
  private JList<String> list;
  private HashMap<String, ArrayList<String>> paragraph;
  private ArrayList<search.Document> documents;
  private ArrayList<String> savedSearches;
  private String searchTerm;

  /**
   * Makes the GUI.
   * @throws IOException
   *           if the file does not exist
   */
  public UserGUI() throws IOException
  {
    super("Pirex");
    inde = deserialize();
    setSize(805, 425);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    init();

    PirexData store = new PirexData();

    dir = new File("src/pirexData/");
    File[] files = dir.listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".txt");
      }
    });

    try
    {
      inde.indexFiles(files);
      for (int i = 0; i < files.length; i++)
      {
        summarizeText.setText(summarizeText.getText() + "Opus " + i + ":" + inde.getProcessAuthor()[i] + "  "
                + inde.getProcessTitle()[i] + "  " + inde.getProcessOpus()[i] + " documents\n"
                + "       " + files[i].getAbsolutePath() + "\n");
      }
      summarizeText.setText(summarizeText.getText() + "\n\nIndex terms: " + inde.getTotalInd()
          + "\nPostings:      " + inde.getPostings());
    }
    catch (IOException e)
    {
	e.printStackTrace();
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
	e.printStackTrace();
    }

    addMenu();
    setVisible(true);
    setResizable(false);
    setLayout(null);
  }

  /**
   * Serializes the map.
   * 
   * @param ind   the index
   * @throws IOException   if the file is not found
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
   * deserializes the map.
   * 
   * @return an index map
   * @throws ClassNotFoundException
   *           if the class is not found
   */
  public static IndexMap deserialize() 
  {
      FileInputStream fileIn;
      ObjectInputStream in;
      File input_file;
    IndexMap index;
    FileOutputStream out;
    
    index = new IndexMap();
    
    //Try to open index, create if it does not exist
    try
    {
	input_file = new File("index.ser");
	if(input_file.exists() && !input_file.isDirectory()) { 
	      fileIn = new FileInputStream(input_file);
	}
	else
	{
	    out = new FileOutputStream("index.ser");
	    out.close();
	    fileIn = new FileInputStream("index.ser");
	}
	in = new ObjectInputStream(fileIn);
	index = (IndexMap) in.readObject();
    }
    catch (java.io.IOException | ClassNotFoundException e)
    {
      e.printStackTrace();
    }

    return index;
  }

  /**
   * Initializes the GUI.
   */
  public void init()
  {
    savedSearches = new ArrayList<String>();
    JPanel search, load, summary;
    JTabbedPane groups;
    String[] fileType = {"Project Gutenberg File"};

    contentPane = getContentPane();
    contentPane.setLayout(null);

    groups = new JTabbedPane();
    contentPane.add(groups);
    groups.setBounds(10, 10, 780, 355);

    // GUI Tabs
    search = new JPanel();
    search.setLayout(null);

    load = new JPanel();
    load.setLayout(null);

    summary = new JPanel();
    summary.setLayout(null);

    searchSearch = new JTextField(20);
    
    queryLabel = new JLabel("Query:");
    retrieveLabel = new JLabel("", JLabel.LEFT);
    clearButton = new JButton("Clear");
    lngDisplay = new JTextArea();
    lngDsplyPne = new JScrollPane(lngDisplay, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    jpanel = new JPanel();
    jpanel.setLayout(new FlowLayout());
    jpanel1 = new JPanel();
    jpanel1.setLayout(null);

    shortDisplay = new JScrollPane(jpanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    txtFile = new JLabel("Text File:");
    loadSearch = new JTextArea(1, 50);
    browse = new JButton("Browse");
    type = new JLabel("Text File Type:");
    typeList = new JComboBox(fileType);
    title = new JLabel("Title:");
    loadTitle = new JTextField(20);
    author = new JLabel("Author:");
    loadAuthor = new JTextField(20);
    line = new JLabel("____________________________________________________________________"
        + "__________________________________________________________");
    process = new JButton("Process");
    process.setEnabled(false);
    loadDisplay = new JTextArea(8, 50);

    summarizeText = new JTextArea(20, 100);
    summarizeDisplay = new JScrollPane(summarizeText);
    summarizeDisplay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    typeList.setSelectedIndex(0);
    typeList.addActionListener(this);
    searchSearch.addActionListener(this);
    loadAuthor.addActionListener(this);
    loadTitle.addActionListener(this);
    searchSearch.setActionCommand("QueryEnter");
    loadAuthor.setActionCommand("N");
    loadTitle.setActionCommand("Y");

    search.add(searchSearch);
    search.add(shortDisplay);
    search.add(queryLabel);
    search.add(retrieveLabel);
    search.add(lngDsplyPne);
    search.add(clearButton);

    load.add(txtFile);
    load.add(loadSearch);
    load.add(browse);
    load.add(type);
    load.add(typeList);
    load.add(title);
    load.add(loadTitle);
    load.add(author);
    load.add(loadAuthor);
    load.add(line);
    load.add(process);
    load.add(loadDisplay);

    summary.add(summarizeDisplay);

    searchSearch.setBounds(65, 10, 595, 20);
    shortDisplay.setBounds(10, 40, 750, 145);
    queryLabel.setBounds(10, 10, 55, 20);
    lngDisplay.setBounds(10, 215, 750, 110);
    lngDsplyPne.setBounds(10, 215, 750, 110);
    retrieveLabel.setBounds(10, 185, 750, 20);
    clearButton.setBounds(680, 10, 80, 20);

    txtFile.setBounds(10, 5, 65, 20);
    loadSearch.setBounds(85, 5, 580, 20);
    browse.setBounds(675, 5, 90, 20);
    type.setBounds(10, 40, 110, 20);
    typeList.setBounds(120, 40, 645, 25);
    title.setBounds(10, 80, 50, 20);
    loadTitle.setBounds(55, 80, 350, 20);
    author.setBounds(435, 80, 60, 20);
    loadAuthor.setBounds(500, 80, 265, 20);
    line.setBounds(10, 100, 760, 20);
    process.setBounds(10, 130, 90, 20);
    loadDisplay.setBounds(10, 160, 755, 160);

    summarizeDisplay.setBounds(10, 10, 755, 310); // comment this out and

    clearButton.addActionListener(this); // sets textArea to ""
    browse.addActionListener(this); // file chooser
    process.addActionListener(this);

    groups.addTab("Search for Documents", search);
    groups.addTab("   Load Documents   ", load);
    groups.addTab("Summarize Documents ", summary);

  }

  public Highlighter highlightText(Highlighter highlighter, String text, HighlightPainter painter, String term)
  {
    String textMod = text.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "");
    String textLow = text.toLowerCase();
    int p0 = textMod.indexOf(term);
    int p0_mod = -1;

    while(p0 >= 0)
    {
      p0_mod = textLow.indexOf(term, p0_mod+1);
      if(p0 == 0 || textMod.charAt(p0-1) == ' ' || textMod.charAt(p0-1) == '\n')
      {
        int p1 = p0_mod + term.length();
        try {
          highlighter.addHighlight(p0_mod, p1, painter);
        }
        catch (BadLocationException ex)
        {
          System.err.println("Cant highlight");
        }
      }
      p0 = textMod.indexOf(term, p0+1);
    }

    return highlighter;
  }

  /**
   * The listener for the lists.
   * 
   * @param e
   *          the event
   */
  public void valueChanged(ListSelectionEvent e)
  {
    int i;
    
    Highlighter highlighter = lngDisplay.getHighlighter();
    HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
    HashMap<String, ArrayList<String>> paragraph1;
    ArrayList<search.Document> documents1;

    paragraph1 = paragraph;
    documents1 = documents;
    JList list;
    Object[] selected;

    list = (JList) (e.getSource());
    selected = list.getSelectedValues();

    for (i = 0; i < selected.length; i++)
    {
      for (search.Document d : documents1)
      {
        if ((d.getAuthor() + " " + d.getTitle() + "   " + d.getParaNum() + "   "
            + paragraph.get(d.getFileName()).get(d.getParaNum()).substring(0, d.getFirstLine()))
                .equals((String) selected[i]))
        {
      
          String text = paragraph.get(d.getFileName()).get(d.getParaNum());
          lngDisplay.setText(text);
          lngDisplay.setEditable(false);

          int boolInd;

          if ((boolInd = searchTerm.indexOf("||")) != -1)
          {
            highlightText(highlighter, text, painter, searchTerm.substring(0, boolInd).replaceAll("\\s+","") );
            highlightText(highlighter, text , painter, searchTerm.substring(boolInd + 2).replaceAll("\\s+","") );
          }
          else if ((boolInd = searchTerm.indexOf("&&")) != -1)
          {
            highlightText(highlighter, text, painter, searchTerm.substring(0, boolInd).replaceAll("\\s+","") );
            highlightText(highlighter, text , painter, searchTerm.substring(boolInd + 2).replaceAll("\\s+","") );
          }
          else
            highlightText(highlighter, text , painter, searchTerm);
        }
      }
    }
  }

  @Override
  public void insertUpdate(DocumentEvent e)
  {

  }

  @Override
  public void removeUpdate(DocumentEvent e)
  {

  }

  @Override
  public void changedUpdate(DocumentEvent e)
  {

  }

  public String getDirPath()
  {
    return dir.getAbsolutePath();
  }

  /**
   * The action performed.
   * 
   * @param event
   *          the event
   */
  public void actionPerformed(ActionEvent event)
  {
    String command;
    command = event.getActionCommand();

    if (command.equals("Clear"))
    {
      jpanel.removeAll();
      searchSearch.setText("");
      jpanel.revalidate();
      jpanel.repaint();
      lngDisplay.setText("");
    }
    else if (command.equals("Browse"))
    {
      final JFileChooser fc = new JFileChooser();
      int returnVal = fc.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION
          && fc.getSelectedFile().getAbsolutePath().endsWith(".txt"))
      {
        File file = fc.getSelectedFile(); // load file Kenneth
        loadSearch.setText(file.getAbsolutePath());
        loadDisplay.setText("");
        loadAuthor.setText("");
        File[] processArray = new File[1];
        String processName = loadSearch.getText();
        File processFile = new File(processName);
        processArray[0] = processFile;
        
        PirexData store = new PirexData();
        try
        {
          inde.indexFiles(processArray);
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        process.setEnabled(true);
      }
    }
    if (command.equals("Process"))
    {
      search.Document info;
      File[] processArray = new File[1];
      String processName = loadSearch.getText();
      File processFile = new File(processName);
      processArray[0] = processFile;
      PirexData store = new PirexData();

      try
      {
        inde.indexFiles(processArray);
        store.pirexFolder(loadSearch.getText(), "src/pirexData/");
        loadDisplay.setText("Opus: " + loadSearch.getText() + "\n" + "Title: "
            + inde.getProcessTitle()[0] + "\nAuthor: " + inde.getProcessAuthor()[0] + "\nOpus size: "
            + inde.getOpusSize() + " documents\nOpus number: " + inde.getOpNum()
            + "\nNew index terms: " + inde.getNewIndexTerms() + "\nNew postings: "
            + inde.getPostings() + "\nTotal index terms: " + inde.getTotalInd()
            + "\nTotal postings: " + inde.getTotalPostings());
        summarizeText.setText(inde.getSummarizeDisplay() + "\n\nIndex terms: " + inde.getTotalInd()
            + "\nPostings:      " + inde.getPostings());
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      loadSearch.setText("");
      loadAuthor.setText("");
      loadTitle.setText("");
      jpanel.revalidate();
      jpanel.repaint();
    }
    else if (command.equals("QueryEnter"))
    {
      jpanel.removeAll();
      jpanel.revalidate();
      jpanel.repaint();
      lngDisplay.setText("");
      ArrayList<search.Document> searchResults;
      ArrayList<search.Document> docs;
      String term;
      index = inde.getIndex();
      term = searchSearch.getText().trim().replace("\n", "");
      searchTerm = term;
      int boolInd = -1;
      if ((boolInd = term.indexOf("||")) != -1)
      {
        Set<search.Document> finalRes = new HashSet<search.Document>();
        finalRes.addAll(IndexMap.search(term.substring(0, boolInd), index));
        finalRes.addAll(IndexMap.search(term.substring(boolInd + 2), index));
        searchResults = new ArrayList<search.Document>(finalRes);
      }
      else if ((boolInd = term.indexOf("&&")) != -1)
      {
        ArrayList<search.Document> resultOne = IndexMap.search(term.substring(0, boolInd), index);
        ArrayList<search.Document> resultTwo = IndexMap.search(term.substring(boolInd + 2), index);
        resultOne.retainAll(resultOne);
        searchResults = resultOne;
      }
      else
        searchResults = IndexMap.search(term, index);
      if (searchResults == null || searchResults.size() == 0)
      {
        System.out.println("No results found");
      }
      else
      {
        docs = searchResults;
        try
        {
          printRes(docs, inde.getAllParagraphs());
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    else if (command.equals("About"))
    {
      JOptionPane.showMessageDialog(this,
          "Pirex (personal information retrieval experimental system)"
              + "\n created by Team09 for CS 345 at JMU",
          "About", JOptionPane.PLAIN_MESSAGE);
    }
    else if (command.equals("Exit"))
    {
      System.exit(1);
      setVisible(false);
    }
    else if (command.equals("Save"))
    {

      if (!(searchSearch.getText().equals(null)))
      {
        savedSearches.add(searchSearch.getText());
      }
    }
    else if (command.equals("Load"))
    {
      Load load;
      load = new Load(savedSearches);
      searchSearch.setText(load.getSelection());  
      jpanel.removeAll();
      jpanel.revalidate();
      jpanel.repaint();    
      lngDisplay.setText("");
    }

    if (command.equals("Edit Author"))
    {
        String response;
        FileEditor fe = new FileEditor();
        try
        {
          fe.editAll();
          String[] choices = fe.getAuthors();
          String input = (String) JOptionPane.showInputDialog(null, "Choose now...",
              "Choose the Author Name to edit: ", JOptionPane.QUESTION_MESSAGE, null, // Use
              choices, // Array of choices
              choices[0]); // Initial choice

          response = JOptionPane.showInputDialog("New Author Name: ");

          if (response == null || input == null)
          {
            JOptionPane.showMessageDialog(null, "NO CHANGE MADE");
          }

          else
          {
            fe.editAuthor(input.trim(), response.trim());
            JOptionPane.showMessageDialog(null, "Change Made to File");
          }
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
    }

    if (command.equals("Edit Title"))
    {
        String responsed;
        FileEditor fe = new FileEditor();
        try
        {
          fe.editAll();
          String[] choices = fe.getTitles();
          String inputing = (String) JOptionPane.showInputDialog(null, "Choose now...",
              "Choose the Title Name to edit: ", JOptionPane.QUESTION_MESSAGE, null, // Use
              choices, // Array of choices
              choices[0]); // Initial choice

          responsed = JOptionPane.showInputDialog("New Title Name: ");

          if (responsed == null || inputing == null)
          {
            JOptionPane.showMessageDialog(null, "NO CHANGE MADE");
          }

          else
          {
            fe.editTitle(inputing.trim(), responsed.trim());
            JOptionPane.showMessageDialog(null, "Change Made to File");
          }
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
    }
    else if (command.equals("Index"))
    {
      File htmlPage = new File("helpInformation/pirexInfo.html");
      try
      {
        Desktop.getDesktop().browse(htmlPage.toURI());
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
  
  /**
   * Prints the results.
   * 
   * @param docs
   *          the documents
   * @param allParagraphs
   *          the paragraphs
   * @throws IOException
   *           if the file does not exist
   */
  public void printRes(ArrayList<search.Document> docs,
      HashMap<String, ArrayList<String>> allParagraphs) throws IOException
  {
    String[] results = new String[100];
    int i = 0;
    for (search.Document d : docs)
    {
      try
      {
        if (i < 100)
        {
          results[i] = (d.getAuthor() + " " + d.getTitle() + "   " + d.getParaNum() + "   "
              + allParagraphs.get(d.getFileName()).get(d.getParaNum()).substring(0,
                  d.getFirstLine()));
          i++;
        }
      }
      catch (java.lang.ArrayIndexOutOfBoundsException e)
      {

      }
    }
    paragraph = allParagraphs;
    documents = docs;
    list = new JList(results);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    list.addListSelectionListener(this);

    jpanel.add(list);
    list.setBounds(1, 1, 747, 142);
    
    jpanel.setBounds(1, 1, 747, 142);
  }

  public void addMenu()
  {
    JMenuBar menuBar;
    JMenu fileMenu, helpMenu, optionMenu;
    JMenuItem load, export, save, exit, about, document, sources, index, author, title;

    menuBar = new JMenuBar();

    // File menu
    fileMenu = new JMenu("File");

    load = new JMenuItem("Load");
    fileMenu.add(load);
    
    save = new JMenuItem("Save");
    fileMenu.add(save);

    exit = new JMenuItem("Exit");
    fileMenu.add(exit);

    menuBar.add(fileMenu);

    // Help Menu with About
    helpMenu = new JMenu("Help");

    about = new JMenuItem("About");
    helpMenu.add(about);

    index = new JMenuItem("Index");
    helpMenu.add(index);

    menuBar.add(helpMenu);

    // Options Menu
    optionMenu = new JMenu("Options");

    author = new JMenuItem("Edit Author");
    optionMenu.add(author);

    title = new JMenuItem("Edit Title");
    optionMenu.add(title);

    menuBar.add(optionMenu);

    // Add Listeners
    load.addActionListener(this);
    save.addActionListener(this);
    exit.addActionListener(this);
    about.addActionListener(this);
    index.addActionListener(this);
    author.addActionListener(this);
    title.addActionListener(this);

    setJMenuBar(menuBar);

  }
}