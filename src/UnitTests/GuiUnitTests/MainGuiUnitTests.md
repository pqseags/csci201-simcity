This is a script to generally test the system's gui. It is not exhaustive, but it's a 
good start. It will mainly involve running scenarios and creating objects and observing
their behavior.

#### Test 0 - Main City Basics

1. Start SimCity. There should be the general square layout of the city, with no buildings
added and no people walking around. 

2. Clicking on the animation should not do anything - should not lead to any print statements.
should not create any objects, and should not change the right-hand animation panel.

#### Test 1 - Simple Pedestrian Movement

1. Start SimCity. There should be nothing on the map.

2. Open the Person Creation panel. Create a person called "Bus Rider" (no need to set any
parameters in the form except for name). Watch the person's movement. The pedestrian should 
walk to just shy of the bottom right corner of the inner island. They should generally move
in strait-line, constant speed segments. THE PERSON SHOULD ALWAYS STAY ON THE GREY SIDEWALK, 
EXCEPT FOR WHILE USING THE CROSSWALK. 

3. Once the pedestrian makes it to his destination, he should stop moving.

4. The pedestrian should stay in place and stay visible until the bus arrives to pick him up.

5. When the bus drops him off at the next stop, the person should leave the bus and walk,
in the same fashion, towards his house, enter, and disappear. 

6. If the scenario is repeated the person should walk, on different trials, at different 
locations on the sidewalk, since this was set randomly to allow many moving pedestrians to
fan out while they are walking.

##### Test 2 - Bus Function

1. Start SimCity. There should be nothing on the map.

2. Open the Person Creation panel. Create a person named "Bus Ride". The same scenario as last
time should result. Make sure the buses stop, start moving, progress to the next stop, and continue
in a regular manner. Also make sure that the buses don't jump from one corner to another (obviously).

3. When the person is waiting at the stop and the bus arrives, the person should disappear. They
should not reappear until the bus makes it to its next stop and drops off its passengers. 

4. Previously, the bus would continue to drop off the same people every time it stopped. 
Verify that it only drops its passenger off once, rather than letting the passenger out
many times. 

5. The print statements will support the test - when the bus arrives at the person's bus stop,
the person should disappear and a print statement should say that the person is getting on the bus.
When they disembark from the bus, it should say they are getting off the bus in the print
statements.

##### Test 3 - Bank Function

1. Start SimCity. There should be nothing on the map.

2. Open the Person Creation panel. Create a person name "Bank". The banking scenario should
result - on teller and several customers. They should walk to the bank to do business.

3. Verify that the pedestrians head for the bank, and that, at some point, they fan out
as usual.

4. Once they reach the bank, they should disappear. 

5. To see inside the bank while they work, simply click on the bank's picture in the city view.

6. Inside the bank, there should be a teller behind the desk and there should be customers 
approaching the teller to do business. Once the customers finish what they need to do, THEY
WILL GET BACK IN LINE, SINCE THEY HAVE ANOTHER BANK TASK TO DO. Thus, each gui will approach
a teller twice. If possible, verify that this is the case. 

7. Once the banking tasks are finished, the bank customer icons in the bank view should move
towards the left to leave. Once they reach the boundary, they should exit and their corresponding
person icon should appear on the main map. THIS STEP IS VERY IMPORTANT.

8. The person that left should then walk in normal fashion toward their house.

##### Test 4 - Market Function

1. Start SimCity. There should be nothing on the map.

2. Open the Person Creation panel. Create a person name "Market". The market scenario should
result - many market staffers and many customers. They should walk to the market to do business.

3. Verify that the pedestrians head for the market, and that, at some point, they fan out
as usual.

4. Once they reach the market, they should disappear. 

5. To see inside the market while they work, simply click on the market's picture in the city view.

6. Inside the market, there should be several market staffers behind the desk and there should 
be customers approaching them to do business. Once the customers finish telling the host their order,
they will move to the loading station, where their orders will be handed off to them. After that, they 
should move to the payment station where their bill should be computed. After paying, they will leave.
Verify that on their way out and in, the sliding doors to the building are responsive.

7. Once a customer's market tasks are finished, the market customer icons in the market view should 
move towards the left to leave. Once they reach the boundary, they should exit and their 
corresponding person icon should appear on the main map. THIS STEP IS VERY IMPORTANT.

8. The person that left should then walk in normal fashion toward their house.

##### Test 5 - House Function

1. Start SimCity.

2. After almost any testing scenario, the person will want to sleep at home. IMPORTANT: 
IN ORDER TO GO HOME TO SLEEP, THE PERSON MUST HAVE THEIR HOME SET. IF YOU CREATE YOUR OWN 
PERSON AND SET HIS HOME AS 'None' THEN HE WILL SLEEP AT THE HOMELESS PERSON AREA IN THE TOP
LEFT CORNER OF THE ANIMATION PANEL.

3. Once the person reaches the home, their icon should disappear and appear in the right-hand
zoom view of the house. They should go to their bed and sleep. Verify that this happens. When/
if they decide to leave the house to do something else, the person should move towards the left
in the zoom view, and eventually disappear from zoom and appear on the city view. THIS IS VERY 
IMPORTANT.
