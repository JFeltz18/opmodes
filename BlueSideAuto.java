package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import java.io.InterruptedIOException;
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
public class BlueSideAuto extends LinearOpMode
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
    @Override
    public void runOpMode() throws InterruptedException
    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.
        leftScissorsMotor = hardwareMap.dcMotor.get("leftScissorsMotor");
        rightScissorsMotor = hardwareMap.dcMotor.get("rightScissorsMotor");

        debrisCollectMotor = hardwareMap.dcMotor.get("debrisCollectMotor");
        rightDriveMotor = hardwareMap.dcMotor.get("rightDriveMotor");
        leftDriveMotor = hardwareMap.dcMotor.get("leftDriveMotor");
        dumpMotor = hardwareMap.dcMotor.get("dumpMotor");
        sensorGyro = hardwareMap.gyroSensor.get("gyro");

        rightScissorsMotor.setDirection(DcMotor.Direction.REVERSE);
        leftScissorsMotor.setDirection(DcMotor.Direction.FORWARD);
        rightDriveMotor.setDirection(DcMotor.Direction.REVERSE);
        leftDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        debrisCollectMotor.setDirection(DcMotor.Direction.REVERSE);

        dumpMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightDriveMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        sensorGyro.calibrate();


        while (sensorGyro.isCalibrating())
        {
            super.sleep(50);
        }

        dumpMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightDriveMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        waitForStart();

        recursiveAuto(0);
    }


    public void recursiveAuto(int step) throws InterruptedException
    {

        if (step == 0)
        {
            while (rightDriveMotor.getCurrentPosition() < 3000)
            {
                rightDriveMotor.setPower(0.29);
                leftDriveMotor.setPower(0.5);
            }
            rightDriveMotor.setPower(0.0);
            leftDriveMotor.setPower(0.0);
            recursiveAuto(step + 1);
        }
        if (step == 1)
        {
           while (opModeIsActive())
           {
               telemetry.addData("Gyro Heading", sensorGyro.getHeading());
           }
        }

    }
} // PushBotAuto
