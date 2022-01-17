package app;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Item {

  private JLabel label = new JLabel();
  private MyJButton saveButton = new MyJButton("↓");
  private MyJButton closeButton = new MyJButton("X");
  private JPanel jpanel = new JPanel();
  private String file;
  private Item thisItem;

  private static LeftPanel leftPanel = null;

  public Item(String file) {
    this.thisItem = this;
    this.file = file;
    try {
      File f = new File(file);
      file = f.getName();
    } catch (Throwable t) {
      /*nothing to do*/
    }
    this.label.setText(file);
    label.setForeground(Util.FOREGROUND_COLOR);

    BoxLayout boxlayout = new BoxLayout(jpanel, BoxLayout.X_AXIS);

    jpanel.setLayout(boxlayout);

    label.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getMinimumSize().height));
    label.setBorder(new EmptyBorder(2, 2, 2, 2));
    jpanel.setBorder(BorderFactory.createLineBorder(Util.FOREGROUND_COLOR.darker().darker(), 1));
    jpanel.add(label);
    jpanel.add(saveButton);

    MouseAdapter closeAdapter =
        new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            if (!closeButton.isEnabled()) {
              return;
            }
            int result =
                JOptionPane.showConfirmDialog(
                    jpanel,
                    "Are you sure you want to remove it?",
                    "Remove item",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
              leftPanel.removeItem(thisItem);
            }
          }
        };

    MouseAdapter saveAdapter =
        new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            if (!saveButton.isEnabled()) {
              return;
            }
            int result =
                JOptionPane.showConfirmDialog(
                    jpanel,
                    "Are you sure you want to Save it?",
                    "Save item",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
              Util.saveFile(leftPanel.getCurrentText(), getFile());
            }
          }
        };

    closeButton.addMouseListener(closeAdapter);

    saveButton.addMouseListener(saveAdapter);

    closeButton.setEnabled(true);
    saveButton.setEnabled(true);

    jpanel.add(closeButton);
    jpanel.setName("ITEM" + UUID.randomUUID());
    jpanel.setBackground(Util.BACKGROUD_COLOR);
  }

  public JPanel getPanel() {
    return jpanel;
  }

  public boolean contains(Point p) {
    //    Rectangle r = label.getBounds();
    boolean result = label.getBounds().contains(p);
    return result;
  }

  public void setSelected(boolean selected) {
    if (selected == true) {
      /*jpanel.setBackground(Util.FOREGROUND_COLOR);
      label.setForeground(Util.BACKGROUD_COLOR);*/
      // jpanel.setBorder(BorderFactory.createLineBorder(Util.BACKGROUD_COLOR, 3));
      jpanel.setBorder(BorderFactory.createLineBorder(Util.FOREGROUND_COLOR, 3));
      closeButton.setEnabled(true);
      saveButton.setEnabled(true);

    } else {
      /*jpanel.setBackground(Util.BACKGROUD_COLOR);
      label.setForeground(Util.FOREGROUND_COLOR);*/
      jpanel.setBorder(BorderFactory.createLineBorder(Util.FOREGROUND_COLOR.darker().darker(), 3));
      closeButton.setEnabled(false);
      saveButton.setEnabled(false);
    }
  }

  public String getName() {
    return jpanel.getName();
  }

  public String getFile() {
    return file;
  }

  public static void setLeftPanel(LeftPanel lp) {
    leftPanel = lp;
  }

  public void applyTheme() {
    label.setForeground(Util.FOREGROUND_COLOR);

    jpanel.setBorder(BorderFactory.createLineBorder(Util.FOREGROUND_COLOR.darker().darker(), 1));
    jpanel.setBackground(Util.BACKGROUD_COLOR);
    closeButton.applyTheme();
    saveButton.applyTheme();
    setSelected(saveButton.isEnabled());
  }
}
