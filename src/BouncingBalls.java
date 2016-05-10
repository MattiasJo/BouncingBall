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

		// Transform balls to fit canvas
		g.scale(PIXELS_PER_METER, -PIXELS_PER_METER);
		g.translate(0, -modelHeight);

		// Checks for wall collision and moves ball.
		for(IBouncingBallsModel ball : ballList) {
			wallCheck(ball);
		}

		// Sets a new velocity vector.
		if(isColliding(ballList.get(0),b2)) {
			setNewVelocity();
		}

		// Update the coordinates for the balls
		for(IBouncingBallsModel ball : ballList) {
			ball.tick(deltaT);
		}

		//Paint balls
		g.setColor(Color.RED);
		for(IBouncingBallsModel ball : ballList) {
			g.fill(ball.getBallG());
		}
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
		//Distances in X and Y from ball center, to ball center.
		double deltaX = Math.abs(b1.getX()-b2.getX());
		double deltaY = Math.abs(b1.getY()-b2.getY());

		//Angle of line running through ball centers.
		double phi = Math.atan(deltaY/deltaX);

		double[] vx = new double[ballList.size()];
		double[] vy = new double[ballList.size()];
		for(IBouncingBallsModel ball : ballList) {
			double v = Math.hypot(ball.getVX(),ball.getVY());
			double theta = determineAngle(ball);
			vx[ballList.indexOf(ball)] = v*Math.cos(theta-phi);
			vy[ballList.indexOf(ball)] = v*Math.sin(theta-phi);
		}

		//Velocities after collision in Polar coordinate system.
		double u1x = ((b1.getMass()-b2.getMass())*vx[0] + (b2.getMass()+b2.getMass())*vx[1])
				/ (b1.getMass()+b2.getMass());
		double u2x = ((b1.getMass()+b1.getMass())*vx[0] + (b2.getMass()-b1.getMass())*vx[1])
				/ (b1.getMass()+b2.getMass());

		//Sets new X and Y velocities for the rectangular coordinate system.
		b1.setVX( u1x*Math.cos(phi) + vy[0]*Math.cos((Math.PI/2)+phi) );
		b1.setVY( u1x*Math.sin(phi) + vy[0]*Math.sin((Math.PI/2)+phi) );
		b2.setVX( u2x*Math.cos(phi) + vy[1]*Math.cos((Math.PI/2)+phi) );
		b2.setVY( u2x*Math.sin(phi) + vy[1]*Math.sin((Math.PI/2)+phi) );
	}

	public double determineAngle(IBouncingBallsModel ball) {
		if(ball.getVX() < 0 && ball.getVY() < 0) {
			return ( 3 * Math.PI/2 - Math.atan(ball.getVY()/ball.getVX()) );
		} else if(ball.getVX() > 0 && ball.getVY() < 0) {
			return ( 2 * Math.PI - Math.atan(ball.getVY()/ball.getVX()) );
		} else if(ball.getVX() < 0 && ball.getVY() > 0) {
			return ( Math.PI - Math.atan(ball.getVY()/ball.getVX()) );
		} else {
			return ( Math.atan(ball.getVY()/ball.getVX()) );
		}
	}

	public void wallCheck(IBouncingBallsModel ball) {
		double r = ball.getR();
		double vx = ball.getVX();
		double vy = ball.getVY();

		// If the next x coordinate is off screen, moves the ball to the edge. If not take the next step.
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

		// If the next y coordinate is off screen, moves the ball to the edge. If not take the next step.
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

	@Override
	protected void setFrameRate(double fps) {
		super.setFrameRate(fps);
		// Update deltaT according to new frame rate
		deltaT = 1 / fps;
	}
}