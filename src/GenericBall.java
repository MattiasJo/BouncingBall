import java.awt.geom.Ellipse2D;

public class GenericBall implements IBouncingBallsModel {

    private final double areaWidth;
    private final double areaHeight;
    private final double gravity = 9.82/8;
    public String test;

    private double x, y, vx, vy, r, mass;

    public GenericBall(double width, double height, double x, double y, double r, String test) {
        this.areaWidth = width;
        this.areaHeight = height;
        this.x = x;
        this.y = y;
        vx = 10;
        vy = 1  ;
        this.test = test;
        this.r = r;
    }

    @Override
    public void tick(double deltaT) {
        vy -= gravity;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void takeXStep(double step) {
        x = step;
    }

    public void takeYStep(double step) {
        y = step;
    }

    public double getMass() {
        return r*2;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public void setVY(double newVY) {vy = newVY;}

    public void setVX(double newVX) {
        vx = newVX;
    }

    public double getVY() {
        return vy;
    }

    public double getVX() {
        return vx;
    }


    @Override
    public Ellipse2D getBallG() {
        return new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
    }
}