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

	private Balls balls;
	private double modelHeight;
	private double deltaT;

	@Override
	public void init() {
		super.init();
		double modelWidth = canvasWidth / PIXELS_PER_METER;
		modelHeight = canvasHeight / PIXELS_PER_METER;
		balls = new Balls(modelWidth, modelHeight);
	}

	@Override
	protected void drawFrame(Graphics2D g) {
		// Clear the canvas
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, canvasWidth, canvasHeight);

		// Update the model
		List<IBouncingBallsModel> ballList = balls.getBalls();
		List<Ellipse2D> ballG = new LinkedList<>();

		IBouncingBallsModel b1 = ballList.get(0);
		IBouncingBallsModel b2 = ballList.get(1);

		if(isColliding(b1, b2)) {
			setNewVelocity(b1, b2);
		}

		b1.tick(deltaT);
		b2.tick(deltaT);
		ballG.add(b1.getBallG());
		ballG.add(b2.getBallG());

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