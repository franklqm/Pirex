package UserGui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Load enables the user to load in a previously saved search query.
 * 
 * @author Cassie Fox
 * @version V1
 */
public class Load extends JDialog implements ActionListener
{
  private JComboBox<String> comboBox;
  private JFrame displayList;

  /**
   * Constructs a load window.
   * 
   * @param savedSearches
   *          the searches that have been saved
   */
  public Load(ArrayList<String> savedSearches)
  {
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    displayList = new JFrame();

    displayList.setLayout(new BorderLayout());

    displayList.setTitle("Load a Query");

    JPanel southPanel;
    southPanel = new JPanel();
    southPanel.setLayout(new FlowLayout());

    comboBox = new JComboBox(savedSearches.toArray());
    comboBox.addActionListener(this);

    displayList.add(comboBox, BorderLayout.NORTH);

    JButton okButton;

    okButton = new JButton();
    okButton.setText("OK");

    southPanel.add(okButton);

    JButton cancelButton;
    cancelButton = new JButton();
    cancelButton.setText("Cancel");

    southPanel.add(cancelButton);

    okButton.addActionListener(this);
    cancelButton.addActionListener(this);

    displayList.add(southPanel, BorderLayout.SOUTH);

    displayList.setSize(400, 200);
    displayList.setVisible(true);

  }

  /**
   * Gets the selected query.
   * 
   * @return the query
   */
  public String getSelection()
  {
    return (String) comboBox.getSelectedItem();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    String command;
    command = e.getActionCommand();

    if (command.equals("OK"))
    {
      displayList.dispose();

    }

  }
}
