package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import java.util.Calendar;
//------------------------------------------------------------------------------
//
// PushBotAuto
//

/**
 * Provide a basic autonomous operational mode that uses the left and right
 * drive motors and associated encoders implemented using a state machine for
 * the Push Bot.
 *
 * @author SSI Robotics
 * @version 2015-08-01-06-01
 */
public class BlueSideAuto extends PushBotAuto
{

    DcMotor rightScissorsMotor;
    DcMotor leftScissorsMotor;
    DcMotor rightDriveMotor;
    DcMotor leftDriveMotor;
    DcMotor debrisCollectMotor;
    DcMotor dumpMotor;
    GyroSensor sensorGyro;

    int xVal, yVal, zVal = 0;
    int heading = 0;

    //--------------------------------------------------------------------------
    //
    // PushBotAuto
    //
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public BlueSideAuto() throws InterruptedException
    {
        rightScissorsMotor = hardwareMap.dcMotor.get("rightScissorsMotor");
        leftScissorsMotor = hardwareMap.dcMotor.get("leftScissorsMotor");
        debrisCollectMotor = hardwareMap.dcMotor.get("debrisCollectMotor");
        rightDriveMotor = hardwareMap.dcMotor.get("rightDriveMotor");
        leftDriveMotor = hardwareMap.dcMotor.get("leftDriveMotor");
        dumpMotor = hardwareMap.dcMotor.get("dumpMotor");
        sensorGyro = hardwareMap.gyroSensor.get("gyro");

        dumpMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightScissorsMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);


        rightScissorsMotor.setDirection(DcMotor.Direction.FORWARD);
        leftScissorsMotor.setDirection(DcMotor.Direction.REVERSE);
        rightDriveMotor.setDirection(DcMotor.Direction.REVERSE);
        leftDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        debrisCollectMotor.setDirection(DcMotor.Direction.REVERSE);
        dumpMotor.setDirection(DcMotor.Direction.REVERSE);

        while (sensorGyro.isCalibrating())
        {
            Thread.sleep(50);
        }
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.


    } // PushBotAuto

    public void init()
    {
    }
    //--------------------------j------------------------------------------------
    //
    // start
    //
    /**
     * Perform any actions that are necessary when the OpMode is enabled.
     *
     * The system calls this member once when the OpMode is enabled.
     */
    @Override public void start()
    {
        super.start();


        //
        // Reset the motor encoders on the drive wheels.
        //
        rightDriveMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        recursiveAuto(0);

    } // start


    //
    // loop
    //
    /**
     * Implement a state machine that controls the robot during auto-operation.
     * The state machine uses a class member and encoder input to transition
     * between states.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override public void loop ()
    {

    } // loop

    //--------------------------------------------------------------------------
    //
    // v_state
    //
    /**
     * This class member remembers which state is currently active.  When the
     * start method is called, the state will be initialized (0).  When the loop
     * starts, the state will change from initialize to state_1.  When state_1
     * actions are complete, the state will change to state_2.  This implements
     * a state machine for the loop method.
     */


    public void recursiveAuto(int step)
    {
        debrisCollectMotor.setPower(1.0);
        if (step == 0)
        {
            rightDriveMotor.setTargetPosition(1000);
            while (rightDriveMotor.getTargetPosition() > rightDriveMotor.getCurrentPosition())
            {
                rightDriveMotor.setPower(0.5);
                leftDriveMotor.setPower(0.5);
            }
            rightDriveMotor.setPower(0.0);
            leftDriveMotor.setPower(0.0);
            rightDriveMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            recursiveAuto(step++);
            return;
        }

        if (step == 2)
        {

        }
    }
} // PushBotAuto
