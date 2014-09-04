
team35
======

# SimCity201 Project Repository for CS 201 students

## How to Run our SimCity
Run cityGui.SimCityGui. This is our main application. To run tests, run UnitTests.RunAllTests.Java. The tests can also be run individually. They can be found under UnitTests. 


##v2 - Grading Scenarios and How to Run Them

Overview - for the most part, scenarios are run by starting SimCityGui and selecting a 
scenario from the scenario selection panel - the list panel on the bottom right. Simply
click the name of a scenario and it will instantiate the appropriate variables and run the
scenario. This is where most of the grading will take place, we expect, since the important
behaviors are isolated in each grading scenario, making it easy to see what's going on. 

###A. Big City
Creates scenario A as described in the grading rubric - a big city with each type of 
building included, with people visiting each type of workplace to do business of different
kinds. To our knowledge, this is running quite smoothly

###B. Big City
Creates scenario B as described in the grading rubric - same as A, a big city with each
type of building included, and people displaying different transportation methods (at least 
one person will show walking, two will take a bus ride, and two will drive cars). The 
transportation in the demo is meant to display the functionality, so we didn't tell tons 
of people to take the bus or drive - adding on more people, both functionalities are much 
the same.

###C. Market Delivery
Creates scenario C as described in the grading rubric - Restaurant gets low on food, orders 
from Market, who delivers the food. This is kept simply so you can see what's going on without 
distracting activity in the rest of the city.

###E. Simple Bus Ride
Creates scenario E as described in the grading rubric - simple demonstration of the bus
functionality. Two people will try to get on the buses at different bus stops, and then will
get off at their end-stops. Afterward, they will go home. 

###F. Closing Buildings
Creates a large city and has the city control panels to close the buildings. If the building is
closed, no PersonAgent will schedule a new action there. If, however, the person was already
on his way, he'll continue. So, push close on a market, and wait. After all the customers leave,
no others will enter. If you open at an early enough moment, you'll see that persons do schedule
business at the market again. 

###G. Market Delivery
Sets up multiple restaurants and a market. In this scenario, the market delivery fails because 
restaurant is closed, so the market redelivers when restaurant is open. The user must manually click on each of the restaurants to "Open" them, after the cook has made the order. The market then 
sends an invoice to cashier, who verifies and pays it.

###J. Big City
Creates scenario J as described in the grading rubric - Big city demo with everything added.
There are cars, buses, and buildings, which are fully staffed. You can see people going to 
work, leaving work, choosing a building to visit, going in, doing business, leaving, and 
repeating the process. As far as we know, J operates without problems.

###O. Bank Robber
Creates scenario O as roughly outlined in the grading rubric - Uses a hack to create a bank
robber, who decides to go to a bank for a robbery. When he gets there, a fire fight will
start and the winner of the fight will be determined based on the combat levels of the 
teller and the robber, luck, and their weapon enchants (no, just kidding - randomly). If the
robber wins, the teller will hand over money and the robber will leave. If the teller wins,
the robber will be deleted from existence, and the world will carry on as if the robber never
existed. 

###P/Q. Vehicle Collisions
These scenarios were not fully completed. Currently, pressing one of these buttons begins a rainstorm, which increases the chances of a collision. However, the actual collisions have not been implemented. 

###R. Weekend Buildings Closed
This will close all buildings, so that no customers will enter. Workers, however, will still
enter. You can see everyone go home. Then, if you click on a building, in its control panel,
there'll be a button to open the building. When you do so, customers will leave their houses
to visit the building.

###Simple Market
Displays the simple market behavior - customers come, order items, and leave. 

## Individual Contributions

#### Bobby Groom

+ Worked on bank animation.
+ Helped design bank.
+ The GUIs.(bank, btr, bcr).
+ Integrating the building GUIs with the SimCity zoom panel.
+ Connecting the bank animations to the personAgent.
+ Designed and coded the control panel for the bank
+ Updated the unit tests in bank as the project developed
+ Worked on a design to display the animation of different buildings on the main screen
	- Ended up using Simon's design

#### Gabriel Mel de Fontenay

- Co-designed the bank (with rgroom)
- Coded the Bank (non-animation)
- Wrote the unit tests for the bank
- Co-designed the bus system (with pqseags)
- Coded the transportation system
- Coded the bus system (non-animation)
- Unit tested the bus system
- Fully in charge of integration of agent code (except restaurant) in city (v1)
- Designed/coded cars (pretty minor)
- Co-designed/Co-wrote much of the Person (with yocca)
- Wrote the Person creation panel
- Wrote some of the early grading scenarios (bank, market,my restaurant, addBuilding, addPerson, addBuses)
- Helped implement CityCard animation (Cards were not being animated while not looked at - I fixed this)
- A* in the main city
- Restaurant Integrated
	+ Including market integration and shared data
	+ Including unit tests of new shared data and market interaction
- Fully in charge of transportation
	+ Buses redesigned/recoded with collision avoidance
	+ Intersections with stop lights
	+ Cars completely coded, including collision avoidance
	+ Pedestrian behavior modified for collision avoidance
	+ Upgraded animations for transportation
- Helped design/create the Scenario Selection Panel
	+ Though my implementation was not used in the end
- Helped add grading scenarios
	+ O. BankRobbery, E. BusRide, F. Closing, R. Weekends
	+ Helped Andrew debug A,B, J big city scenarios
- Implemented building opening/closing functionality
	+ Included PersonAgent decisions not to go to closed buildings

#### Simon (Fangli) Hou

+ Co-designed the Person with yocca
+ Modified and improved PersonAgent codes
+ Fully coded the House and House animation(CityHouseCard.java)
+ Fully coded the InhabitantRole and InhabitantGui
+ Unit Tested the InhabitantRole
+ Co-designed the CityAnimation with yocca
+ Designed the method to integrate the Animation of building into the the main window by using designing CityXXXCard.java
+ Helped all teammates to integrate their building animations into the main window
+ Designed the method to keep individual animation panels running in the background
+ Created / added to multiple interfaces
+ Kept designs simple and efficient by clearing everyone’s redundant codes
+ Make the main control panel able to add different types of Restaurants from different buttons(All connected!)
+ Created buildings Control Panels for House, Bank, and everyone's Restaurants, and integrated them into the main frame
+ Integrated my Restaurant, and it connected with other buildings and have shifts

#### Parker Seagren
+ Co-designed the Market roles (with Linda Xu)
+ Co-implemented the Market roles (with Linda Xu)
+ Fully designed and coded the market animation
+ Fully designed and coded the market panel
+ Updated all market roles to cohere with animation and city
+ Unit tested market roles (with help from rgroom)
+ Co-designed transportation (with Gabriel)
+ Integrated the market animation, panel, and guis into the main city gui
+ Helped the rest of the team to suppress bugs and compiler errors
+ designed the layout for the main frame. condensed all of the multiple windows into a single frame
+ set up city control panel with scroll panels for choosing buildings or scenarios
+ created market control panel
+ set up integration of building control panels, integrated market control panel
+ updated market to deal with delivering to restaurants (with Linda)
+ built market scenarios
+ updated individual restaurants to work with restaurant deliveries (with Linda)
+ helped with integration of building panels
+ began initial work on vehicle collision scenarios (rain)
+ built capability and scenario for market delivering to closed restaurants


#### Andrew Yocca
+ Designed and coded the City Gui with the exception of the bus system and cars
+ Created the ability to see invalid building placements and to see the correct orientation of buildings based on where its able to be placed
+ Coded the original person movement in the City Gui (replaced with astar by Gabe)
+ Implemented sprites into the city gui, market, bank, restaurant, and house
+ Coded the person agent and designed the person scheduler
	- Gabe made a lot of additions to make it work properly and to test it
+ Created and wrote unit test for the Person Agent
+ Integrated and updated my own restaurant into the city
+ Designed, coded, and tested Scenario A
+ Designed, coded, and tested Scenario B
+ Designed, coded, and tested Scenario J
+ Designed, coded, and tested Scenario K
	- gave people ID numbers that float above their sprites in the CityGui
	- gave people ID numbers and a job string that floats above their sprites in the CityCards

#### Linda Xu
+ Integrated own restaurant into the city, with updates
+ Implemented some amount of AStar to own restaurant (only applies to structures, people may still walk on top of each other)
+ Co-designed/implemented Market roles with Parker Seagren
+ Integrated Restaurant and generalized the integration method for other members' restaurants
	- includes base classes, super classes, and interfaces
+ Helped integrate control panels for everyone's restaurants using Simon's Building Control Panel super class
+ Coded parts of the market unit tests, particularly MarketDeliveryMan
	- Also cleaned up a lot of unit tests to get rid of compilation errors and packaged all the tests into a single test class
+ Implemented the trace panel into the project and added a method to filter according to the current building being shown.
+ Added methods to log to the trace panel in several role classes, particularly those belonging to market and restaurant.
+ Helped team integrate and debug stuff, mostly concentration on market and restaurant issues


## Known Bugs/Missing Functionality
+ Vehicle crashing scenarios are not completed. Currently they just start a rainstorm, but the collisions themselves have not been implemented.
+ Some restaurants are not fully integrated, mainly the market interaction


## v1 - Some Basic Scenarios to Demonstrate Functionality for Grading
NOTES: 
- These scenarios will all be very simple, since they are meant to demonstrate particular
functionalities of SimCity - one at a time. It'd be quite difficult to see through the print
statements what was going on if many scenarios were running at once, so these grading aids
make it easier by doing one scenario at a time. To see lots of things happening at once (at 
the cost of clarity) call the last scenario (#5).
- Most scenarios will end in the person sleeping.

### 1. Buses
Start SimCity, and create a new person. Enter the name as "Bus Ride". This will add the
bus system to the city, and will create a single person name "Rider". Rider will choose to
Rider will choose to go to the bus stop, ride the bus, and go home. In this particular case,
riding the bus is out of the way for Rider, but this is just because we are simply trying to
demonstrate the bus system's functionality.

### 2. Bank
Start SimCity and open the Person Creation panel. Enter the name as "Bank". This will create several
people and will give them all the job of Bank Teller, but with different shifts. It will
add a bank to the city. When everything is created, all new people will go to the bank,
one to start his shift, and the others to do business. To see inside the bank, click on the
bank in the main city view. As you'll see, soon after the scenario starts, the first teller
will get off work and help the others with their remaining business, and the same will happen
for up to the last teller as well. The banking can be seen in the animation or more specifically in
the print statements. Afterwards, they all go home to sleep.

### 3. Market
Start SimCity and open the Person Creation panel. Enter the name as "Market". This will create
a market and people, who will staff and buy from the market. 3 people will 
work during the shift, and the others will do business with the market staffers. To see
the animation, click on the market in the city view. Also, see the print statements, which 
detail the market purchases. The people will go hom after they have finished all business. 

### 4. Busy 
Start SimCity, and open the Person Creation panel. Enter the name as "Busy". This will create
several buildings and enough people to staff them. The buses will also be added. Some people will
go to work at the buildings, some will go to the bank to do business, others will board the 
bus. Click on any building to see what's inside. People will go home to sleep as the default
behavior.

### 5. House
Start SimCity(run the SimCityGui.java), and click “Add House” button. Place the image on the green and left click. 
Each house contains inhabitant(s) and inventory.
Click on the house to view inside: bed (black), table (cyan), refrigerator (white), grill (gray). It also show the control panel, where you can see and modify the inventory.
People can go to the house to sleep and eat.
	-A Star not implemented in House
	
### 6.Restaurant
Test individual restaurants by using the "Add Person" button, and type in "RestaurantXxxx". For example, "RestaurantLinda","RestaurantSimon","RestaurantYocca","RestaurantGabe","RestaurantPaker".
Ps: if customer do not go to the restaurant, manually add people with no jobs, until one of them decide to go to the restaurant.
