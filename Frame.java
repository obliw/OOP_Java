
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.event.MouseInputListener;


// Ta in storlekar
//
abstract class Geometry {
    int xPos;
    int yPos;
    int width;
    int height;
    int clickedX;
    int clickedY;
    Color c;
    Boolean clicked;


    Geometry(int x, int y, int w, int h, Color c) {
        this.xPos = x;
        this.yPos = y;
        this.width = w;
        this.height = h;
        this.clicked = false;
        this.c = c;


    }

    public Boolean isClicked(int x, int y) {
        if (isInside(x, y)) {
            this.clickedX = x;
            this.clickedY = y;
            this.clicked = true;
        } else {
            this.clicked = false;
        }
        return isInside(x, y);
    }

    public abstract Boolean isInside(int x, int y);

    public abstract void paint(Graphics g);

}

class Rectangle extends Geometry {
    Rectangle(int x, int y, int w, int h, Color c) {
        super(x, y, w, h, c);

    }

    public Boolean isInside(int x, int y) {
        return (xPos <= x && x <= xPos + width) && (yPos <= y && y <= yPos + height);
    }

    public void paint(Graphics g) {
        g.setColor(this.c);
        g.fillRect(this.xPos, this.yPos, this.width, this.height);

        g.setColor(Color.BLACK);
        g.drawRect(this.xPos, this.yPos, this.width, this.height);
    }

}

class Oval extends Geometry {
    public int r;

    Oval(int x, int y, int d, Color c) {
        super(x, y, d, d, c);
        this.r = d / 2;
    }

    public Boolean isInside(int x, int y) {
        return (xPos + r - x) * (xPos + r - x) + (yPos + r - y) * (yPos + r - y) <= r * r;
    }

    public void paint(Graphics g) {
        g.setColor(this.c);
        g.fillOval(this.xPos, this.yPos, this.width, this.height);
        g.setColor(Color.BLACK);
        g.drawOval(this.xPos, this.yPos, this.width, this.height);
    }
}

class Triangle extends Geometry {
    Triangle(int x, int y, int w, int h, Color c) {
        super(x, y, w, h, c);
    }

    public Boolean isInside(int x, int y) {

        int dx = x - xPos - width / 2;
        int dy = y - yPos;
        return -dy <= 2 * dx &&
                2 * dx <= dy &&
                dy <= height;
    }

    public void paint(Graphics g) {
        int[] tXPoints = new int[]{this.xPos + this.width / 2, this.xPos + this.width, this.xPos};
        int[] tYPoints = new int[]{this.yPos, this.yPos + this.height, this.yPos + this.height};

        g.setColor(this.c);
        g.fillPolygon(tXPoints, tYPoints, 3);
        g.setColor(Color.black);
        g.drawPolygon(tXPoints,tYPoints,3);
    }
}

class myPanel extends JPanel implements MouseInputListener {

    LinkedList<Geometry> geometries = new LinkedList<>();
    Geometry currGeometry;


    myPanel() {
        addMouseListener(this);
        addMouseMotionListener(this);

        geometries.add(new Rectangle(200, 200, 100, 100, Color.green));
        geometries.add(new Triangle(50, 50, 80, 80, Color.orange));
        geometries.add(new Oval(150, 100, 100, Color.blue));
        currGeometry = geometries.getFirst();


        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    // Makes sure that if a geometry is pressed, that geometry is moved to the back of the list.
    // Without changing the order of the other geometries.
    public void mousePressed(MouseEvent e) {
        Iterator<Geometry> iterator = geometries.descendingIterator();

        while (iterator.hasNext()) {
            Geometry geometry = iterator.next();
            if (geometry.isClicked(e.getX(), e.getY())) {
                currGeometry = geometry;
                System.out.println("Clicked " + geometry);
                geometries.remove(geometry);
                geometries.addLast(geometry);
                repaint();
                break;
            }
        }


    }

    // Occurs when mouse is dragged but only calls isDragged() if the currGeometry is clicked.
    public void mouseDragged(MouseEvent e) {
        if (currGeometry.clicked) {
            isDragged(e.getX(), e.getY(), currGeometry);

        }
    }

    public void mouseReleased(MouseEvent e) {


    }
    public void mouseMoved(MouseEvent e) {

    }
    public void mouseClicked(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }

    // Repaints the smallest possible area around the passed in geometry
    public void isDragged(int x, int y, Geometry geometry) {

        int OFFSET = 1;

        int newX = x - geometry.clickedX;
        int newY = y - geometry.clickedY;

        repaint(geometry.xPos, geometry.yPos, geometry.width + OFFSET, geometry.height + OFFSET);

        geometry.xPos += newX; //+1
        geometry.yPos += newY; //+1
        repaint(geometry.xPos, geometry.yPos, geometry.width + OFFSET, geometry.height + OFFSET);
        geometry.clickedX = x;
        geometry.clickedY = y;


    }

    // Calls each individual paint() function in the list of geometries.
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Geometry geo : geometries) {
            geo.paint(g);

        }

    }




}


public class Frame {
    JFrame f;
    myPanel p;

    public Frame() {
        this.f = new JFrame("Play with geometry");
        this.p = new myPanel();
    }

    // Starts the GUI
    public void createGui() {
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
        f.setBounds(400, 300, 1000, 600);
        p.setBackground(Color.darkGray);
        f.add(p);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        Frame fe = new Frame();
        fe.createGui();

    }
}

