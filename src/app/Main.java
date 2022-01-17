package app;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

class Main extends JFrame {

  private static final long serialVersionUID = 1L;

  static JFrame frame;
  static TextEditor textEditor;
  static LeftPanel lelftPanel = null;

  static JMenuBar menuBar = new JMenuBar();

  public static void main(String[] args) {
    EventQueue.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            try {

              UIManager.setLookAndFeel(
                  "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Throwable t) {
              t.printStackTrace();
            }

            new Main();
          }
        });
  }

  public Main() {

    frame = new JFrame("Memo");
    textEditor = new TextEditor();
    lelftPanel = new LeftPanel(textEditor);

    lelftPanel
        .getJscrollPane()
        .setMinimumSize(new Dimension(Util.MIN_LEFT_PANEL_HEIGHT, Util.HEIGHT));

    JSplitPane sl =
        new JSplitPane(
            SwingConstants.VERTICAL, lelftPanel.getJscrollPane(), textEditor.getComponent());
    frame.add(sl);

    frame.setSize(Util.WIDTH, Util.HEIGHT);
    frame.setVisible(true);
    createMenuBar();
    // changeFont(frame, 18);
    frame.pack();

    /*KeyboardFocusManager.getCurrentKeyboardFocusManager()
    .addKeyEventDispatcher(
        new KeyEventDispatcher() {
          @Override
          public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.isControlDown() && e.isAltDown()) {
              if (e.getKeyCode() >= 48 && e.getKeyCode() <= 57) {
                int number_pressed = e.getKeyCode() - 48;
                lelftPanel.selecteItemByNumber(number_pressed);
              }
            }
            return false;
          }
        });*/

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  // default is 12
  public static void changeFont(Component component, int fontSize) {
    Font f = component.getFont();
    component.setFont(new Font(f.getName(), f.getStyle(), /*f.getSize() +*/ fontSize));
    if (component instanceof Container) {
      for (Component child : ((Container) component).getComponents()) {
        /* if (child instanceof JPanel) {
          JPanel jPanel1 = (JPanel) child;
          Border gen_border = jPanel1.getBorder();
          if (gen_border instanceof javax.swing.border.TitledBorder) {
            javax.swing.border.TitledBorder border = (javax.swing.border.TitledBorder) gen_border;
            if (border != null) {
              border.setTitleFont(new Font(f.getName(), f.getStyle(), fontSize));
            }
          }
        }*/
        changeFont(child, fontSize);
      }
    }
  }

  private void createMenuBar() {

    var menu = new JMenu("Menu");

    var themeMenuItem = new JMenu("Theme");

    ButtonGroup group = new ButtonGroup();
    createThemeSubMenu(themeMenuItem, group, Util.ORANGE_WHITE, true);
    createThemeSubMenu(themeMenuItem, group, Util.BLACK_WHITE, false);
    createThemeSubMenu(themeMenuItem, group, Util.DARKGRAY_SOFTWHITE, false);
    createThemeSubMenu(themeMenuItem, group, Util.GRAY_YELLOW, false);
    createThemeSubMenu(themeMenuItem, group, Util.BLUE_MINT, false);
    createThemeSubMenu(themeMenuItem, group, Util.GREEN_GREENN, false);
    createThemeSubMenu(themeMenuItem, group, Util.BLUE_WHITE, false);

    menu.add(themeMenuItem);

    var fontMenuItem = new JMenu("Font");

    ButtonGroup group2 = new ButtonGroup();
    createFontSubMenu(fontMenuItem, group2, Util.FONT_12, true);
    createFontSubMenu(fontMenuItem, group2, Util.FONT_14, true);
    createFontSubMenu(fontMenuItem, group2, Util.FONT_16, true);

    menu.add(fontMenuItem);

    menuBar.setOpaque(false);
    menuBar.setBackground(Util.BACKGROUD_COLOR);
    menuBar.setForeground(Util.FOREGROUND_COLOR);
    /* var themeMenuTime = new JMenuItem("Theme");

    newMenuItem.add(themeMenuTime);*/

    var exitMenuItem = new JMenuItem("Exit");
    exitMenuItem.setToolTipText("Exit application");
    exitMenuItem.addActionListener((event) -> System.exit(0));
    menu.add(exitMenuItem);

    menuBar.add(menu);

    frame.setJMenuBar(menuBar);
  }

  private void createThemeSubMenu(JMenu menu, ButtonGroup group, String theme, boolean selected) {
    JRadioButtonMenuItem radioMenuItem = new JRadioButtonMenuItem(theme, selected);
    // radioMenuItem.setMnemonic(KeyEvent.VK_I);
    menu.add(radioMenuItem);
    group.add(radioMenuItem);

    radioMenuItem.addActionListener(
        (event) -> {
          Util.setTheme(theme);
          UIManager.put("SplitPaneDivider.draggingColor", Util.FOREGROUND_COLOR);
          // UIManager.put("MenuBar.background", Util.BACKGROUD_COLOR);
          menuBar.setBackground(Util.BACKGROUD_COLOR);
          menuBar.setForeground(Util.FOREGROUND_COLOR);

          lelftPanel.applyTheme();
          textEditor.applyTheme();
          frame.revalidate();
          frame.repaint();
        });
  }

  private void createFontSubMenu(JMenu menu, ButtonGroup group, int fontSize, boolean selected) {
    JRadioButtonMenuItem radioMenuItem = new JRadioButtonMenuItem("" + fontSize, selected);
    // radioMenuItem.setMnemonic(KeyEvent.VK_I);
    menu.add(radioMenuItem);
    group.add(radioMenuItem);

    radioMenuItem.addActionListener(
        (event) -> {
          textEditor.changeFontTitleBorder(fontSize);
          Main.changeFont(frame, fontSize);
          frame.revalidate();
          frame.repaint();
        });
  }
}
