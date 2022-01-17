package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class TextEditor /*implements ActionListener*/ {
  private static final String PASTE = "Paste";
  private static final String COPY = "Copy";
  private static final String CUT = "Cut";
  private static final String OPEN = "Open";
  private static final String MENU = "Menu";
  private JPanel container = new JPanel();
  private String file = null;
  private JTextArea textArea;
  private JPanel jpanel;
  private JScrollPane jscrollPane;
  private UndoManager undoManager;
  JTextField text = new JTextField("");
  JPanel panelSearch = new JPanel();
  JLabel occurencesLabel = new JLabel();
  MyJButton find = new MyJButton("Find/Next");
  TitledBorder titledBorder = null;
  private int pos = 0;
  /* private Color backgroundColor1 = Color.DARK_GRAY;
    private Color backgroundColor2 = Color.DARK_GRAY;
    private Color foregroundColor = Color.WHITE;
  */
  public TextEditor() {
    BoxLayout boxlayout = new BoxLayout(container, BoxLayout.Y_AXIS);
    container.setLayout(boxlayout);

    textArea = new JTextArea("");
    textArea.setCaretColor(Util.FOREGROUND_COLOR);
    /*
    Wrap text (if you want to wrap the text you need to enable the preferred size on the textArea and/or the jpanel)
    */
    // textArea.setLineWrap(true);
    // textArea.setWrapStyleWord(true);

    textArea.setForeground(Util.FOREGROUND_COLOR);
    textArea.setBackground(Util.BACKGROUD_COLOR);
    jpanel = new JPanel(new BorderLayout());
    jpanel.setBackground(Util.BACKGROUD_COLOR);
    // jpanel.add(createSearchPanel(), BorderLayout.NORTH);
    jpanel.add(textArea, BorderLayout.CENTER);

    // textArea.setEditable(false);
    container.add(createSearchPanel());
    container.setBackground(Util.BACKGROUD_COLOR);
    // textArea.setPreferredSize(new Dimension(Util.WIDTH * 4 / 5, Util.HEIGHT));
    // jpanel.setPreferredSize(new Dimension(Util.WIDTH * 4 / 5, Util.HEIGHT * 2));
    jscrollPane =
        Util.createJScrollPaneVert(jpanel, new Dimension(Util.WIDTH * 4 / 5, Util.HEIGHT));

    jscrollPane.setBackground(Util.BACKGROUD_COLOR);
    container.add(jscrollPane);
    addUndo();
    // createContextMenu();
    textArea.addMouseListener(new ContextMenuMouseListener());
  }

  private JPanel createSearchPanel() {

    BoxLayout boxlayout = new BoxLayout(panelSearch, BoxLayout.X_AXIS);
    panelSearch.setLayout(boxlayout);

    text.setForeground(Util.FOREGROUND_COLOR);
    text.setBackground(Util.BACKGROUD_COLOR);
    occurencesLabel.setOpaque(true);
    occurencesLabel.setForeground(Util.FOREGROUND_COLOR);
    occurencesLabel.setBackground(Util.BACKGROUD_COLOR);
    // occurencesLabel.setPreferredSize(new Dimension(20, 40)); // Width, Height

    panelSearch.add(text);
    panelSearch.add(occurencesLabel);
    panelSearch.add(find);

    panelSearch.setForeground(Util.FOREGROUND_COLOR);
    panelSearch.setBackground(Util.BACKGROUD_COLOR);

    ActionListener searchAction =
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            performSearch();
          }
        };
    find.addActionListener(searchAction);
    text.addActionListener(searchAction);

    textArea.addKeyListener(
        new KeyListener() {

          public void keyPressed(KeyEvent e) {

            if (e.getKeyChar() == KeyEvent.VK_ENTER) {
              performSearch();
            }
          }

          @Override
          public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub

          }

          @Override
          public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub

          }
        });

    return panelSearch;
  }

  private void performSearch() {

    text.revalidate();
    // Get the text to find...convert it to lower case for eaiser comparision
    String find = text.getText().toLowerCase();
    // Focus the text area, otherwise the highlighting won't show up
    textArea.requestFocusInWindow();

    // Make sure we have a valid search term
    if (find != null && find.length() > 0) {

      Document document = textArea.getDocument();
      int occurences = countMatches(textArea.getDocument(), find);
      sertOccurences(occurences);
      int findLength = find.length();
      try {
        boolean found = false;
        // Rest the search position if we're at the end of the document
        if (pos + findLength > document.getLength()) {
          pos = 0;
        }
        // While we haven't reached the end...
        // "<=" Correction
        while (pos + findLength <= document.getLength()) {
          // Extract the text from teh docuemnt
          String match = document.getText(pos, findLength).toLowerCase();
          // Check to see if it matches or request
          if (match.equals(find)) {
            found = true;
            break;
          }
          pos++;
        }

        // Did we find something...
        if (found) {
          // Get the rectangle of the where the text would be visible...
          Rectangle viewRect = textArea.modelToView(pos);
          // Scroll to make the rectangle visible
          textArea.scrollRectToVisible(viewRect);
          // Highlight the text
          textArea.setCaretPosition(pos + findLength);
          textArea.moveCaretPosition(pos);
          // Move the search position beyond the current match
          pos += findLength;
        } else {
          textArea.scrollRectToVisible(new Rectangle(0, 0, 0, 0));
          textArea.setCaretPosition(0);
          textArea.moveCaretPosition(0);
          // set focus on text field
          text.requestFocusInWindow();
        }
        /*SwingUtilities.invokeLater(
        new Runnable() {

          public void run() {
            text.requestFocus();
          }
        });*/
      } catch (Exception exp) {
        exp.printStackTrace();
      }
    } else {
      sertOccurences(0);
    }
  }

  private void sertOccurences(int num) {
    occurencesLabel.setText("    " + num + "  ");
  }

  private int countMatches(Document document, String find) {

    int lastIndex = 0;
    int count = 0;

    try {
      String doc = document.getText(0, textArea.getDocument().getLength());
      while (lastIndex != -1) {
        lastIndex = doc.indexOf(find, lastIndex);
        if (lastIndex != -1) {
          count++;
          lastIndex += find.length();
        }
      }
    } catch (BadLocationException e) { // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return count;
  }

  private void addUndo() {
    undoManager = new UndoManager();

    Document doc = textArea.getDocument();
    doc.addUndoableEditListener(
        new UndoableEditListener() {
          @Override
          public void undoableEditHappened(UndoableEditEvent e) {
            undoManager.addEdit(e.getEdit());
          }
        });

    InputMap im = textArea.getInputMap(JComponent.WHEN_FOCUSED);
    ActionMap am = textArea.getActionMap();

    im.put(
        KeyStroke.getKeyStroke(
            KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
        "Undo");
    im.put(
        KeyStroke.getKeyStroke(
            KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
        "Redo");

    am.put(
        "Undo",
        new AbstractAction() {
          private static final long serialVersionUID = 1L;

          @Override
          public void actionPerformed(ActionEvent e) {
            try {
              if (undoManager.canUndo()) {
                undoManager.undo();
              }
            } catch (CannotUndoException exp) {
              exp.printStackTrace();
            }
          }
        });
    am.put(
        "Redo",
        new AbstractAction() {
          private static final long serialVersionUID = 1L;

          @Override
          public void actionPerformed(ActionEvent e) {
            try {
              if (undoManager.canRedo()) {
                undoManager.redo();
              }
            } catch (CannotUndoException exp) {
              exp.printStackTrace();
            }
          }
        });
  }

  public void update(String file) {
    sertOccurences(0);
    titledBorder = BorderFactory.createTitledBorder(file);
    titledBorder.setTitleColor(Util.getContrastColor(Util.BACKGROUD_COLOR));
    textArea.setBorder(titledBorder);
    pos = 0;
    if (file == null || file.trim().equals("")) {
      textArea.setText("");
    }
    readFile(file);
    jscrollPane.revalidate();
    jscrollPane.repaint();
    addUndo();
    JFrame f1 = (JFrame) SwingUtilities.windowForComponent(jscrollPane);
    f1.setTitle(file);
  }

  private void readFile(String file) {
    try {
      // Read some text from the resource file to display in
      // the JTextArea.
      File initialFile = new File(file);
      initialFile.createNewFile();
      Reader targetReader = new FileReader(initialFile);
      textArea.read(targetReader, "");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public JPanel getComponent() {
    return container;
  }

  public String getText() {
    return this.textArea.getText();
  }

  public void applyTheme() {

    textArea.setCaretColor(Util.FOREGROUND_COLOR);

    textArea.setForeground(Util.FOREGROUND_COLOR);
    textArea.setBackground(Util.BACKGROUD_COLOR);
    jpanel.setBackground(Util.BACKGROUD_COLOR);
    container.setBackground(Util.BACKGROUD_COLOR);
    jscrollPane.setBackground(Util.BACKGROUD_COLOR);
    text.setForeground(Util.FOREGROUND_COLOR);
    text.setBackground(Util.BACKGROUD_COLOR);
    occurencesLabel.setForeground(Util.FOREGROUND_COLOR);
    occurencesLabel.setBackground(Util.BACKGROUD_COLOR);
    panelSearch.setForeground(Util.FOREGROUND_COLOR);
    panelSearch.setBackground(Util.BACKGROUD_COLOR);
    find.applyTheme();
    container.revalidate();
    container.repaint();
  }

  public void changeFontTitleBorder(int fontSize) {
    Font f = textArea.getFont();
    titledBorder.setTitleFont(new Font(f.getName(), f.getStyle(), /*f.getSize() +*/ fontSize));
  }
}
