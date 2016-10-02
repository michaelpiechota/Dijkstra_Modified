Write a Graph.java class that performs the following:

Reads an input file named "input.txt" that has the following format:

Ac
cd
dT
dg
dA
Ab
Ag27
cT400

Lines consisting of two characters represent edges in the graph. Capital letters indicate Cities while lowercase represent towns.

The tax going into a city is the integer ceiling of 20% of the number of spoons you are entering with.

tax going into a town is simply 2 spoons.

Lines with an integer following are orders for spoons. The first letter is the city the salesman starts from and the second city is where the spoons are to be delivered. The integer is the number of spoons to be delivered.

The program should simply output:

ATbg45
cdgAT450

The list of letters is the path the salesman should travel. THe number is the spoons he is required to start with.