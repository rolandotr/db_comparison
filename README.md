db_comparison
=============

Copyright 2014 Gildas Avoine and Rolando Trujillo Rasua
-----------------

This java project is aimed at comparing distance bounding protocols. It contains implementations of all, 
or almost all, DB protocols proposed up-to-date. The flexible design and the methodology used to compare 
DB protocols make it a great tool for distance bounding protocols designers. More information can be 
obtained from our paper [1].

[1] Comparing distance bounding protocols: A critical mission supported by decision theory. G. Avoine, S. Mauw and R. Trujillo-Rasua. Computer Communications, 67:92–102, 2015 

Contact address:
Rolando Trujillo Rasua
Universit\'e du Luxembourg 
Facult\'e des Sciences, de la Technologie et de la Communication
6, rue Richard Coudenhove-Kalergi
L-1359 Luxembourg
Email: rolando.trujillo@uni.lu
Updates via: https://github.com/rolandotr/db_comparison

FAQs
====

- How can I change the set of protocols to be compared? -

The static method DBProtocol.loadProtocolsFairly() is used to load the set of protocols to compare with each other, which calls the method loadProtocols() within the DBProtocol class. The latter can be modified or re-defined in order to change the set of considered protocols.

It is worth remarking that the method DBProtocol.loadProtocolsFairly() instead of a set return a two-dimensional matrix where protocol instances are ordered in terms of the number of bits exchanged during the fast phase (hence the suffix in the method's name "fairly").

- How can I change the maximum number of rounds considered in the experiments? - 

A static and public variable called MAX_N can be used for that purpose. 

- How can I run the methodology as defined in the paper "Comparing distance bounding protocols: A critical mission supported by decision theory" -

A main method (named createComparisonTable) within the class methodology.Evolution can be used for that purpose. 
	- This method first uses the static method DBProtocol.loadProtocolsFairly() in order load the set of protocols to be compared. 
	- Then the method "constraintProtocols" is used to only consider those protocols whose resistance to mafia fraud is upper bounded by given threshold. Note that, in the
	corresponding scientific article this is used to generate Table 4. The analyst can define as many thresholds as needed.
	- Next, the set of considered attributes is defined. To understand the notion of approximate equality and scales see the corresponding scientific article.
	- The method ParetoFrontier.computeAllParetoFrontiers computes the pareto frontiers, while Evolution.printLatexTable provides a latex table with the result. It should be remarked that this is a very basic table, which needs to be refactored in order to look like the one in the paper.
	
*** Should you have any question, don't hesitate to contact me at rolando.trujillo@uni.lu ****
