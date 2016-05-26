import java.awt.geom.Ellipse2D;

public interface IBall {
    double getX();
    double getY();
    void setX(double x);
    void setY(double y);
    double getVx();
    double getVy();
    void setVx(double vx);
    void setVy(double vy);
    double getR();
    double getMass();
    Ellipse2D getBall();
}