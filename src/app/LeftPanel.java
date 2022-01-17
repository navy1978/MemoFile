package app;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileSystemView;

public class LeftPanel {
  private JPanel outerContainer = new JPanel();
  private JPanel itemPanelContainer;
  private JScrollPane jscrollPane;
  private List<Item> items = new ArrayList<>();
  private JPanel itemsPanel;
  private TextEditor textEditor;
  MyJButton addButton = new MyJButton("Add");
  // private Color backgroudnColor = Color.DARK_GRAY; // new Color(240, 200, 100);

  public LeftPanel(TextEditor textEditor) {
    Item.setLeftPanel(this);
    this.textEditor = textEditor;
    itemPanelContainer = new JPanel(new BorderLayout());
    itemPanelContainer.setBackground(Util.BACKGROUD_COLOR);
    itemsPanel = new JPanel(new GridBagLayout());
    itemsPanel.setBackground(Util.BACKGROUD_COLOR);
    itemPanelContainer.add(itemsPanel, BorderLayout.NORTH);

    // outerContainer.setLayout(new BoxLayout(outerContainer, BoxLayout.Y_AXIS));
    int numberOfColumns = 10;
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();

    outerContainer.setLayout(gridbag);
    outerContainer.setBackground(Util.BACKGROUD_COLOR);
    c.weightx = 1;
    c.weighty = 1;
    // c.fill = GridBagConstraints.HORIZONTAL;
    // c.ipady = 1;
    c.gridx = 0;
    c.gridy = 0;
    // c.gridwidth = 1;
    // c.gridheight = 1;
    c.anchor = GridBagConstraints.FIRST_LINE_START;

    addButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            JFileChooser j = new JFileChooser("c:\\", FileSystemView.getFileSystemView());

            // invoke the showsOpenDialog function to show the save dialog
            int r = j.showOpenDialog(null);

            // if the user selects a file
            if (r == JFileChooser.APPROVE_OPTION) {

              // set the label to the path of the selected file
              System.out.println("we have to add:" + j.getSelectedFile().getAbsolutePath());
              addItem(j.getSelectedFile().getAbsolutePath());
            }
            // if the user cancelled the operation
            else System.out.println("the user cancelled the operation");
          }
        });
    // test.setPreferredSize(new Dimension(50, test.getPreferredSize().height));

    outerContainer.add(addButton, c);

    c.fill = GridBagConstraints.HORIZONTAL; // metti both
    c.gridx = 0;
    // c.gridwidth = 1;
    // c.ipady = 103;
    // c.gridheight = 103;
    c.gridy = 1;
    // c.anchor = GridBagConstraints.FIRST_LINE_START;
    itemPanelContainer.setPreferredSize(new Dimension(Util.MIN_LEFT_PANEL_HEIGHT, Util.HEIGHT));
    outerContainer.add(itemPanelContainer, c);

    jscrollPane =
        Util.createJScrollPaneVert(outerContainer, new Dimension(Util.WIDTH / 5, Util.HEIGHT));

    List<String> names = Arrays.asList("C:\\dev\\utils\\urls.txt", "C:\\dev\\utils\\email.txt");

    for (String format : names) {
      addItem(format);
    }

    MouseAdapter ma =
        new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {

            for (Component c : itemsPanel.getComponents()) {
              if (c.getName() != null && c.getName().startsWith("ITEM")) {
                Item i = getItemByName(c.getName());
                Rectangle r = i.getPanel().getBounds();
                if (r.contains(new Point(e.getX(), e.getY()))) {
                  selectItem(i, true);
                } else {
                  selectItem(i, false);
                }
              }
            }
          }
        };
    itemPanelContainer.addMouseListener(ma);

    itemPanelContainer.setDropTarget(
        new DropTarget() {
          private static final long serialVersionUID = -6418118605479053389L;

          public synchronized void drop(DropTargetDropEvent evt) {
            try {
              evt.acceptDrop(DnDConstants.ACTION_COPY);
              List droppedFiles =
                  (List) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
              if (droppedFiles.size() > 1) {
                JOptionPane.showMessageDialog(
                    itemPanelContainer, "Sorry...can't handle more than one files together.");
              } else {
                File droppedFile = (File) droppedFiles.get(0);
                addItem(droppedFile.getAbsolutePath());
              }
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        });
  }

  private void selectItem(Item i, boolean selected) {
    i.setSelected(selected);
    if (selected) {
      this.textEditor.update(i.getFile());
    }
  }

  public void addItem(String file) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    Item item = new Item(file);
    itemsPanel.add(item.getPanel(), gbc);
    items.add(item);
    // when we add components dynamically we need to re-validate in order to see the changes
    itemsPanel.revalidate();
    itemsPanel.repaint();
  }

  public JScrollPane getJscrollPane() {
    return jscrollPane;
  }

  public void setJscrollPane(JScrollPane jscrollPane) {
    this.jscrollPane = jscrollPane;
  }

  private Item getItemByName(String name) {
    for (Item i : items) {
      if (i.getName() != null && i.getName().equals(name)) {
        return i;
      }
    }
    return null;
  }

  public void selecteItemByNumber(int i) {
    if (items.size() > 0) {
      selectItem(items.get(i), true);
    }
  }

  public void removeItem(Item item) {

    items.remove(item);
    // when we add components dynamically we need to re-validate in order to see the changes
    itemsPanel.remove(item.getPanel());
    itemsPanel.revalidate();
    itemsPanel.repaint();
  }

  public String getCurrentText() {
    return this.textEditor.getText();
  }

  public void applyTheme() {
    itemPanelContainer.setBackground(Util.BACKGROUD_COLOR);
    itemsPanel.setBackground(Util.BACKGROUD_COLOR);
    outerContainer.setBackground(Util.BACKGROUD_COLOR);
    addButton.applyTheme();
    for (Item it : items) {
      it.applyTheme();
    }
  }
}
