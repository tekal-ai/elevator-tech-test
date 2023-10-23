# Memorable Technical Test: build an elevator system

In this exercise, you are going to design an elevator system for a skyscraper with 100 floors. The goal is to minimize the amount of time spent between calling an elevator and arriving at the destination floor. If any portion of this project’s requirements is relaxed, please include in your solution which requirements they are with a brief description for why they couldn’t be included. 

## Part A: 

In a programming language of your choice, implement one or more functions that takes as input a time series of elevator calls and destinations and outputs a time series of elevator actions. Given constraints: 
 
 * There are 3 elevator shafts. 
 * The destination floor is known at the time of the elevator call. 
 * There is a lobby on the 1st floor. 
 * It takes 1 second for the elevator to move 1 floor. 
 * It takes 5 seconds to open the elevator door to pick up / drop-off a passenger. This time is 30 seconds for the lobby floor. 
 * A maximum of 10 people can fit into the elevator car at any one time. 
 

## Part B: 

Implement a simulator that generates the time series of elevator calls to feed the function in part A. It should use the following assumptions when generating the inputs: 

Except for the lobby, all other floors have a uniform distribution of number and frequency of calls. The number of passengers per call is random according to a lognormal distribution, rounded to the nearest integer in the range (0, 5]. The random functions should be seeded in such a way that the results of any run can be reproduced if the same seed is used. 
 
After the simulator runs, it should produce summary statistics that describe: 

 * The average time spent waiting for an elevator 
 * The average time spent inside an elevator 
 * The average total time spent 
 

## How to deliver your solution:
1. You should fork this repository and invite the following collaborators:
- @cfosco
- @GonzaloGregorio
- @diegomedinacastillo
- @andrescanabarro

2. Commit your solution to your fork, and send us an email when you're done.
3. Your fork should contain a markdown document (you can overwrite this README) with an explanation of your solution. Explain any technical decisions, considerations and additional constraints you took into account.
4. Include detailed instructions on how to build and run the application. Any automation you can add here will be greatly appreciated. 


**Additionally**

Engineers at Memorable have a collaborative culture, and we often discuss projects across teams. Consider yourself our colleague on this project, and please don’t hesitate to reach out to us for clarification on any requirements or guidance if you’re having trouble. In other words: please ask questions. 

We are looking for high quality work, so we would rather see something that is functionally incomplete but well-designed and tested than something that is of lower quality. Please expect that each decision may be questioned by any engineer in the review discussion and design accordingly. You should expect to actually demo your solution. 






