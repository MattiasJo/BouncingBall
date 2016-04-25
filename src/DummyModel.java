import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

public class DummyModel {

	private final double areaWidth;
	private final double areaHeight;
	private final double gravity = 9.82/8;

	private double x, y, vx, vy, r, mass;

	public DummyModel(double width, double height) {
		this.areaWidth = width;
		this.areaHeight = height;
		x = 5;
		y = 8;
		vx = 4;
		vy = -gravity;
		r = 1;
		mass = 4;
	}

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

	public List<Ellipse2D> getBalls() {
		List<Ellipse2D> myBalls = new LinkedList<>();
		myBalls.add(new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r));
		return myBalls;
	}
}