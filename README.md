# ProbabilisticChess
Android App Development by Robert Makepeace  \
Concept and Rules by Tom Riisfeldt (Intellectual Property of Tom Riisfeldt, 23/11/12) 

![image](https://github.com/robmakepeace/ProbabilisticChess/assets/11868424/86b24545-69ca-476c-baa1-7c81b29e725e) 

## Riisfeldt’s Probabilistic Chess.  Game Development Proposal.  

### Rules and Probability Ratios
The probability of piece X taking piece Y is  (Points (X))/(Points (X)+ Points (Y)).
For example, the probability of a queen taking a pawn would be 9/(9+1) = 9/10.  The probability of a pawn taking a queen would be 1/(1+9) = 1/10.  The probability of any piece taking an opponent’s corresponding piece is 1/2.  When the attacking player attempts to take a piece, a random probability generator determines whether or not he is successful in taking the piece.  The attacking player’s move finishes after attempting to take a piece, regardless of whether or not he is successful.  The king is worth 15 points.  Furthermore, the victory condition is not checkmate.  Rather, it is actually taking the king.  However, a player must still move his king when he is in check.  Therefore, to win, the attacking player must first checkmate the opposing king and then get lucky an actually take it.
This is the table of all of the probability ratios:

| Attacking Player (Top) vs. Defending Player (Left) |Pawn (1 point) | Bishop or Knight (3 points) |	Rook (5 points)	| Queen (9 points) | King (15 points) | 
| -------------------------------------------------	| ---	| ---	| ---	| ---	| ---	| 
| Pawn (1 point)	| $1 \over 2$	| $3 \over 4$ | $5 \over 6$	| $9 \over 10$	| $15 \over 16$| 
| Bishop or Knight (3 points)	| $1 \over 4$	| $1 \over 2$	| $5 \over 8$	| $3 \over 4$	| $5 \over 6$ | 
| Rook (5 points)	| $1 \over 6$	| $3 \over 8$	| $1 \over 2$	| $9 \over 14$	| $5 \over 8$ | 
| Queen (9 points)	| $1 \over 10$	| $1 \over 4$	| $5 \over 14$ | 	$1 \over 2$	| $5 \over 8$  | 
| King (15 points)	| $1 \over 16$	| $3 \over 16$ | $1 \over 4$| $3 \over 8$ | $1 \over 2$ | 

### Generating Random Integers to ‘Enact’ the Probabilities
First we need a common denominator for all of the probability ratios.
To find the LCM, find the integer factorisation of the denominators (2, 4, 6, 8, 10, 14, 16).
2 = 2\
4 = 22\
6 = 2 x 3\
8 = 23\
10 = 2 x 5\
14 = 2 x 7\
16 = 24\
So the LCM = 24 x 3 x 5 x 7 = 1680.\
The table of probability ratios can now be rewritten with each fraction having (the same) common denominator of 1680:\

| Attacking Player (Top) vs. Defending Player (Left) |Pawn (1 point) | Bishop or Knight (3 points) |	Rook (5 points)	| Queen (9 points) | King (15 points) | 
| -------------------------------------------------	| ---	| ---	| ---	| ---	| ---	| 
| Pawn (1 point)	| 840	| 1260 | 1400 | 1512	| 1575 | 
| Bishop or Knight (3 points)	| 420	| 840	| 1050	| 1260	| 1400 | 
| Rook (5 points)	| 280	| 630	| 840	| 1080	| 1260 | 
| Queen (9 points)	| 168	| 420	| 600 | 	840	| 1050  | 
| King (15 points)	| 105	| 315 | 420 | 630 | 840 | 

Now, random numbers can easily be generated to “enact” these probabilities using the random number generator at http://www.random.org/integers/
Set the page to generate 1 random integer between the integers 1 and 1680 (formatted in 1 column).  Now, for example, if my pawn was attempting to take my opponent’s bishop, I would set the page like this and then click “get numbers.”  On this occasion, I got 441.  But, since the probability ratio for taking a bishop with a pawn is 420/1680, only numbers 1-420 correspond to successfully taking the bishop, whilst numbers 421-1680 correspond to failing to take the bishop.  Since I got 441, I failed to take the bishop.  Alternatively, if I tried to take a knight with my rook, and in the random integer generator I got 983, I would successfully take the knight, since it lies within the range 1-1050.

### Future Initiatives
This is all of the rules at this point.  In the future I might simplify the probability ratios so that they are not true to the ratio of piece values, but are simple enough to be “enacted” using dice and coins, etc., thereby alleviating the requirement of a computer with a random integer generator.  However, I will probably attempt this after the game has been played a few times to see whether or not it works well and what improvements, if any, can be or need to be made.
