package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.hardware.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
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
public abstract class BaseAuto extends LinearOpMode
{

    DcMotor rightScissorsMotor;
    DcMotor leftScissorsMotor;
    DcMotor rightDriveMotor;
    DcMotor leftDriveMotor;
    DcMotor debrisCollectMotor;
    DcMotor dumpMotor;
    ModernRoboticsI2cGyro sensorGyro;

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
        sensorGyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");

        rightScissorsMotor.setDirection(DcMotor.Direction.REVERSE);
        leftScissorsMotor.setDirection(DcMotor.Direction.FORWARD);
        rightDriveMotor.setDirection(DcMotor.Direction.REVERSE);
        leftDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        debrisCollectMotor.setDirection(DcMotor.Direction.REVERSE);
        dumpMotor.setDirection(DcMotor.Direction.REVERSE);


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
    }

    public abstract void recursiveAuto(int step) throws InterruptedException;

    public final void travel(int encoderCounts, int originalEncoderCount, TravelDirection d) throws InterruptedException
    {
        if (d == TravelDirection.FORWARD)
        {
            while (((rightDriveMotor.getCurrentPosition() * -1) - originalEncoderCount) < encoderCounts)
            {
                rightDriveMotor.setPower(1.0);
                leftDriveMotor.setPower(1.0);
                telemetry.addData("encoder position", -1 * rightDriveMotor.getCurrentPosition());
                waitOneFullHardwareCycle();
            }
            telemetry.clearData();
            rightDriveMotor.setPower(0.0);
            leftDriveMotor.setPower(0.0);
        }
        else
        {
            while (((rightDriveMotor.getCurrentPosition() * -1) - originalEncoderCount) > encoderCounts)
            {
                rightDriveMotor.setPower(-1.0);
                leftDriveMotor.setPower(-1.0);
                telemetry.addData("encoder position", -1 * rightDriveMotor.getCurrentPosition());
                waitOneFullHardwareCycle();
            }
            telemetry.clearData();
            rightDriveMotor.setPower(0.0);
            leftDriveMotor.setPower(0.0);
        }
        rightDriveMotor.setPower(0.0);
        leftDriveMotor.setPower(0.0);

    }

    public final void rotate(int turnAngle, int originalTotalRotation, TurnDirection d) throws InterruptedException
    {
        if (d == TurnDirection.CLOCKWISE)
        {
            while (sensorGyro.getIntegratedZValue() < (originalTotalRotation + turnAngle))
            {
                rightDriveMotor.setPower(-0.4675);
                leftDriveMotor.setPower(0.1);
                telemetry.addData("Gyro Position", sensorGyro.getIntegratedZValue());
                waitOneFullHardwareCycle();
            }
        }
        else
        {
            while (sensorGyro.getIntegratedZValue() > (originalTotalRotation - turnAngle))
            {
                rightDriveMotor.setPower(0.4675);
                leftDriveMotor.setPower(-1.0);
                telemetry.addData("Gyro Position", sensorGyro.getIntegratedZValue());
                waitOneFullHardwareCycle();

            }
        }
        rightDriveMotor.setPower(0.0);
        leftDriveMotor.setPower(0.0);
    }
} // PushBotAuto
