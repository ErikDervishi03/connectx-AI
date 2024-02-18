package connectx.AnitaMaxMin;

import connectx.CXBoard;
import connectx.CXCellState;
import connectx.CXGameState;
import connectx.CXPlayer;
import java.util.concurrent.TimeoutException;

public class AnitaMaxMin implements CXPlayer {
	private CXGameState myWin, yourWin, draw;
	private CXCellState our, opponent;
	private int TIMEOUT;
	private long START;
	private int M, N, K;
    private boolean iamfirst;
    private final Integer MYWIN = Integer.MAX_VALUE;
    private final Integer OPPWIN = Integer.MIN_VALUE;
    private final Integer DRAW = 0;
    private CXBoard board;
    private Integer[] myTokens;
    private Integer[] oppTokens;
    private final int MY_TOKENS_WEIGHT = 10;
    private final int OPP_TOKENS_WEIGHT = 20;
    private int currentDepth;

	public AnitaMaxMin() {
	}

 @Override
	public void initPlayer(int M, int N, int K, boolean first, int timeout_in_secs) {
		myWin = first ? CXGameState.WINP1 : CXGameState.WINP2;
		yourWin = first ? CXGameState.WINP2 : CXGameState.WINP1;
        draw = CXGameState.DRAW;

		TIMEOUT = timeout_in_secs;
		this.M = M;
		this.N = N;
        this.K = K;
        iamfirst = first;

		//Inizializzazione valori delle mosse
		our = first ? CXCellState.P1 : CXCellState.P2;
        opponent = first ? CXCellState.P2 : CXCellState.P1;

        myTokens = new Integer[K-1];
        oppTokens = new Integer[K-1];

        //Inizializza gli array con zeri
        for (int i = 0; i < K-1; i++) {
            myTokens[i] = 0;
            oppTokens[i] = 0;
        }
	}

@Override
    public int selectColumn(CXBoard B) {
        START = System.currentTimeMillis(); // Save starting time
        board = B.copy();

        Integer[] L = board.getAvailableColumns();

        if (B.numOfMarkedCells() <= 1)
            return N / 2;

        if (L.length == 1)
            return L[0];
    	int bestMove = L[0];

        try {
            Integer maxEval = Integer.MIN_VALUE;
            Integer alpha = Integer.MIN_VALUE;
            Integer beta = Integer.MAX_VALUE;

            currentDepth = 1;
            
            while(currentDepth<=board.numOfFreeCells()){
                //System.out.println("depth  : " + currentDepth);
                for (int i: middleSort(L)) {
                    checktime();
                    board.markColumn(i);
                    if(board.gameState() == myWin)
                        return i;
                    Integer evaltmp = alphabeta(alpha, beta, currentDepth, false); 
                    //System.out.println(evaltmp);
                    if (evaltmp > maxEval) {
                        maxEval = evaltmp;
                        bestMove = i;
                    }

                    board.unmarkColumn();
                }
                currentDepth++;
            }
        } catch (TimeoutException e) {
            //System.err.println("timeout");
            return bestMove;
        }
        return bestMove;

    }

	private void checktime() throws TimeoutException {
		if ((System.currentTimeMillis() - START) / 1000.0 >= TIMEOUT * (99.0 / 100.0))
			throw new TimeoutException();
	}

    public Integer alphabeta(Integer alpha, Integer beta,int depth, boolean maximizingPlayer) throws TimeoutException {
        checktime();
        
        CXGameState state = board.gameState();
        if(state != CXGameState.OPEN || depth == 0) {
            if(state == myWin){
                //System.out.println(MYWIN);
                return MYWIN;
            }
            else if (state == yourWin){
                //System.out.println(OPPWIN);
                return OPPWIN;
            }
            else if(state == draw){
                //System.out.println(DRAW);
                return DRAW;
            }
            else{
                //System.out.println(eval.heuristic());
                return heuristic();
            }
        }

        Integer alphabetaResult;

        if(maximizingPlayer) {
            alphabetaResult = Integer.MIN_VALUE;
            for(int i :  middleSort(board.getAvailableColumns())) {
                board.markColumn(i);
                alphabetaResult = Math.max(alphabetaResult, alphabeta(alpha, beta,depth-1, false));
                board.unmarkColumn();
                alpha = Math.max(alphabetaResult, alpha);
                if (beta <= alpha)
                    break;
            }
        }
        else {
            alphabetaResult = Integer.MAX_VALUE;
            for(int i :  middleSort(board.getAvailableColumns())) { 
                board.markColumn(i);
                alphabetaResult = Math.min(alphabetaResult, alphabeta(alpha, beta,depth-1, true));
                board.unmarkColumn();
                beta = Math.min(alphabetaResult, beta);
                if (beta <= alpha)
                    break;
            }
        }

        alphabetaResult = alphabetaResult < -1 ? alphabetaResult + 1 : alphabetaResult;
        alphabetaResult = alphabetaResult > 1 ? alphabetaResult - 1 : alphabetaResult;

        return alphabetaResult;
    }

    public Integer[] middleSort(Integer[] L) {
        Integer[] V = new Integer[L.length];
        int j = 0;
        for (int i = 0; (i <= L.length / 2); i++) {
            V[j] = L[L.length / 2 + i];
            j++;
            if (i != 0) {
                V[j] = L[L.length / 2 - i];
                j++;
            }
            if ((i + 1 == L.length / 2) && ((L.length % 2) == 0)) {
                V[j] = L[0];
                break;
            }
        }
        return V;
    }

    public void calculate_tokens(){
        CXCellState[][] myB = board.getBoard();

        vertical_count(myB);
        horizontal_count(myB);
        diagonal_count(myB);
    }

    public Integer heuristic() {
        calculate_tokens();

        Integer heuristic = 0;
        for(int i = 0; i < K-1; i ++) {
            heuristic += myTokens[i]*myTokens[i]*myTokens[i] - oppTokens[i]*oppTokens[i]*oppTokens[i];
            myTokens[i]=0;
            oppTokens[i]=0;
        }
        return heuristic;
    }

    private void vertical_count(CXCellState[][] myB) {
        int counter;
        CXCellState expected;
        
        for(int j = 0; j < N; j ++) {
            // int cells_free = MyBoard.get_RP(j);
            if(myB[M-1][j] == CXCellState.FREE) //colonna libera, non ha senso controllarla
                continue;
            
            int cell2controll = 0; // n celle da controllare
            for(int i = 0; i < M; i++) {//troviamo la prima cella non libera
                if(myB[i][j] != CXCellState.FREE) {
                    cell2controll = i;
                    break;
                }
            }
            expected = myB[cell2controll][j];
            counter = 0;
            while(cell2controll <= M-1 && expected == myB[cell2controll][j] && counter < K) {
                counter++;
                cell2controll++;
            }
            if(counter > 1){
              if(expected == our){
                  myTokens[counter-2] ++;
                  //System.out.println("my vert");
              }
              else{
                  oppTokens[counter-2] ++;
                  //System.out.println("opp vert");
              }
            }
        }
    }

    private void horizontal_count(CXCellState[][] myB) {
        boolean allFree = false;
        for(int i = M-1; i >=0 && !allFree; i --) {
            allFree = true;
            for(int j = 0; j <= N - K; j ++) {
                int countPlayer = 0;      
                int countOpponent = 0;       
                for (int x = 0; x < K; x++) {                 
                    CXCellState cellState = myB[i][j + x];        
                    if(cellState != CXCellState.FREE){
                        allFree = false;
                    }
                    if (cellState == our)
                        countPlayer++;
                    else
                        countOpponent++;
                }
                if (countPlayer > 1 && countOpponent == 0)                 
                    myTokens[countPlayer-2]++;
                else if (countOpponent > 1 && countPlayer == 0)              
                    oppTokens[countOpponent-2]++; 
            }
        }
    }

    private void diagonal_count(CXCellState[][] myB) {
        for (int i = 0; i <= M - K; i++) {      
            for (int j = 0; j <= N - K; j++) {     
                int countPlayer = 0;     
                int countOpponent = 0;  
                int countFree = 0;      

                for (int x = 0; x < K; x++) {                                       
                    CXCellState cellState = myB[i + x][j + x];

                    if (cellState == our)
                        countPlayer++;
                    else
                        countOpponent++;
                    }

                if (countPlayer > 1 && countOpponent == 0)                 
                    myTokens[countPlayer-2]++;
                else if (countOpponent > 1 && countPlayer == 0)              
                    oppTokens[countOpponent-2]++;
            }       
        }

        for (int i = K - 1; i < M; i++) {         
            for (int j = 0; j <= N - K; j++) {     
                int countPlayer = 0;  
                int countOpponent = 0; 
                int countFree = 0;   

                for (int x = 0; x < K; x++) {                                                                             
                    CXCellState cellState = myB[i - x][j + x];

                    if (cellState == our)
                        countPlayer++;
                    else
                        countOpponent++;
                
                }

                if (countPlayer > 1 && countOpponent == 0)                 
                    myTokens[countPlayer-2]++;
                else if (countOpponent > 1 && countPlayer == 0)              
                    oppTokens[countOpponent-2]++;    
            }
        }
    }




 @Override
	public String playerName() {
		return "AnitaMaxMin";
	}
}

