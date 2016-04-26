import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

/**
 * Extends Animator with capability to draw a bouncing balls model.
 * 
 * This class can be left unmodified for the bouncing balls lab. :)
 * 
 * @author Oscar Soderlund
 * 
 */
@SuppressWarnings("serial")
public final class BouncingBalls extends Animator {

	private static final double PIXELS_PER_METER = 30;

	//private Balls balls;
	IBouncingBallsModel b1,b2;
	private double modelHeight;
	private double deltaT;

	@Override
	public void init() {
		super.init();
		double modelWidth = canvasWidth / PIXELS_PER_METER;
		modelHeight = canvasHeight / PIXELS_PER_METER;
		//balls = new Balls(modelWidth, modelHeight);
		b1 = new GenericBall(modelWidth, modelHeight, 2, 7, 0.8, 1);
		b2 = new GenericBall(modelWidth, modelHeight, 6, 9, 1.2, 2);
	}

	@Override
	protected void drawFrame(Graphics2D g) {
		// Clear the canvas
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, canvasWidth, canvasHeight);

		// Check if the balls is colliding, and eventually set a new velocity vector.
		if(isColliding(b1, b2)) {
			setNewVelocity(b1, b2);
		}

		// Update the coordinates for the balls
		b1.tick(deltaT);
		b2.tick(deltaT);

		// Transform balls to fit canvas
		g.scale(PIXELS_PER_METER, -PIXELS_PER_METER);
		g.translate(0, -modelHeight);

		//Paint ball 1
		g.setColor(Color.PINK);
		g.fill(b1.getBallG());

		//Paint ball 2
		g.setColor(Color.ORANGE);
		g.fill(b2.getBallG());
	}

	public boolean isColliding(IBouncingBallsModel b1, IBouncingBallsModel b2) {
		double delta = (b1.getR() + b2.getR());
		return ( Math.abs(b1.getX() - b2.getX()) <= delta
				&& Math.abs(b1.getY() - b2.getY()) <= delta );
	}

	public void setNewVelocity(IBouncingBallsModel b1, IBouncingBallsModel b2) {
		double v1 = b1.getVY();
		double u1 = b2.getVY();
		b1.setVY(v1 * -1);
		b1.setVX( (b1.getVX() + b2.getVX()) * -1 );
		b2.setVY(u1 * -1);
		b2.setVX( (b1.getVX() + b2.getVX()) * -1 );
	}

	@Override
	protected void setFrameRate(double fps) {
		super.setFrameRate(fps);
		// Update deltaT according to new frame rate
		deltaT = 1 / fps;
	}
}