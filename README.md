# CONNECTX-AI BOT
 go see the documentation for more info

- Command-line compile.  In the connectx/ directory run:

        javac -cp ".." *.java */*.java

CXGame application:

- Human vs Computer.  In the connectx/ directory run:

        java -cp ".." connectx.CXGame 6 7 4 connectx.AnitaMaxMin.AnitaMaxMin

- Computer vs Computer. In the connectx/ directory run:

        java -cp ".." connectx.CXGame 6 7 4 connectx.AnitaMaxMin.AnitaMaxMin connectx.L1.L1

CXPlayerTester application:

- Output score only:

        java -cp ".." connectx.CXPlayerTester 6 7 4 connectx.AnitaMaxMin.AnitaMaxMin connectx.L1.L1

- Verbose output

        java -cp ".." connectx.CXPlayerTester 6 7 4 connectx.AnitaMaxMin.AnitaMaxMin connectx.L1.L1 -v

- Verbose output and customized timeout (1 sec) and number of game repetitions (10 rounds)

        java -cp ".." connectx.CXPlayerTester 6 7 4  connectx.AnitaMaxMin.AnitaMaxMin connectx.L1.L1 -v -t 1 -r 10
