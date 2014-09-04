

This is a script for testing the Person Creation panel in the city's gui.

Here are a few different things to try:

#####Test 1 - Make sure the panel comes up

1. Run SimCityGui, the main panel should pop up, and there should be a button saying 
"Add Person". This will bring up the person creation panel. Push it.

2. A panel called "Person Creation Panel" should come up. It should include several
should include several fields such as "Employer","Place of Work","Shift Start","Shift End",
and "Housing". The first thing to test is that the panel actually comes up and displays
the necessary person creation fields.

#####Test 2 - Make sure the Person Creation Panel data is set properly

1. The person creation panel is supposed to give options having to do with work
that depend on the status of the city. First, open a new city, and open the person
creation panel.

2. Make sure, as before, the panel comes up. Make surethat if the city doesn't 
have any buildings, then under Employers, the only option is "None". Further, if
there are no houses in the map, make sure the only housing option is "None.

3. Close the panel

4. In the city, add several banks, markets, and restaurants. This should update
the city's internal data, but it should also update the gui's data. 

5. Open the person creation panel again.

6. This time, the Person creation panel should have as options for "Place of 
Employment" all of the buildings that were created and added to the map. If you
didn't add a particular place of work type (market, bank, restaurant), then this 
type of employment place should not appear in the selection list.

7. Verify that in the housing options list, the only option is still "None".

8. Close the panel.

9. Add a few houses - make it more than one. Make sure they are actually being
put down on the map. There should now be several houses and many places to work.

10. Open the person creation panel once more.

11. Verify that all of the employment options are still there that were there in 
the previous check (same numbers and types), and verify that there is a house 
option available for each house you added (i.e. added 5 houses, 5 houses in the
person creation panel). "None" should still be an option for both employment
and housing.

#####Test 3 - Make sure Person Creation list selection is being handled correctly

1. Create a new city. Open up the Person Creation Panel. There should be no
employment housing options.

2. Close the Panel.

3. Create several employment and housing options. Make sure there is at least one
of each of the following: Market, Bank, Restaurant. 

4. Open the panel again. Verify that the housing and employment options you added
in step 3 are shown in the "Employer" and "Housing" fields in the panel.

5. Click none. Nothing should happen to the job type field.

6. Click any of the other employers. The available jobs should be shown in the
job types panel. The available jobs should correspond to which jobs someone at 
that particular place of work would be able to take. The options for each employer 
should be
	+ Bank
		- Bank teller
	+ Market
		- Market Host
		- Market Cashier
		- Market Employee
		- Market Delivery Man
	+ Restaurant
		- Restaurant Host
		- Restaurant Cashier
		- Restaurant Waiter
		- Restaurant Cook
		
7. Verify that the above options are diplayed correctly for each place of 
employment.

8. Verify that clicking on a different "Employer" option changes the job type
view field.

9. Verify that clicking "None" for Employer clears the job types field.

#####Test 4 - Make sure the Person Creation panel prompt you to fill the form

1. Open a new SimCity, create a few places to work and a few houses.

2. Open the Person Creation Panel

3. Press the "Add" button at the bottom of the form.

4. The error field at the very bottom should say something like "Please enter
a name". It should not let you create without entering a name.

5. Enter a name. Press the add again.

6. The error field should now say something like "Please select a place of work/
employer". Again, you should not be allowed to go on without it.

7. Select an employer, but do not select a job type.

8. Press Add. This time it should not let you add the person, saying "Please select
a job type. 

9. Select a job type from the list of available options. For now, make sure the
employer is not "None". 

10. Add. It should then say "Please specify job shift start and end times". It should 
not let you add the person without this.

11. Add the start and end times. 

12. Press Add. It should then say "Please select a place to live". It should not have
created the person yet. 

13. Select a housing option.

14. Press Add. This time, it should create the person and the panel should close. 
Verify that no errors are shown in the console. 

15. Repeat the above procedure. This time, verify that if Employer is selected as
"None", the Shift start and end times do not need to be added in order to create
the person. If no start and end times are specified, the creation panel should
still close without complaint and create a new person with a "None" job.
	
#####Test 5 - verify that the close Person creation Panel button doesn't end the program

1. Open a new SimCity.

2. Open the person creation panel. 

3. Close it. Verify that the whole SimCity didn't close (this was happening before).
	
	
	
	
	
	
	