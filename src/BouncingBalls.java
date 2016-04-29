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

	private List<IBouncingBallsModel> ballList;
	IBouncingBallsModel b1,b2;
	private double modelWidth, modelHeight;
	private double deltaT;

	@Override
	public void init() {
		super.init();
		modelWidth = canvasWidth / PIXELS_PER_METER;
		modelHeight = canvasHeight / PIXELS_PER_METER;
		//balls = new Balls(modelWidth, modelHeight);
		ballList = new LinkedList<>();
		b1 = new GenericBall(modelWidth, modelHeight, 10, 8, 0.7,"One");
		b2 = new GenericBall(modelWidth, modelHeight, 5, 8, 1.4,"Two");
		ballList.add(b1); ballList.add(b2);
	}

	@Override
	protected void drawFrame(Graphics2D g) {

		// Clear the canvas
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, canvasWidth, canvasHeight);

		// Check if the balls is colliding, and eventually set a new velocity vector.
		if(isColliding(b1, b2)) {
			setNewVelocity();
		}

		wallCheck();

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
	//Return true if a after next step balls will overlap.
	public boolean isColliding(IBouncingBallsModel b1, IBouncingBallsModel b2) {
		double rs = b1.getR() + b2.getR();
		double deltaX = Math.abs(b1.getX() + b1.getVX() * deltaT) - Math.abs(b2.getX() + b2.getVX() * deltaT);
		double deltaY = Math.abs(b1.getY() + b1.getVY() * deltaT) - Math.abs(b2.getY() + b2.getVY() * deltaT);

		return (Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)) < rs);
	}

	public void setNewVelocity() {

		for(IBouncingBallsModel ball : ballList) {
			double ang = Math.tanh(ball.getVY()/ball.getVX());
			double r = Math.sqrt(Math.pow(ball.getVY(),2) + Math.pow(ball.getVX(),2));

			if(ball.getVX() < 0) {
				ball.setVX(r * Math.cos(ang));
			} else {
				ball.setVX(r * Math.cos(ang)*-1);
			}

			if(ball.getVX() < 0) {
				ball.setVY(r * Math.sin(ang));
			} else {
				ball.setVY(r * Math.sin(ang) *- 1);
			}
		}

	}

	public void wallCheck() {

		for(IBouncingBallsModel ball : ballList) {
			double r = ball.getR();
			double vy = ball.getVY();

			// If the next x coordinate is off screen,
			// moves the ball to the edge.
			double nextXStep = ball.getX()+ball.getVX()*deltaT;
			if(nextXStep < r) {
				ball.setX(r);
				ball.setVX(ball.getVX() *- 1);
			} else if( nextXStep > (modelWidth - r) ) {
				ball.setX(modelWidth - r);
				ball.setVX(ball.getVX() *- 1);
			} else {
				ball.takeXStep(nextXStep);
			}

			// If the next y coordinate is off screen,
			// moves the ball to the edge.
			double nextYStep = ball.getY() + ball.getVY() * deltaT;
			if(nextYStep < r) {
				ball.setY(r);
				ball.setVY(vy * -1);
			} else if( nextYStep > (modelHeight - r) ) {
				ball.setY(modelHeight - r);
				ball.setVY(vy *- 1);
			} else {
				ball.takeYStep(nextYStep);
			}
		}
	}

	@Override
	protected void setFrameRate(double fps) {
		super.setFrameRate(fps);
		// Update deltaT according to new frame rate
		deltaT = 1 / fps;
	}
}