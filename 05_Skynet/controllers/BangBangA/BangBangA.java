// File:          BangBangA.java
// Date:          
// Description:   
// Author:        
// Modifications: 

// You may need to add other webots classes such as
//  import com.cyberbotics.webots.controller.DistanceSensor;
//  import com.cyberbotics.webots.controller.LED;
// or more simply:
//  import com.cyberbotics.webots.controller.*;
import com.cyberbotics.webots.controller.DifferentialWheels;

// Here is the main class of your controller.
// This class defines how to initialize and how to run your controller.
// Note that this class derives Robot and so inherits all its functions
public class BangBangA extends DifferentialWheels {
  
	private static int MAX_SPEED = 1000; // max. motor speed
	
  // You may need to define your own functions or variables, like
  //  private LED led;
  
  // BangBangA constructor
  public BangBangA() {
      
    // call the Robot constructor
    super();
    
    // You should insert a getDevice-like function in order to get the
    // instance of a device of the robot. Something like:
    //  led = getLED("ledName");
    
  }
    
  // User defined function for initializing and running
  // the BangBangA class
  public void run() {
    
    // Main loop:
    // Perform simulation steps of 64 milliseconds
    // and leave the loop when the simulation is over
    while (step(64) != -1) {
    	System.out.println("This is awesome!");
    	setSpeed(MAX_SPEED, MAX_SPEED);
      // Read the sensors:
      // Enter here functions to read sensor data, like:
      //  double val = distanceSensor.getValue();
      
      // Process sensor data here
      
      // Enter here functions to send actuator commands, like:
      //  led.set(1);
    };
    
    // Enter here exit cleanup code
  }

  // This is the main program of your controller.
  // It creates an instance of your Robot subclass, launches its
  // function(s) and destroys it at the end of the execution.
  // Note that only one instance of Robot should be created in
  // a controller program.
  // The arguments of the main function can be specified by the
  // "controllerArgs" field of the Robot node
  public static void main(String[] args) {
    BangBangA controller = new BangBangA();
    controller.run();
  }
}
