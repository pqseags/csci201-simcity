package UnitTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import UnitTests.BankUnitTests.RunBankTests;
import UnitTests.BusUnitTests.BusTest;
import UnitTests.HouseTests.RunHouseTests;
import UnitTests.MarketUnitTests.RunMarketTests;
import UnitTests.restaurantLindaUnitTests.RunRestaurantLindaTests;

@RunWith(Suite.class)
@SuiteClasses({RunBankTests.class,BusTest.class,RunHouseTests.class,RunMarketTests.class,RunRestaurantLindaTests.class})
public class RunAllTests {

}
