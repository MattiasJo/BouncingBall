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
		b1 = new GenericBall(modelWidth, modelHeight, 10, 8, 0.7);
		b2 = new GenericBall(modelWidth, modelHeight, 5, 8, 1.4);
		ballList.add(b1); ballList.add(b2);
	}

	@Override
	protected void drawFrame(Graphics2D g) {

		// Clear the canvas
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, canvasWidth, canvasHeight);

		// Checks for wall collision and moves ball.
		wallCheck();

		// Sets a new velocity vector.
		if(isColliding(b1,b2)) {
			setNewVelocity();
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
	//Return true if balls are overlapping.
	private boolean stillColliding = false;
	public boolean isColliding(IBouncingBallsModel b1, IBouncingBallsModel b2) {
		double rs = b1.getR()+ b2.getR();
		double deltaX = Math.abs(b1.getX() - b2.getX());
		double deltaY = Math.abs(b1.getY() - b2.getY());

		double centerDistances = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

		if (stillColliding && (centerDistances <= rs)) {
			return !(centerDistances <= rs);
		}else if( !(centerDistances <= rs) && stillColliding){
			stillColliding = false;
			return (centerDistances <= rs);
		} else if ((centerDistances <= rs)) {
			stillColliding = true;
			return (centerDistances <= rs);
		} else {
			return (centerDistances <= rs);
		}
	}	

	public void setNewVelocity() {

		double deltaX = Math.abs(b1.getX()-b2.getX());
		double deltaY = Math.abs(b1.getY()-b2.getY());

		double phi = Math.atan(deltaY/deltaX);

		double v1 = Math.hypot(b1.getVX(),b1.getVY());
		double v2 = Math.hypot(b2.getVX(),b2.getVY());

		if(b1.getVX() < 0) {
			v1 = v1*-1;
		}
		if(b2.getVX() < 0) {
			v2 = v2*-1;
		}

		System.out.println(v1);
		System.out.println(v2);

		double theta1 = Math.atan(b1.getVY()/b1.getVX());
		double theta2 = Math.atan(b2.getVY()/b2.getVX());

		double v1x = v1*Math.cos(theta1-phi);
		double v1y = v1*Math.sin(theta1-phi);

		double v2x = v2*Math.cos(theta2-phi);
		double v2y = v2*Math.sin(theta2-phi);

		double u1x = ((b1.getMass()-b2.getMass())*v1x + (b2.getMass()+b2.getMass())*v2x)
				/ (b1.getMass()+b2.getMass());

		double u2x = ((b1.getMass()+b1.getMass())*v1x + (b2.getMass()-b1.getMass())*v2x)
				/ (b1.getMass()+b2.getMass());

		b1.setVX(u1x*Math.cos(phi)+v1y*Math.cos((Math.PI/2)+phi));
		b1.setVY(u1x*Math.sin(phi)+v1y*Math.sin((Math.PI/2)+phi));
		b2.setVX(u2x*Math.cos(phi)+v2y*Math.cos((Math.PI/2)+phi));
		b2.setVY(u2x*Math.sin(phi)+v2y*Math.sin((Math.PI/2)+phi));
	}

	public void wallCheck() {

		for(IBouncingBallsModel ball : ballList) {
			double r = ball.getR();
			double vy = ball.getVY();
			double vx = ball.getVX();

			// If the next x coordinate is off screen,
			// moves the ball to the edge. If not take
			// the next step.
			double nextXStep = ball.getX()+vx*deltaT;
			if(nextXStep < r) {
				ball.setX(r);
				ball.setVX(vx *- 1);
			} else if( nextXStep > (modelWidth - r) ) {
				ball.setX(modelWidth - r);
				ball.setVX(vx * -1);
			} else {
				ball.takeXStep(nextXStep);
			}

			// If the next y coordinate is off screen,
			// moves the ball to the edge. If not take
			// the next step.
			double nextYStep = ball.getY()+ vy * deltaT;
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