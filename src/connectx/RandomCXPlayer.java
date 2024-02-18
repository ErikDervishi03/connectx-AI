import connectx.CXBoard;
import connectx.CXPlayer;

import java.util.Random;

public class RandomCXPlayer implements CXPlayer {
    private int M;
    private int N;
    private int X;
    private boolean isFirstPlayer;
    private int timeoutInSeconds;
    private Random random;

    @Override
    public void initPlayer(int M, int N, int X, boolean first, int timeout_in_secs) {
        this.M = M;
        this.N = N;
        this.X = X;
        this.isFirstPlayer = first;
        this.timeoutInSeconds = timeout_in_secs;
        this.random = new Random();
    }

    @Override
    public int selectColumn(CXBoard B) {
        // Get the list of available columns
        Integer[] availableColumns = B.getAvailableColumns();

        if (availableColumns.length > 0) {
            // If there are valid columns, select one at random
            int randomIndex = random.nextInt(availableColumns.length);
            return availableColumns[randomIndex];
        }

        // No valid moves, return -1 to indicate an error
        return -1;
    }

    @Override
    public String playerName() {
        return "RandomCXPlayer";
    }
}
