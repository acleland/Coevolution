CS 541 - Coevolution of Cellular Automata Project
Andrew Cleland
Dec. 5, 2016

** Project Summary **
-----------------------------------------------------------------------------------------------------
* My program is based on the algorithm described in Melanie Mitchel's paper "Coevolutionary Learning 
With Spatially Distributed Populations" (MitchellWCCI2006).

* A genetic algorithm is used to evolve a population of 1-dimensional Cellular Automata (CAs) to 
solve the density > 0.5 task. 

* CA states are 149-length 1-dimensional bit vectors.

* The CA correctly classifies an Initial Condition (IC) if it returns a state vector of all 1s 
if the IC had a majority 1s, and a vector of all 0s if the IC had a majority 0s.

* The program uses a coevolution strategy, where the CAs are treated has the host population,
and the ICs are treated as a parasite population that competes with CAs to give them challenging 
problems. 
______________________________________________________________________________________________________


** Algorithm Overview **
-----------------------------------------------------------------------------------------------------
* The populations are represented as 20x20 grid of (CA, IC) pairs. The grid uses periodic boundary
conditions, so the top row, is connected to the bottom row, and the left-most column is connected to
the right-most column. 

* Each generation, each CA in the grid is run on its own IC, and the 8 neighboring ICs. CA fitness is 
determined by the fraction of ICs it correctly classifies. 

* IC fitness is only computed with respect to its paired CA. IC fitness is 0 if the CA correctly 
classifies it, and |density - 1/2| otherwise.

* CA and IC parents are selected probabilistically based on its fitness value. See the proposal 
document for details.

* Crossover is performed with a specified probability on CAs with a random neighboring parent. No 
crossover is done on ICs.

* Both ICs and CAs are mutated according to a given probability.

* The program repeats for set number of iterations.
______________________________________________________________________________________________________


** Instructions for Compiling and Running **
---------------------------------------------
* Unzip the file
* Compile with javac *.java
* To modify parameters, edit constant values in Coevolution.java
* Run main program with java Coevolution

Each generation, the program will print the fitness values for each CA and IC in the grid.
Then it will select the highest fitness valued CA and show you a sample run of the CA with
a new random initial condition. The program will print each step of the CA's run, and indicate
if the final state gives a correct or incorrect classification. 
_____________________________________________



