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
		b1 = new GenericBall(modelWidth, modelHeight, 10, 8, 0.7,"One");
		b2 = new GenericBall(modelWidth, modelHeight, 5, 8, 1.4,"Two");
	}

	@Override
	protected void drawFrame(Graphics2D g) {
		// Clear the canvas
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, canvasWidth, canvasHeight);

		// Check if the balls is colliding, and eventually set a new velocity vector.
		if(isColliding(b1, b2)) {
			setNewVelocity(b1, b2);
		} else 

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
		double deltaX = Math.abs(b1.getX() + b1.getVX()*deltaT) - Math.abs(b2.getX() + b2.getVX()*deltaT) ;
		double deltaY = Math.abs(b1.getY() + b1.getVY()*deltaT) - Math.abs(b2.getY() + b2.getVY()*deltaT) ;
		return (Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)) < rs);
	}

	public void setNewVelocity(IBouncingBallsModel b1, IBouncingBallsModel b2) {
		double b1vy = b1.getVY();
		double b1vx = b1.getVX();
		b1.setVY(b2.getVY() * 5/3);
		b1.setVX(b2.getVX() * 5/3);
		b2.setVX(b1vx * 1/3);
		b2.setVY(b1vy * 1/3);
		System.out.println("hit");

		/*
		double b1Theta = Math.toDegrees(Math.atan(b1.getVY()/b1.getVX()));
		double b2Theta = Math.toDegrees(Math.atan(b2.getVY()/b2.getVX()));

		double r = (b1.getR() + b2.getR());
		double deltaX =  Math.abs(b1.getX() - b2.getX());
		double deltaY = Math.abs(b1.getY() - b2.getY());
		double collitionTheta =  Math.toDegrees(Math.atan(deltaY/deltaX));

		double b1Ken = (b1.getMass()/2) * Math.pow(Math.hypot(b1.getVX(),b1.getVY()),2);
		double b2Ken = (b2.getMass()/2) * Math.pow(Math.hypot(b2.getVX(),b2.getVY()),2);
		*/

	}

	@Override
	protected void setFrameRate(double fps) {
		super.setFrameRate(fps);
		// Update deltaT according to new frame rate
		deltaT = 1 / fps;
	}
}