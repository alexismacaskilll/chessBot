# chessBot

In this project, I wrote several chess bots that competed against other chess bots in my class. I implemented several (graph/tree) algorithms,  Minimax, and Alpha-Beta Pruning, (both sequential and parallel).

## Important Files to look at in the src->chess->bots folder.

- SimpleSearcher implements the sequential Minimax algorithm. 
      -Minimax is a kind of backtracking algorithm that is used in decision making and game theory to find the optimal move for a player, assuming that your opponent also plays optimally. It is widely used in two player turn-based games. In Minimax the two players are called maximizer and minimizer. The maximizer tries to get the highest score possible while the minimizer tries to do the opposite and get the lowest score possible.
- ParallelSearcher implements the parallel Minimax algorithm.
      -Minimax is a naturally parallelizable algorithm. Each “node” of the game tree can be run on independent threads. 
- AlphaBetaSearcher implements the sequential Alpha-Beta Pruning algorithm. 
      -Alpha-beta Pruning is a more efficient version of Minimax that avoids considering branches of the game tree that are irrelevant. Before getting too deep into the algorithm, it is very important to note that a correct Alpha-beta Search will return the same answer as Minimax. In other words, it is not an approximation algorithm, it only ignores moves that cannot change the answer.
- JamboreeSearcher implements the parallel Alpha-Beta Pruning algorithm.
      -Unfortunately, unlike minimax, alphabeta is not “naturally parallelizable”. In particular, if I attempt to parallelize the loop, it will be unable to propogate the new alpha and beta values to each iteration. This would result in us evaluating unnecessary parts of the tree. In practice, however, it turns out that that this is an acceptable loss, because the parallelism still gives us an overall benefit.

## Provided Code

I did not write the code found in cse332.* and chess.board.
