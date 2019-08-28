# Project 3 (Chess) Write-Up #
--------

## Project Enjoyment ##
- How Was Your Partnership?
  <pre>Our partnership was great. In this project, we worked very closely together on each part because in the project spec it mentioned that all of the parts of this project involve understanding exactly how the previous parts worked. Therefore, we did less of splitting up the work by having one of us do half of the parts and the other one do the rest. Working together entirely was also tricky for this project in particular because a big chunk of the work fell over Thanksgiving, and one of us was out of town for almost a full week for Thanksgiving. Nonetheless, we were still able to get all of the work done with just a little bit longer of a work day than usual before the second checkpoint. </pre>
  
- What was your favorite part of the project?
  <pre> Our favorite part of the project was Jamboree and Alphabeta. We found the concept behind alphabeta pruning very interesting and fun to implement because it was very clear why we were implementing this algorithm. Alpha beta pruning is important because it allows for branches of the search tree to be eliminated, which allows for a deeper search to be performed within the same time. In past projects, it wasn't always clear why we would implement certain sorts or algorithms, but since in this project it was very clear how this algorithm improved the performance of our chess bot, it made us more interested in implementing it. We also liked implementing the jamboree algorithm. Although it was difficult to understand all of the different constants apart of jamboree, once we understood it, it was really fun to implement. Since alphabeta is not naturally parallelizable, jamboree really tested how well we really understand parallel programming. Although we could pass the tests at the second checkpoint, we weren't implementing jamboree in the most efficient way. It took us several iterations, continuously improving the efficiency of our parallelism,  to come up with the final implementation that we ended up turning in. Constantly trying to improve our jamboree algorithm also really tested our knowledge of parallel programming. </pre>

- What was your least favorite part of the project?
  <pre> Our least favorite part of the project was trying to beat clamps consistently to get 8.0/8.0 points. Even though we thought our jamboree searcher was pretty good, it still wasn't good enough to beat clamps. We had to try to go back and implement iterative deepening, move ordering, or a transposition table. This was kind of difficult to understand how to do this, so this was our least favorite part. In addition, we didn't know which one to try to do first and we didn't know if this was going to allow us to beat clamps consistently or not before we implemented it, so this was our least favorite part. </pre>

- How could the project be improved?
  <pre> We think the project could be improved if there was more instruction on how to implement iterative deepening, move ordering, or a transposition table. The diagrams for the jamboree and the other searchers were very helpful. We think diagrams or links to videos on how these improvements work / could be implemented would help. We also think it would have been better if the spec just told us to do one of these, not given us the option to do all three. We think it would have even been better if these suggestions were in the above and beyond section and clamps was easier to beat without any of these improvements. </pre>

- Did you enjoy the project?
  <pre>Overall we enjoyed the project. We really liked how closely related each part of the project was to one another. We really enjoyed how we implemented the sequential version first followed by a parallel version. This made the the project parts feel like they flowed really well together and made sure we were understanding the algorithms since we implemented them in both ways. We also liked the experiments for this project. The experiments helped us find the optimal cutoff and number of processors in order to help us with the main goal: to beat clamps. We liked how the experiments all linked together. </pre>
    
-----

## The Chess Server ##
- When you faced Clamps, what did the code you used do?  Was it just your jamboree?  Did you do something fancier?
  <pre> When we faced Clamps, we used an optimized version of our jamboree which implemented some iterative deepening. We think the implementation of this iterative deepening improved our bots ability to beat clamps by a little, but not by a whole lot.   </pre>

- Did you enjoy watching your bot play on the server?  Is your bot better at chess than you are?
  <pre> Yes we enjoyed watching our bot play on the server. After watching our bot, it is really clear it is better than chess than either of us. When we watched our bot play clamps, we tried to think of the moves we would choose whenever it was our bots turn. We rarely chose the same move as our bot; our bot was always thinking several moves ahead, but we only were thinking one move ahead. There is just too many possibilities to keep track of in our head to be able to think 5 moves ahead like our bot, so our bot is much better than us.  </pre>

- Did your bot compete with anyone else in the class?  Did you win?
  <pre> Yes. We competed against osaka. Since osaka is one of my friend's bots, I asked about what depth they were running, and we were both running at depth 5. We ended up beating osaka. We competed against osaka before we implemented any move ordering. I asked osaka if they implemented any move ordering either, and they did not. </pre>

- Did you do any Above and Beyond?  Describe exactly what you implemented.
  <pre> No we didn't do any. </pre>

## Experiments ##
**NOTE**: Feel free to tweak your divide cutoff variable to beat clamps, **but keep it constant for your experiments.**

### Chess Game ###

#### Hypotheses ####
Suppose your bot goes 3-ply deep.  How many game tree nodes do you think
it explores (we're looking for an order of magnitude) if:
 - ...you're using minimax?
    <pre> The games handout attached in the project spec mentioned that in chess the the average branching factor is approximately 35. We use the average branching factor because as we will see in later experiments, some positions in chess have more moves than others. The middle board positions have many more possible moves than the beginning or the end. So we predict that if our bot goes 3-ply deep, then our game tree will have to explore 35^3 nodes. 35^3 nodes is equal to 42,875 nodes, which is equal to an order of magnitude of 10^4. We predict that the minimax and parallel minimax will explore the same amount of game tree nodes because they both still have to explore the same amount of game tree nodes; the only difference is the parallel minimax can search the nodes faster since each node of the game tree can be run on independent threads.  </pre>
 - ...you're using alphabeta?
    <pre> If we are using alphabeta, we know that it will explore less game tree nodes because of the way the alphabeta algorithm works. Alpha beta pruning allows for branches of the search tree to be eliminated, which allows for us to explore less game tree nodes, but we aren't sure exactly how many less it will allow us to explore. So we predict that if our bot goes 3-ply deep, then our bot will explore an order of magnitude of 10^3 game tree nodes. </pre>

#### Results ####
Run an experiment to determine the actual answers for the above.  To run
the experiment, do the following:
1. Run SimpleSearcher against AlphaBetaSearcher and capture the board
   states (fens) during the game.  To do this, you'll want to use code
   similar to the code in the testing folder.
2. Now that you have a list of fens, you can run each bot on each of them
   sequentially.  You'll want to slightly edit your algorithm to record the
   number of nodes you visit along the way.
3. Run the same experiment for 1, 2, 3, 4, and 5 ply. And with all four
   implementations (use ply/2 for the cut-off for the parallel
   implementations). Make a pretty graph of your results and fill in the table 
   here as well. NOTE: Your result should be the average number of nodes visited
   across all fens for each bot & depth.

<pre>TODO: Fill in the table below</pre>


|      Algorithm     | 1-ply | 2-ply | 3-ply | 4-ply      | 5-ply         |
| :----------------: |:-----:|:-----:|:-----:|:-----:     |:-----:        |
|       Minimax      | 30.2  |1026.0 |37520.1|  1397012.5 |  53197610.7   |
|  Parallel Minimax  | 30.2  |1026.0 |37520.1|  1397012.5 |  53197610.7   |
|      Alphabeta     | 30.2  |391.1  |6793.4 |  78870.8   |  1033962.4    |
|      Jamboree      | 30.2  |418.4  |8250.8 |  101732.9  |  1586425.7    |

#### Conclusions ####
How close were your estimates to the actual values?  Did you find any
entry in the table surprising?  Based ONLY on this table, do you feel
like there is a substantial difference between the four algorithms?
<pre>
Our hypothesis was formulated while assuming our bot goes 3-ply deep. We predicted that for minimax, the bot will explore an order of magnitude of 10^4 game tree nodes. More specifically, we predicted that the bot will explore 35^3, or 42,875, game tree nodes, since 35 is the average branching factor for chess. As you can see from the table, for 3-ply, our estimation is very close for minimax, parallel and nonparallel. Our prediction for minimax and parallel minimax was the same number of nodes because both searchers have to explore the same amount of game tree nodes; the only difference is the parallel minimax can search the nodes faster since each node of the game tree can be run on independent threads. Our prediction was 42,875 game tree nodes for 3-ply and the actual number of game tree nodes explored was 37520.1, which is very close. The order of magnitude of game tree nodes of our prediction matched the order of magnitude in the actual values for 3-ply, 10^4. For alphabeta, we predicted that the bot will explore an order of magnitude of 10^3 game tree nodes. We were correct in this prediction since the actual order of magnitude of game tree nodes explored was 10^3, or 6,793.4, for 3-ply. 

We did not think anything in the table was surprising. Alphabeta explores the least amount of nodes, even less than jamboree, but this makes sense because alphabeta is not naturally parallelizable. If we attempt to parallelize the loop, we will be unable to propogate the new alpha and beta values to each iteration; therefore, this results in us having to evaluate unnecessary parts of the tree (do additional work). Jamboree's additional work consists of evaluating a certain % of the moves sequentially to get reasonable alpha/beta values that will enable us to cut out large parts of the tree. This explains why Jamboree explores more nodes than alphabeta. Even though jamboree explores more game tree nodes, the parallelism still gives us an overall benefit. We also noticed that at ply = 1, all algorithms explore the same amount of game tree nodes. This makes sense because alphabeta can't cut out parts of the subtree when the ply is only 1  
</pre>

### Optimizing Experiments ###
**THE EXPERIMENTS IN THIS SECTION WILL TAKE A LONG TIME TO RUN.**
To make this better, you should use Google Compute Engine:
* Google Compute Engine lets you spin up multiple instances. You should do this to run multiple experiments in parallel.
* **DO NOT** run multiple experiments in parallel on the same machine. This will lead to strange results.
* It's not strictly required to run experiments on multiple machines, but the write-up will take a lot longer if you don't do this.

#### Generating A Sample Of Games ####
Because chess games are very different at the beginning, middle,
and end, you should choose the starting board, a board around the middle
of a game, and a board about 5 moves from the end of the game.  The exact boards
you choose don't matter (although, you shouldn't choose a board already in
checkmate), but they should be different.

#### Sequential Cut-Offs ####
Experimentally determine the best sequential cut-off for both of your
parallel searchers.  You should test this at depth 5.  If you want it
to go more quickly, now is a good time to figure out Google Compute
Engine.   Plot your results and discuss which cut-offs work the best on each of
your three boards.

<pre> The table and the graph below shows the data for this experiment for the parallel minimax searcher. As you can tell, the best sequential cut-off at depth 5 for the parallel minimax searcher is 3 for all three board positions. This means that at the beginning, middle, and end board, the parallel minimax searcher could find the best move the fastest when the sequential cut-off was 3. 
</pre>
![](<fix1.png>)
![](<parallelCutoff.png>)

<pre> The table and the graph below shows the data for this experiment for the jamboree searcher. As you can tell, the best sequential cut-off at depth 5 for the jamboree searcher is also 3 for all three board positions. This means that at the beginning, middle, and end board, the jamboree searcher could find the best move the fastest when the sequential cut-off was 3. 
</pre>
![](<fix2.png>)
![](<fix.png>)

#### Number Of Processors ####
Now that you have found an optimal cut-off, you should find the optimal
number of processors. You MUST use Google Compute Engine for this
experiment. For the same three boards that you used in the previous 
experiment, at the same depth 5, using your optimal cut-offs, test Parallel Minimax and
Jamboree on a varying number of processors.  You shouldn't need to test all 32
options; instead, do a binary search to find the best number. You can tell the 
ForkJoin framework to only use k processors by giving an argument when
constructing the pool, e.g.,
```java
ForkJoinPool POOL = new ForkJoinPool(k);
```
Plot your results and discuss which number of processors works the best on each
of the three boards.
<pre>The table and the graph below shows the data for this experiment for the Jamboree searcher. As you can tell, the best number of processor at depth 5 and sequential cutoff = 3 for the  Jamboree searcher is 28 for early game, 19 for mid game and 24 for end game three. This means that at the beginning of the game the Jamboree searcher could find the best move fastest when there is 28 processors, at the mid of the game the Jamboree searcher could find the best move fastest when there is 19 processor and at the end of the game the Jamboree searcher could find the best move fastest when there is 24 processor</pre>
![](<Jamboree.png>)
![](<ProcessorJamboreeGraph.png>)

<pre>The table and the graph below shows the data for this experiment for the parallel minimax searcher. As you can tell, the best number of processor at depth 5 and sequential cutoff = 3 for the  parallel minimax searcher is 31 for early game, 32 for mid game and 27 for end game three. This means that at the beginning of the game the parallel minimax searcher could find the best move fastest when there is 31 processors, at the mid of the game the parallel minimax searcher could find the best move fastest when there is 32 processor and at the end of the game the parallel minimax searcher could find the best move fastest when there is 27 processor</pre>
![](<ProcesorParaTable.png>)
![](<ProcessParaGraph.png>)
<pre>As we can see from those graphs and data, There is actually no clear winner, When the processor in a certain range, the time is so close that the difference might be the experiment error </pre>

#### Comparing The Algorithms ####
Now that you have found an optimal cut-off and an optimal number of processors, 
you should compare the actual run times of your four implementations. You MUST
use Google Compute Engine for this experiment (Remember: when calculating
runtimes using *timing*, the machine matters).  At depth 5, using your optimal 
cut-offs and the optimal number of processors, time all four of your algorithms
for each of the three boards.

Plot your results and discuss anything surprising about your results here.
<pre>As we can see from the table the overall performance of Jamboree is the best which matches out expectation. There is one thing surprised me that in end game the parallel minimax has the best performance. I think that might because when there is just few choices left, the parallel minimax works even better than the Jamboree. </pre>

|      Algorithm     | Early Game | Mid Game | End Game |
| :----------------: |:----------:|:--------:|:--------:|
|       Minimax      |  5434.2    | 115660.1 |    67.6  |
|  Parallel Minimax  |   365.9    |  7732.1  |    5.8   |
|      Alphabeta     |   176.8    |  727.3   |    10.9  |
|      Jamboree      |    90.6    |  403.8   |    8.4   |


### Beating Traffic ###
In the last part of the project, you made a very small modification to your bot
to solve a new problem.  We'd like you to think a bit more about the 
formalization of the traffic problem as a graph in this question.  
- To use Minimax to solve this problem, we had to represent it as a game. In
  particular, the "states" of the game were "stretches of road" and the valid
  moves were choices of other adjacent "stretches of road".  The traffic and
  distance were factored in using the evaluation function.  If you wanted to use
  Dijkstra's Algorithm to solve this problem instead of Minimax, how would you
  formulate it as a graph?
  <pre>the node is each position. the edge between v1 and v2 is the direct path from the position that v1 represent to the position that v2 represent. The label of edge (v1,v2) is the distance from position v1 and v2 </pre>

- These two algorithms DO NOT optimize for the same thing.  (If they did,
  Dijkstra's is always faster; so, there would be no reason to ever use
  Minimax.)  Describe the difference in what each of the algorithms is
  optimizing for.  When will they output different paths?
  <pre>
  In the this situation, the Dijkstra optimize the absolute shortest path from start position to target position but the minmax optimize the best way to avoid as much traffic as possible when the total time spend is within a certain seconds.
  When the shortest path is not the path(within certain distance) that contains least traffic, they will output different paths. Let's look an example:
  For for the graph below, let the starting position be A and targeting position be E. The red number on the edge is the  distance between these two node and the blue number on the edge is the number of traffic by taking this road. 
  Using the Dijkstra, we will got the path A --> B--> D--> E since this is the path with shortest distance 
  Using the minmax, when choose acceptable distance is 80, the we will got the path A --> B --> C -->E since This is the path(within distance 80) with the least traffic.        
 </pre>
 ![](<graph.png>)