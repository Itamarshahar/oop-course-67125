interface Ball {
    void ballOperation();
}

class Football implements Ball{
    private void kickBall() {
        System.out.println("Kicking the football");
    }

    @Override
    public void ballOperation() {
        kickBall();
    }
}

class Baseball implements Ball{
    private void batBall() {
        System.out.println("Batting the baseball");
    }

    @Override
    public void ballOperation() {
        batBall();
    }
}

class Basketball implements Ball{
    private void throwBall() {
        System.out.println("Throwing the basketball");
    }

    @Override
    public void ballOperation() {
        throwBall();
    }
}

//class Player {
//    void throwBasketBall(Basketball basketball) {
//        basketball.throwBall();
//    }
//
//    void batBaseBall(Baseball baseball) {
//        baseball.batBall();
//    }
//
//    void kickFootBall(Football football) {
//        football.kickBall();
//    }
//
//    public static void main(String[] args) {
//        Player player = new Player();
//        player.throwBasketBall(new Basketball());
//        player.batBaseBall(new Baseball());
//        player.kickFootBall(new Football());
//    }
//}


class PolyPlayer {
    void UseBall(Ball ball) {
        ball.ballOperation();
    }

    public static void main(String[] args) {
        PolyPlayer player = new PolyPlayer();
        Ball[] balls = { new Football(), new Baseball(), new Basketball() };
        for (Ball ball : balls) {
            player.UseBall(ball);
        }
    }
}
