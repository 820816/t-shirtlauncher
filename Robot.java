 /*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/                                                                                                                                 


package frc.robot;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive
 * class.
 */
public class Robot extends TimedRobot {
  private static final int kFrontLeftChannel = 2;
  private static final int kRearLeftChannel = 3;
  private static final int kFrontRightChannel = 1;
  private static final int kRearRightChannel = 0;
  private static final int kJoystickChannel = 0;
  

  private Spark m_frontLeft;
  private Spark m_frontRight;
  private Victor m_rearLeft;
  private Spark m_rearRight;
  private MecanumDrive m_robotDrive;
  private Joystick m_stick;
  private JoystickButton toggleButton, led1, led2, led3;
  private double ledStyle = 0;
  private DoubleSolenoid launchersolenoid;
  private DigitalInput getPressureSwitchValue;
  private Relay compressorSwitch;
  private Spark m_led_driver;
  private Talon feeder;
  private JoystickButton feederbutton; 

/**DeadBand Code */
  private static double linearDeadband(double raw, double deadband)
  {
 
     if (Math.abs(raw)<deadband) return 0;
 
    return Math.signum(raw)*(Math.abs(raw)-deadband)/(1-deadband);
  }


/**Innit */
  @Override
  public void robotInit() {
    
    m_frontLeft = new Spark(0);	
    m_rearLeft = new Victor(4);
    m_frontRight = new Spark(2);	
    m_rearRight = new Spark(3);
    m_led_driver = new Spark(5);
    m_robotDrive = new MecanumDrive(m_frontLeft, m_rearLeft, m_frontRight, m_rearRight);
    m_stick = new Joystick(kJoystickChannel);
    toggleButton = new JoystickButton(m_stick, 1);
    led1 = new JoystickButton(m_stick, 2);
    led2 = new JoystickButton(m_stick, 3);
    led3 = new JoystickButton(m_stick, 4);
    launchersolenoid = new DoubleSolenoid(1,0);
    getPressureSwitchValue = new DigitalInput(2);
    compressorSwitch = new Relay(1);
    feeder = new Talon(6);
    feederbutton = new JoystickButton(m_stick,5);
    

    // Invert the left side motors.
    // You may need to change or remove this to match your robot.
    m_frontLeft.setInverted(false);
    m_rearLeft.setInverted(true);
   //m_robotDrive.setDeadband(0.05);

  }
  //Override

/**Teleop Period */
public void teleopPeriodic() {
    // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.
    m_robotDrive.driveCartesian(m_stick.getZ(), m_stick.getY(), m_stick.getX(), 0.0);

    ledFunction();
    feeder();
    launch();
    
  }


  /** This is the function that checks buttons and sets the LEDs accordingly */
  private void ledFunction() {
    if (led1.get() && ledStyle != .63 /* This is some value */) ledStyle = .63;
    else if (led1.get()) ledStyle = 0;
    if (led2.get() && ledStyle != -.11/* This is some value */) ledStyle = -.11;
    else if (led2.get()) ledStyle = 0;
    if (led3.get() && ledStyle != .45/* This is some value */) ledStyle = .45;
    else if (led3.get()) ledStyle = 0;

    m_led_driver.set(ledStyle);

    
  }

/**This function controls the feeder belt */
  private void feeder() {
    if (feederbutton.get()) {
      feeder.set(.5);
    } else {
      feeder.set(0);
    }
  }

/**This function controls the launch mechananism  */
  private void launch() {
    if (toggleButton.get()) {
    launchersolenoid.set(DoubleSolenoid.Value.kForward);
   } else {
    launchersolenoid.set(DoubleSolenoid.Value.kReverse);
    }
  
  }

}

