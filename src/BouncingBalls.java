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
		b1 = new GenericBall(modelWidth, modelHeight, 8, 5, 0.8, 2);
		b2 = new GenericBall(modelWidth, modelHeight, 6, 9, 1.2, 4);
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
		return ( b1.getX() + b1.getR() == b2.getX() + b2.getR()
				&& b1.getY() + b1.getR() == b2.getY() + b2.getR() );
	}

	public void setNewVelocity(IBouncingBallsModel b1, IBouncingBallsModel b2) {
		b1.setVX(-1);
		b2.setVY(-1);
	}

	@Override
	protected void setFrameRate(double fps) {
		super.setFrameRate(fps);
		// Update deltaT according to new frame rate
		deltaT = 1 / fps;
	}
}