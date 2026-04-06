package hooks;

import driverManager.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {


    @Before
    public void initDriverManager() {
        DriverManager.initDriverManager();
    }


//    @After
//    public void closeDriverManager() {
//        DriverManager.quitDriverManager();
//    }
//

}
