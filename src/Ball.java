import java.awt.geom.Ellipse2D;

public class Ball implements IBall{

    private double x;
    private double y;
    private double vx;
    private double vy;
    private double r;

    public Ball (double x, double y, double vx, double vy, double r) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.r = r;
    }

    //----Getters and Setters----
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getR() {
        return r;
    }

    //Placeholder mass based on ball radius.
    public double getMass(){return r*2;}

    //Returns ball information for animation purposes.
    public Ellipse2D getBall(){return new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);}
}

