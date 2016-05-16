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
	private boolean stillColliding = false;
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
	IBall oBall;
	@Override
	protected void drawFrame(Graphics2D g) {

		// Clear the canvas
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, canvasWidth, canvasHeight);

		// Transform balls to fit canvas
		g.scale(PIXELS_PER_METER, -PIXELS_PER_METER);
		g.translate(0, -modelHeight);

		for(IBall ball : ballList) {

			for(IBall otherBall: ballList) {
				if(ball != otherBall) {
					oBall = otherBall;
				}
			}

			// Apply gravity.
			ball.setVy(ball.getVy()- gravity);

			//Move balls
			if(!xWallCollision(ball)){
				ball.setX(ball.getX() + ball.getVx() * deltaT);
			} else {
				ball.setVx(ball.getVx()*-1);
			}

			if(!yWallCollision(ball)){
				ball.setY(ball.getY() + ball.getVy() * deltaT);
			} else {
				ball.setVy(ball.getVy()*-1);
			}

		}

		if(willCollide(b1,b2)){
			manageCollision(b1,b2);
			setNewVelocity();
		}

		/*if(isColliding(b1,b2) && !stillColliding){
			stillColliding = true;
			setNewVelocity();
		} else if(!isColliding(b1,b2)) {
			stillColliding = false;
		}*/

		//Paint balls
		for(IBall ball : ballList) {
			g.setColor(Color.BLACK);
			g.fill(ball.getBall());
		}
	}

	//Returns true if the ball will exit the area next
	//step and instead moves it to the wall.
	public boolean xWallCollision(IBall ball) {
		boolean xWallCollide = false;
		double r = ball.getR();
		double vx = ball.getVx();

		double nextXStep = ball.getX() + vx * deltaT;
		if (nextXStep < r) {
			ball.setX(r);
			xWallCollide = true;
		} else if (nextXStep > (modelWidth - r)) {
			ball.setX(modelWidth - r);
			xWallCollide = true;
		}
		return xWallCollide;
	}

	public boolean yWallCollision( IBall ball) {
		boolean yWallCollide = false;
		double r = ball.getR();
		double vy = ball.getVy();

		double nextYStep = ball.getY()+ vy * deltaT;
		if(nextYStep < r) {
			ball.setY(r);
			yWallCollide = true;
		} else if( nextYStep > (modelHeight - r) ) {
			ball.setY(modelHeight - r);
			yWallCollide = true;
		}
		return yWallCollide;
	}

	//Return true if balls will overlap next step.
	public boolean willCollide(IBall b1, IBall b2) {
		double rs = b1.getR() + b2.getR();
		double deltaX = Math.abs(b1.getX() + b1.getVx() * deltaT - b2.getX() + b2.getVx() * deltaT);
		double deltaY = Math.abs(b1.getY() + b1.getVy() * deltaT - b2.getY() + b2.getVy() * deltaT);
		double nextStepCenterDistances = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		return (nextStepCenterDistances <= rs);
	}

	// Moves balls a fraction of the distance
	public void manageCollision(IBall b1, IBall b2){

		double rs = b1.getR() + b2.getR();
		double deltaX = Math.abs(b1.getX() + b1.getVx() * deltaT*0.1 - b2.getX() + b2.getVx() * deltaT*0.1);
		double deltaY = Math.abs(b1.getY() + b1.getVy() * deltaT*0.1 - b2.getY() + b2.getVy() * deltaT*0.1);

		double centerDistances = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

		while((centerDistances < rs)) {
			if(!xWallCollision(b1)|| !yWallCollision(b1)) {
				b1.setX(b1.getX() - b1.getVx() * deltaT * 0.1);
				b1.setY(b1.getY() - b1.getVy() * deltaT * 0.1);
			}
			if(!xWallCollision(b2) || !yWallCollision(b2)) {
				b2.setX(b2.getX() - b2.getVx() * deltaT * 0.1);
				b2.setY(b2.getY() - b2.getVy() * deltaT * 0.1);
			}
			deltaX = Math.abs(b1.getX() + b1.getVx() * deltaT*0.1 - b2.getX() + b2.getVx() * deltaT*0.1);
			deltaY = Math.abs(b1.getY() + b1.getVy() * deltaT*0.1 - b2.getY() + b2.getVy() * deltaT*0.1);
			centerDistances = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		}

	}

	public void setNewVelocity() {
		//Distances in X and Y from ball center, to ball center.
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

	public double determineAngle(IBall ball) {
		if(ball.getVx() < 0 && ball.getVy() < 0) {
			return ( 3 * Math.PI/2 - Math.atan(ball.getVy()/ball.getVx()) );
		} else if(ball.getVx() > 0 && ball.getVy() < 0) {
			return ( 2 * Math.PI - Math.atan(ball.getVy()/ball.getVx()) );
		} else if(ball.getVx() < 0 && ball.getVy() > 0) {
			return ( Math.PI - Math.atan(ball.getVy()/ball.getVx()) );
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