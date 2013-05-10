import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Peter
 * Date: 17/12/12
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */

public class MagnifierPanel extends JPanel {


    private Image screenImage;
    private BufferedImage img;

    private int magnifierSize;
    private int position_X;
    private int position_Y;

    private Area rectangleFrame;
    public Area rectanglePanel;

    private Color frameColor;
    private Color panelColor;

    private int screenWidth  = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

    /* class for taking picture of the screen */
    private Robot robot;

    public MagnifierPanel(int magnifierSize){

        try{
            robot = new Robot();
        } catch (AWTException e){}

        getScreen();
    }

    public void setMagnifierPosition(int position_X, int position_Y){
        this.position_X = position_X;
        this.position_Y = position_Y;
        updateMagnifierPicture();
    }

    public void setImage(Image screenImage){
        this.screenImage = screenImage;
        updateMagnifierPicture();
    }

    /* take picture of the current screen setup */
    public void getScreen() {
        screenImage = robot.createScreenCapture(new Rectangle(0,
                                                              0,
                                                              screenWidth,
                                                              screenHeight)
        );

        // MULTIPLE MONITORS - FAIL
//        //get handle to graphics environment
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        GraphicsDevice[] screens = ge.getScreenDevices();
//
//        //if screens are detected proceed
//        if (screens.length > 0){
//                //get the first screen
//                GraphicsDevice a = screens[0];
//                //get the monitor resolution
//                Toolkit t = Toolkit.getDefaultToolkit();
//                Rectangle allScreenBounds = new Rectangle(t.getScreenSize());
//                //offset screen capture (in cases where first monitor is not the
//                //highest this will come into effect)
//                allScreenBounds.y = -1 * screens[0].getDefaultConfiguration().getBounds().y;
//                //take screenshot
//                img = robot.createScreenCapture(allScreenBounds);
//        }
    }


    /* set the size and color of the magnifier tool */
    public void setMagnifierSize(int magnifierSize){
        this.magnifierSize = magnifierSize;

        rectangleFrame = new Area(new Rectangle(0, 0, magnifierSize, magnifierSize));
        rectanglePanel = new Area(new RoundRectangle2D.Double(10, 10, magnifierSize - 20, magnifierSize - 20, 30, 30));

        rectangleFrame.subtract(rectanglePanel);

        frameColor = new Color(8, 0, 151, 178);
        panelColor = new Color(0, 248, 251, 46);

        /* set default size for this component */
        setPreferredSize(new Dimension(magnifierSize,magnifierSize));

        if (getParent() != null){
            getParent().repaint();
        }

        updateMagnifierPicture();
    }

    public void updateMagnifierPicture(){
        if (getParent() != null){
            getParent().repaint();
        } else {
            repaint();
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent((Graphics2D) g);
        drawMagnifier((Graphics2D) g);
    }

    public void drawMagnifier(Graphics2D g){
        g.setClip(rectangleFrame);
        g.drawImage(screenImage,
                0,
                0,
                magnifierSize,
                magnifierSize,
                position_X,
                position_Y,
                position_X + magnifierSize,
                position_Y + magnifierSize,
                this);
        g.setColor(frameColor);
        g.fill(rectangleFrame);

        // clip the inner rectangle, so the zoom is applied only to it
        g.setClip(rectanglePanel);
        g.drawImage(screenImage,
                0,
                0,
                magnifierSize,
                magnifierSize,
                position_X + (magnifierSize / 4),
                position_Y + (magnifierSize / 4),
                position_X + (magnifierSize / 4 * 3),    // set the magnifier zoom
                position_Y + (magnifierSize / 4 * 3),    // set the magnifier zoom
                this);
        g.setColor(panelColor);
        g.fill(rectanglePanel);
    }
}

/* TODO: auto-refresh picture
 * TODO: shortcuts
 * */
