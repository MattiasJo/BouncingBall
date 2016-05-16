import java.awt.geom.Ellipse2D;

public interface IBall {

    public double getX();
    public double getY();
    public void setX(double x);
    public void setY(double y);
    public double getVx();
    public double getVy();
    public void setVx(double vx);
    public void setVy(double vy);
    public double getR();
    public double getMass();
    public Ellipse2D getBall();
}