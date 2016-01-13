package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
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
public class BlueAutoRamp extends PushBotAuto
{

    DcMotor motorRight;
    DcMotor motorLeft;

    Servo rotate;
    Servo dump;
    Servo light;
    Servo rightLever;
    Servo leftLever;

    ServoController sc;


    //Autonomous variables that probably make no sense

    int count = 0;
    private Calendar cal;
    double scale = 1.2;
    double speed = 0.25;
    double encCount = 155.64;
    boolean hasFoundFirstDate = false;
    private Calendar cal2;
    long startTime = 0;
    int currPos = 0;

    //--------------------------------------------------------------------------
    //
    // PushBotAuto
    //
    /**
     * Construct the class.
     *
     * The system calls this member when the class is instantiated.
     */
    public BlueAutoRamp()

    {
        //
        // Initialize base classes.
        //
        // All via self-construction.

        //
        // Initialize class members.
        //
        // All via self-construction.


    } // PushBotAuto

    //--------------------------------------------------------------------------
    //
    // start
    //
    /**
     * Perform any actions that are necessary when the OpMode is enabled.
     *
     * The system calls this member once when the OpMode is enabled.
     */
    @Override public void start ()

    {
        //
        // Call the PushBotHardware (super/base class) start method.
        //
        super.start();
        motorRight = hardwareMap.dcMotor.get("rightMotor");
        motorLeft = hardwareMap.dcMotor.get("leftMotor");
        motorRight.setDirection(DcMotor.Direction.REVERSE);

        dump = hardwareMap.servo.get("dumpServo");
        rotate = hardwareMap.servo.get("rotateServo");
        light = hardwareMap.servo.get("lightServo");
        leftLever = hardwareMap.servo.get("leftLever");
        rightLever = hardwareMap.servo.get("rightLever");

        dump.setPosition(0.0);
        rotate.setPosition(1);
        light.setPosition(0.5);
        rightLever.setPosition(1);
        leftLever.setPosition(0);

        sc = hardwareMap.servoController.get("tetrixServoController");
        sc.pwmEnable();

        light.setPosition(.5);
        dump.setPosition(0.8);
        rotate.setPosition(.68);
        leftLever.setPosition(.3);
        rightLever.setPosition(.7);




        //
        // Reset the motor encoders on the drive wheels.
        //


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
        //----------------------------------------------------------------------
        //
        // State: Initialize (i.e. state_0).
        //
        telemetry.addData("17", "Encoders at " + motorLeft.getCurrentPosition());
        telemetry.addData("16", "Count: " + count);
        telemetry.addData("15", "Seconds: " + findTurnTime(45) * (1000));
        if(count == 0) {
            update_telemetry();
            motorLeft.setPower(speed);
            motorRight.setPower(speed * (scale));

            if (motorLeft.getCurrentPosition() >= ((int) (encCount * 16))) {


                motorLeft.setPower(0.0);
                motorRight.setPower(0.0);
                count++;
            }
        }
        if(count == 1) {
            update_telemetry();
            if (!hasFoundFirstDate) {
                hasFoundFirstDate = true;

                startTime = cal.getInstance().getTimeInMillis();
            }
            float turnTime = findTurnTime((360 - 47)) * ((float) 1000);

            motorLeft.setPower(-1.0 * (1 / scale));
            motorRight.setPower(1.0);

            if (cal2.getInstance().getTimeInMillis() - startTime > turnTime) {
                motorLeft.setPower(0);
                motorRight.setPower(0);
                hasFoundFirstDate = false;
                count++;
                currPos = motorLeft.getCurrentPosition();
            }
        }
            if (count == 2) {
                update_telemetry();

                motorLeft.setPower(speed);
                motorRight.setPower(speed * (scale));

                if (motorLeft.getCurrentPosition() >= ((int) 6540.48) + currPos) {

                    motorLeft.setPower(0.0);
                    motorRight.setPower(0.0);
                    count++;
                    currPos = motorLeft.getCurrentPosition();
                }
            }
                if (count == 3) {
                    update_telemetry();
                    if (!hasFoundFirstDate) {
                        hasFoundFirstDate = true;

                        startTime = cal.getInstance().getTimeInMillis();
                    }
                    float turnTime = findTurnTime((360 - 80)) * ((float) 1000);

                    motorLeft.setPower(-1.0 * (1 / scale));
                    motorRight.setPower(1.0);

                    if (cal2.getInstance().getTimeInMillis() - startTime > turnTime) {
                        motorLeft.setPower(0);
                        motorRight.setPower(0);
                        count++;
                        currPos = motorLeft.getCurrentPosition();
                    }
                }
        if (count == 4) {
            update_telemetry();

            motorLeft.setPower(.7);
            motorRight.setPower(.7 * (scale));

            if (motorLeft.getCurrentPosition() >= ((int) 4290.48) + currPos) {

                motorLeft.setPower(0.0);
                motorRight.setPower(0.0);
                count++;
                currPos = motorLeft.getCurrentPosition();
            }
        }
                //
                // Send telemetry data to the driver station.
        //
                update_telemetry(); // Update common telemetry


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

    // t = (Theta * Distance Between Two Treads)/(2Pi * Velocity of Wheel[rad/sec] * Radius of Wheel)
    // hardwareMap.voltageSensor.get("Motor Controller 1").getVoltage();
    private float findTurnTime(float theta)
    {
        theta = theta * (float)Math.PI / (float) 180.0;

        float distBetweenTreads = (float) 0.40005; // 0.40005 meters
        float radiusOfWheel = (float) 0.0113338102; // 0.0113338102 meters
        float velocityOfWheel = (float) ((137.5/60.0) * 2 * Math.PI);

        float time = (theta * distBetweenTreads)/((float)Math.PI * (float) 2.0 * radiusOfWheel * velocityOfWheel);
        return (time * (float)1.54);
    }

} // PushBotAuto
