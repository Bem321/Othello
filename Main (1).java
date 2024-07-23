public class Main {
    public static void main(String[] args)throws Exception {
        PlayBoard game = new PlayBoard();
        game.playGame();
        game.printBoard();
        System.out.println(game.getWinner());
    }
}


