package moe.leer.plugins.codeflow;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Scaleable and scrollable image panel
 *
 * @author leer
 * Created at 12/30/19 4:16 PM
 */
public class ScrollScaleImagePanel extends JPanel {

//  private static final Logger LOG = Logger.getInstance(ScrollScaleImagePanel.class);

  private double scaleMin = 0.5;
  private double scaleMax = 2.0;

  private BufferedImage image;
  private JScrollPane scrollPane;
  private JPanel canvas;
  private JSlider slider;
  int width, height;
  //  int originWidth, originHeight;
  double scale = 1.0;

  public ScrollScaleImagePanel() {
    canvas = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // zoom in and zoom out
        if (image != null) {
          g.drawImage(image, 0, 0, width, height, null);
        }
      }
    };
    canvas.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
          // todo use bufferedimage to save file
          RightClickMenu.showPopupMenu(canvas, e);
        }
      }
    });
    this.setLayout(new BorderLayout());

    slider = createImageSlider();
    this.add(slider, BorderLayout.SOUTH);

    scrollPane = new JScrollPane(canvas);
    this.add(scrollPane, BorderLayout.CENTER);
    scrollPane.setWheelScrollingEnabled(true);
  }

  public ScrollScaleImagePanel(BufferedImage image) {
    this();
    this.image = image;
    this.width = image.getWidth();
    this.height = image.getHeight();
    canvas.setPreferredSize(new Dimension(width, height));
  }

  public void setImage(BufferedImage image) {
    this.image = image;
    this.width = image.getWidth();
    this.height = image.getHeight();
    canvas.repaint();
    scrollPane.revalidate();
    slider.repaint();
  }

  @NotNull
  private JSlider createImageSlider() {
    JSlider scaleSlider;
    scaleSlider = new JSlider(JSlider.HORIZONTAL, (int) ((scaleMin - 1) * 1000), (int) ((scaleMax - 1) * 1000), 0);
    scaleSlider.addChangeListener(e -> {
      int v = ((JSlider) e.getSource()).getValue();
      scale = v / 1000.0 + 1.0;
      width = (int) (image.getWidth() * scale);
      height = (int) (image.getHeight() * scale);
      canvas.setPreferredSize(new Dimension(width, height));
      canvas.repaint();
      // refresh scroll bar
      scrollPane.revalidate();
//      LOG.debug("image scale: " + scale);
      System.out.println(image.getWidth() * scale + ", " + image.getHeight() * scale);
    });
    return scaleSlider;
  }

}
