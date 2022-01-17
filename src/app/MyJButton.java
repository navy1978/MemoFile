package app;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JButton;

public class MyJButton extends JButton {

  /** */
  private static final long serialVersionUID = 1L;

  boolean up = true;

  public MyJButton() {
    super();
    applyStyle();
  }

  public MyJButton(Icon icon) {
    super(null, icon);
    applyStyle();
  }

  public MyJButton(String text) {
    super(text, null);
    applyStyle();
  }

  private void applyStyle() {
    // setBorderPainted(false);
    setContentAreaFilled(false);
    // setBorder(BorderFactory.createLineBorder(Color.WHITE));
    addMouseListener(
        new MouseListener() {

          @Override
          public void mouseReleased(MouseEvent e) {
            up = true;
          }

          @Override
          public void mousePressed(MouseEvent e) {
            up = false;
          }

          @Override
          public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub

          }

          @Override
          public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub

          }

          @Override
          public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub

          }
        });
  }

  @Override
  protected void paintComponent(Graphics g) {

    Graphics2D g2 = (Graphics2D) g.create();
    if (isEnabled()) {
      g2.setColor(getColorClick().brighter());
    } else {
      g2.setColor(getColorClick().darker());
    }
    // g2.fillRect(0, 0, getWidth(), getHeight());
    /* g2.setPaint(
        new GradientPaint(
            new Point(0, 0), getBackground(), new Point(0, getHeight() / 3), Color.GRAY));
    g2.fillRect(3, 3, getWidth() - 6, getHeight() / 3 - 6);
    g2.setPaint(
        new GradientPaint(
            new Point(0, getHeight() / 3), Color.GRAY, new Point(0, getHeight()), getBackground()));
    g2.fillRect(3, getHeight() / 3 - 3, getWidth() - 6, getHeight() - 6);
    */
    g2.fillRect(2, 2, getWidth() - 4, getHeight() / 3 - 4);
    g2.setColor(getColorClick());
    g2.fillRect(2, 2, getWidth() - 4, getHeight() - 4);
    g2.dispose();

    super.paintComponent(g);
  }

  @Override
  public void setEnabled(boolean b) {
    this.setForeground(
        b ? Util.FOREGROUND_COLOR.brighter().brighter() : Util.FOREGROUND_COLOR.darker().darker());
    super.setEnabled(b);
  }

  private Color getColorClick() {
    if (up) {
      return Util.FOREGROUND_COLOR.darker(); // Color.GRAY;
    } else {
      return Util.FOREGROUND_COLOR.brighter();
    }
  }

  public void applyTheme() {
    if (Util.BACKGROUD_COLOR.equals(Color.WHITE)) {
      System.out.println("true!");
    }
    setBackground(Util.BACKGROUD_COLOR);
    setForeground(Util.FOREGROUND_COLOR);
  }
}
