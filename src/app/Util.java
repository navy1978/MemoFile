package app;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class Util {

  public static final int WIDTH = 1280;
  public static final int HEIGHT = 768;

  public static final int MIN_LEFT_PANEL_HEIGHT = 200;
  public static Color BACKGROUD_COLOR = new Color(16, 24, 32);
  public static Color FOREGROUND_COLOR = new Color(242, 170, 76);

  public static final String ORANGE_WHITE = "Orange & Black";
  public static final String BLACK_WHITE = "Black & White";
  public static final String DARKGRAY_SOFTWHITE = "Dark Gray & Soft White";
  public static final String GRAY_YELLOW = "Gray & Yellow";
  public static final String BLUE_MINT = "Blue & Mint";
  public static final String GREEN_GREENN = "Green On Green";
  public static final String BLUE_WHITE = "Blue & White";

  public static final int FONT_12 = 12;
  public static final int FONT_14 = 14;
  public static final int FONT_16 = 16;

  public static JScrollPane createJScrollPaneBoth(JPanel jpanel, Dimension dimension) {
    return createJScrollPane(
        jpanel,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        dimension);
  }

  public static JScrollPane createJScrollPaneVert(JPanel jpanel, Dimension dimension) {

    return createJScrollPane(
        jpanel,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        dimension);
  }

  public static JScrollPane createJScrollPaneHori(JPanel jpanel, Dimension dimension) {
    return createJScrollPane(
        jpanel,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.VERTICAL_SCROLLBAR_NEVER,
        dimension);
  }

  public static JScrollPane createJScrollPane(
      JPanel jpanel, int policyH, int policyV, Dimension dimension) {
    JScrollPane scrollPane = new JScrollPane(jpanel);
    scrollPane.getViewport().setPreferredSize(dimension);
    scrollPane.setHorizontalScrollBarPolicy(policyH);
    scrollPane.setVerticalScrollBarPolicy(policyV);
    scrollPane.getVerticalScrollBar().setBackground(new Color(192, 192, 192));
    scrollPane.getHorizontalScrollBar().setBackground(new Color(192, 192, 192));
    scrollPane
        .getVerticalScrollBar()
        .setUI(
            new BasicScrollBarUI() {
              @Override
              protected void configureScrollBarColors() {
                this.thumbColor = Color.GRAY;
              }
            });
    scrollPane.setBackground(Color.DARK_GRAY);
    // scrollPane.getVerticalScrollBar().setUnitIncrement(2);
    MouseWheelController mwc = new MouseWheelController(scrollPane, 1);
    mwc.setScrollAmount(50);
    return scrollPane;
  }

  public static Color getContrastColor(Color color) {
    double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
    return y >= 128 ? Color.black : Color.white;
  }

  public static void saveFile(String text, String file) {

    try {
      File output = new File(file);
      FileWriter writer = new FileWriter(output);

      writer.write(text);
      writer.flush();
      writer.close();
    } catch (IOException e) { // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void setTheme(String themeName) {

    if (themeName.equals(ORANGE_WHITE)) {
      System.out.println("1");
      BACKGROUD_COLOR = new Color(16, 24, 32);
      FOREGROUND_COLOR = new Color(242, 170, 76);
    } else if (themeName.equals(BLACK_WHITE)) {
      System.out.println("2");
      BACKGROUD_COLOR = Color.BLACK;
      FOREGROUND_COLOR = Color.WHITE;
    } else if (themeName.equals(DARKGRAY_SOFTWHITE)) {
      System.out.println("3");
      BACKGROUD_COLOR = Color.DARK_GRAY;
      FOREGROUND_COLOR = new Color(244, 244, 244);
    } else if (themeName.equals(GRAY_YELLOW)) {
      System.out.println("4");
      BACKGROUD_COLOR = new Color(148, 147, 152);
      FOREGROUND_COLOR = new Color(244, 223, 78);
    } else if (themeName.equals(BLUE_MINT)) {
      System.out.println("5");
      BACKGROUD_COLOR = new Color(0, 32, 63);
      FOREGROUND_COLOR = new Color(173, 239, 209);
    } else if (themeName.equals(GREEN_GREENN)) {
      BACKGROUD_COLOR = new Color(44, 95, 45);
      FOREGROUND_COLOR = new Color(151, 188, 98);
    } else if (themeName.equals(BLUE_WHITE)) {
      BACKGROUD_COLOR = new Color(137, 171, 227);
      FOREGROUND_COLOR = Color.white;
    } else {
      System.out.println("NOT FOUND!!!!");
    }
  }
}
