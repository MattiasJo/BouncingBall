import java.util.LinkedList;
import java.util.List;

public class Balls {

    List<IBouncingBallsModel> balls;
    double width, height;

    Balls(double modelWidth, double modelHeight) {
        width = modelWidth;
        height = modelHeight;
        init();
    }

    void init() {
        balls = new LinkedList<>();
        IBouncingBallsModel littleBall = new LittleBall(width, height);
        IBouncingBallsModel bigBall = new BigBall(width, height);
        balls.add(littleBall);
        balls.add(bigBall);
    }

    List<IBouncingBallsModel> getBalls() {
        return balls;
    }

}
