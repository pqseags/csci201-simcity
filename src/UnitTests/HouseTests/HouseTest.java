package UnitTests.HouseTests;

import UnitTests.mock.MockPerson;
import house.House;
import house.InhabitantRole;
import junit.framework.TestCase;

public class HouseTest extends TestCase {
	House house;
	UnitTests.mock.MockPerson p;
	InhabitantRole inhabitant;

	public void setUp() throws Exception{
		house=new House();
		p=new MockPerson();
		inhabitant = new InhabitantRole("inhabitant", p);
	}

	public void testPersonIn(){
		assertEquals("the  inhabitant role should be null. it is not.",house.room.inhabitant, null);
		house.msgImHome(inhabitant);
		assertEquals("the person pointer in the inhabitant role should be the person who get in. it is not.",house.room.inhabitant.self, p);


	}





}
