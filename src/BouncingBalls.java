import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public final class BouncingBalls extends Animator {

	private static final double PIXELS_PER_METER = 30;
	private List<IBall> ballList;
	private double modelWidth, modelHeight;
	private double deltaT;
	private final double gravity = 9.81/20;
	IBall b1,b2;

	@Override
	public void init() {
		super.init();
		modelWidth = canvasWidth / PIXELS_PER_METER;
		modelHeight = canvasHeight / PIXELS_PER_METER;
		ballList = new LinkedList<>();
		b1 = new Ball(10, 8, 3, 3, 0.7);
		b2 = new Ball(5, 8, 3, 3, 1.4);
		ballList.add(b1);
		ballList.add(b2);
	}
	@Override
	protected void drawFrame(Graphics2D g) {

		// Clear the canvas
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, canvasWidth, canvasHeight);

		// Transform balls to fit canvas
		g.scale(PIXELS_PER_METER, -PIXELS_PER_METER);
		g.translate(0, -modelHeight);
		//Checks for collision

		for(IBall ball : ballList) {
			// Apply gravity.
            //if(!isColliding(b1,b2)) {
                ball.setVy(ball.getVy() - gravity);
            //}

			//Move balls
			ball.setX(ball.getX() + ball.getVx() * deltaT);
			if(xWallCollision(ball)){
				ball.setVx(ball.getVx() * -1);
				manageXWallCollision(ball);
			}

			ball.setY(ball.getY() + ball.getVy() * deltaT);
			if(yWallCollision(ball)){
				ball.setVy(ball.getVy() * -1);
				manageYWallCollision(ball);
			}
		}

        if(isColliding(b1,b2)){
            manageCollision(b1,b2);
            setNewVelocity();
        }
		//Paint balls
		for(IBall ball : ballList) {
			g.setColor(Color.BLACK);
			g.fill(ball.getBall());
		}
	}

	//Returns true if the ball has exited the area along the X axis.
	public boolean xWallCollision(IBall ball) {
		boolean xWallCollide = false;

		//Ball radius.
		double r = ball.getR();

		//Ball X Coordinate.
		double loc = ball.getX();

		//If next step leaves the room moves to wall.
		if (loc < r) {
			xWallCollide = true;
		} else if (loc > (modelWidth - r)) {
			xWallCollide = true;
		}
        //Returns true if the ball has left the room.
		return xWallCollide;
	}

	public void manageXWallCollision(IBall ball) {

		//Ball radius.
		double r = ball.getR();

		//Ball X Coordinate.
		double loc = ball.getX();

        //Moves the ball to the edge of the wall.
		if (loc < r) {
			ball.setX(r);
		} else if (loc > (modelWidth - r)) {
			ball.setX(modelWidth - r);
		}
	}

	//Returns true if the ball will exited the area along the Y.
	public boolean yWallCollision( IBall ball) {
		boolean yWallCollide = false;

		//Ball radius.
		double r = ball.getR();

		//Ball Y Coordinate.
		double loc = ball.getY();

		if(loc < r) {
			yWallCollide = true;
		} else if( loc > (modelHeight - r) ) {
			yWallCollide = true;
		}
		//Returns true if the ball has left the room.
		return yWallCollide;
	}

	public void manageYWallCollision( IBall ball) {
		//Ball radius.
		double r = ball.getR();

		//Ball Y Coordinate
		double loc = ball.getY();

        //Moves the ball to the edge of the wall.
		if(loc < r) {
			ball.setY(r);
		} else if( loc > (modelHeight - r) ) {
			ball.setY(modelHeight - r);
		}
	}

	//Returns true if balls are overlapping.
	public boolean isColliding(IBall b1, IBall b2) {

		//Combined ball radius
		double rs = b1.getR() + b2.getR();

		//Distances in X and Y
		double deltaX = Math.abs(b1.getX() - b2.getX());
		double deltaY = Math.abs(b1.getY() - b2.getY());

		//Distance from ball center to ball center.
		double centerDistances = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

		//Returns true when balls are centers are closer
		//then their combined radius.
		return (centerDistances <= rs);
	}

	//Move ball back along its path untill no longer colliding.
	public void manageCollision(IBall b1, IBall b2){

		double stepFraction = 0.1;
		//Combined ball radius

		boolean a = false;
		boolean b = false;

		//If the center distance is less than the radiuses move ball
		while((isColliding(b1,b2))) {

			if(!xWallCollision(b1)) {
                b1.setX(b1.getX() - b1.getVx() * deltaT * stepFraction);
            }
            if(!yWallCollision(b1)) {
				b1.setY(b1.getY() - b1.getVy() * deltaT * stepFraction);
			}
            if (!xWallCollision(b2)){
                b2.setX(b2.getX() - b2.getVx() * deltaT * stepFraction);
            }
            if(!yWallCollision(b2)){
                b2.setY(b2.getY() - b2.getVy() * deltaT * stepFraction);
			}
		}
	}

	public void setNewVelocity() {

		//Distances in X and Y from ball center to ball center.
		double deltaX = Math.abs(b1.getX()-b2.getX());
		double deltaY = Math.abs(b1.getY()-b2.getY());

		//Angle of line running through ball centers.
		double phi = Math.atan(deltaY/deltaX);

		//Velocity vector for balls.
		double v1 = Math.hypot(b1.getVy(),b1.getVx());
		double v2 = Math.hypot(b2.getVy(),b2.getVx());

		//Vector angle, unique for each quadrant of the coordinate system.
		double theta1 = determineAngle(b1);
		double theta2 = determineAngle(b2);

		//Velocities of X and Y in Polar coordinate system.
		double v1x = v1*Math.cos(theta1-phi);
		double v1y = v1*Math.sin(theta1-phi);
		double v2x = v2*Math.cos(theta2-phi);
		double v2y = v2*Math.sin(theta2-phi);

		//Velocities after collision in Polar coordinate system.
		double u1x = ((b1.getMass()-b2.getMass())*v1x + (b2.getMass()+b2.getMass())*v2x)
				/ (b1.getMass()+b2.getMass());
		double u2x = ((b1.getMass()+b1.getMass())*v1x + (b2.getMass()-b1.getMass())*v2x)
				/ (b1.getMass()+b2.getMass());

		//Sets new X and Y velocities for the rectangular coordinate system.
		b1.setVx(u1x*Math.cos(phi)+v1y*Math.cos((Math.PI/2)+phi));
		b1.setVy(u1x*Math.sin(phi)+v1y*Math.sin((Math.PI/2)+phi));
		b2.setVx(u2x*Math.cos(phi)+v2y*Math.cos((Math.PI/2)+phi));
		b2.setVy(u2x*Math.sin(phi)+v2y*Math.sin((Math.PI/2)+phi));
	}

	//Determines angle for velocity vector.
	public double determineAngle(IBall ball) {
		// Fourth quadrant
		if(ball.getVx() > 0 && ball.getVy() < 0) {
			return ( 2 * Math.PI - Math.atan(ball.getVy()/ball.getVx()) );

            // Third quadrant
		} else if(ball.getVx() < 0 && ball.getVy() < 0) {
			return ( 3 * Math.PI/2 - Math.atan(ball.getVy()/ball.getVx()) );

			//Second quadrant
		} else if(ball.getVx() < 0 && ball.getVy() > 0) {
			return ( Math.PI - Math.atan(ball.getVy()/ball.getVx()) );

            //First quadrant
		} else {
			return ( Math.atan(ball.getVy()/ball.getVx()) );
		}
	}

	@Override
	protected void setFrameRate(double fps) {
		super.setFrameRate(fps);
		// Update deltaT according to new frame rate
		deltaT = 1 / fps;
	}
}