# ConnectX Project

## Table of Contents
1. [Introduction](#1-introduction)
2. [Implementation Choices](#2-implementation-choices)
   - [SelectColumn()](#21-selectcolumn)
   - [Iterative Deepening](#211-iterative-deepening)
   - [Alpha/Beta Pruning](#212-alphabeta-pruning)
   - [MiddleSort()](#213-middlesort)
   - [Heuristics](#22-heuristics)
3. [Computational Costs](#3-computational-costs)
   - [Cost of Heuristics](#31-cost-of-heuristics)
   - [Alphabeta](#32-alphabeta)
   - [SelectColumn](#33-selectcolumn)
   - [Summary Table](#34-summary-table)
4. [Ideas for Future Implementation](#4-ideas-for-future-implementation)
5. [Sitography](#5-sitography)
6. [Running the Game](#6-running-the-game)
7. [Running Tests](#7-running-tests)


## 1. Introduction
The project is based on a generalization of the classic Connect 4 game. The difference is that matches take place in a game matrix of M×N, where M is the number of rows and N is the number of columns. Players aim to connect X consecutive pieces to win. Pieces can be connected vertically, horizontally, and diagonally. The project aims to create a bot capable of playing and winning ConnectX games against other bots, making decisions based on opponent moves while adhering to a given TIMEOUT constraint.

## 2. Implementation Choices

### 2.1 SelectColumn()
The goal of `selectColumn()` is to choose a column from the available options in which to make a move. The best move is determined using a search strategy called Iterative Deepening, which utilizes a minimax algorithm with alpha/beta pruning.

#### 2.1.1 Iterative Deepening
This technique maximizes the time available for making a decision, increasing depth with each iteration until either the number of free cells is reached or a win is achieved.

#### 2.1.2 Alpha/Beta Pruning
Alpha/Beta Pruning significantly reduces the number of nodes explored by ignoring sub-trees that wouldn't influence the evaluation of the optimal move. 

#### 2.1.3 MiddleSort()
The `MiddleSort()` function reorders an array of integers to prioritize moves in the center of the board, enhancing the efficiency of the search.

### 2.2 Heuristics
Heuristics are based on the concept of tokens—chains of connected pieces that could lead to victory. Tokens are counted for both the player and the opponent, using methods to evaluate vertical, horizontal, and diagonal connections.

## 3. Computational Costs

### 3.1 Cost of Heuristics
- **Count Vertical:** O(MN)
- **Count Horizontal:** O(MNX)
- **Count Diagonal:** O(MNX)
- **Heuristic Calculation:** O(MNX)

### 3.2 Alphabeta
The computational cost of alpha/beta pruning in the worst case remains O(N^p), with a total cost of O(MN^pX) when nodes are evaluated.

### 3.3 SelectColumn
The `selectColumn` function's total cost is O(MN^pXP) due to the nested calls to alpha/beta pruning.

### 3.4 Summary Table
| Function      | Cost               |
|---------------|--------------------|
| Count Vertical| O(MN)              |
| Count Horizontal | O(MNX)          |
| Count Diagonal| O(MNX)             |
| Heuristic     | O(MNX)             |
| MiddleSort    | O(N)               |
| Alphabet      | O(MN^pX)           |
| SelectColumn  | O(MN^pXP)          |

## 4. Ideas for Future Implementation
To enhance performance and explore a greater depth, a hash table can be incorporated using the Zobrist hashing technique, allowing for quick verification of previously explored game states.

## 5. Sitography
- The idea of implementing heuristics is inspired by various literature.
- Iterative deepening: [link](#).

## 6. Running the Game
1. **Compile the project:**
   ```bash
   javac -cp ".." *.java */*.java
2. **Human vs Computer:**
   ```bash
   java -cp ".." connectx.CXGame 6 7 4 connectx.AnitaMaxMin.AnitaMaxMin
3. **Computer vs Computer:**
   ```bash
   java -cp ".." connectx.CXGame 6 7 4 connectx.AnitaMaxMin.AnitaMaxMin connectx.L1.L1
   
## 7. Running Tests
1. **Output score only:**
   ```bash
   java -cp ".." connectx.CXPlayerTester 6 7 4 connectx.AnitaMaxMin.AnitaMaxMin connectx.L1.L1
2. **Verbose output:**
   ```bash
   java -cp ".." connectx.CXPlayerTester 6 7 4 connectx.AnitaMaxMin.AnitaMaxMin connectx.L1.L1 -v
3. **Verbose output with customized timeout (1 sec) and number of game repetitions (10 rounds):**
    ```bash
    java -cp ".." connectx.CXPlayerTester 6 7 4 connectx.AnitaMaxMin.AnitaMaxMin connectx.L1.L1 -v -t 1 -r 10
