import java.awt.geom.Ellipse2D;

/**
 * Created by Mattias on 2016-04-25.
 */
public class BigBall implements IBouncingBallsModel {

    private final double areaWidth;
    private final double areaHeight;
    private final double gravity = 9.82/8;

    private double x, y, vx, vy, r, mass;

    public BigBall(double width, double height) {
        this.areaWidth = width;
        this.areaHeight = height;
        x = 8;
        y = 9;
        vx = 4;
        vy = -gravity;
        r = 1.2;
        mass = 8;
    }

    @Override
    public void tick(double deltaT) {
        if( y < (r - Math.abs(vy*deltaT)) ) {
            if (x < r || x > areaWidth - r) {
                vx *= -1;
            }
            if(vx > 0.1) {vx -= 0.02;} else if(vx <= 0.1) {vx += 0.02;}
            x += vx * deltaT;
        } else{
            if (x < r || x > areaWidth - r) {
                vx *= -1;
            }

            if (y < r || y > areaHeight - r) {
                vy *= -1;
            }

            vy -= gravity;

            x += vx * deltaT;
            y += vy * deltaT;
        }
    }

    public double getMass() {
        return mass;
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

    public void setVY(double newVY) {
        vy = newVY;
    }

    public void setVX(double newVX) {
        vx = newVX;
    }

    @Override
    public Ellipse2D getBallG() {
        return new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
    }
}