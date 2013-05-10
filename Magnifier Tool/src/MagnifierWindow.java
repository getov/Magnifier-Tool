import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Peter
 * Date: 17/12/12
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 */
public class MagnifierWindow extends JFrame {

    private Container container = getContentPane();

    /* menu items */
    private JPopupMenu popupMenu  = new JPopupMenu();
    private JMenuItem menuRefresh = new JMenuItem("Refresh");
    private JMenuItem menuHelp    = new JMenuItem("Help");
    private JMenuItem menuExit    = new JMenuItem("Exit");


    private int setX;
    private int setY;
    private int absoluteX;
    private int absoluteY;
    /* on mouse press */
    private int relativeX;
    private int relativeY;

    private boolean mousePressed;

//    private int screenWidth   = Toolkit.getDefaultToolkit().getScreenSize().width;
//    private int screenHeight  = Toolkit.getDefaultToolkit().getScreenSize().height;
    private int magnifierSize = 300;

    private MagnifierPanel magnifierPanel = new MagnifierPanel(magnifierSize);

    private int updateScreenDelay = 500;

    /* class to register mouse press */
    private class MouseClicks extends MouseAdapter {
        public void mousePressed(MouseEvent e){
            if (e.getButton() == MouseEvent.BUTTON1){
                mousePressed = true;
                relativeX = e.getX();
                relativeY = e.getY();
            }
            /* check in mousePressed for cross-platform functionality */
            if (e.isPopupTrigger()){
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
        public void mouseReleased(MouseEvent e){
            mousePressed = false;
            if (e.isPopupTrigger()){
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /* class to register mouse motion */
    private class MouseMotion extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e){
            if (mousePressed == true){
                absoluteX = MagnifierWindow.this.getLocationOnScreen().x + e.getX();
                absoluteY = MagnifierWindow.this.getLocationOnScreen().y + e.getY();
                setX      = absoluteX - relativeX;
                setY      = absoluteY - relativeY;
                magnifierPanel.setMagnifierPosition(setX, setY);
                /* move the magnifier to mouse location */
                setLocation(setX, setY);
            }
        }
    }

    public MagnifierWindow(){
        super();

        /* hide the JFrame */
        setUndecorated(true);

        container.add(magnifierPanel);

        addMouseListener(new MouseClicks());
        addMouseMotionListener(new MouseMotion());

        menuRefresh.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateScreen();
                    }
                }
        );

        menuExit.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );
        popupMenu.add(menuRefresh);
        popupMenu.add(menuExit);

        updateSize(magnifierSize);
        setVisible(true);
    }

    public void updateSize(int magnifierSize){
        magnifierPanel.setMagnifierSize(magnifierSize);
        setSize(magnifierSize, magnifierSize);
        validate();
    }

    public void updateScreen(){
        setVisible(false);
        try{
            Thread.sleep(updateScreenDelay);
        } catch (InterruptedException e){

        }
        magnifierPanel.getScreen();
        setVisible(true);
    }
}
