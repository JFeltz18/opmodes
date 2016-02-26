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
public class RedRampAuto extends BaseAuto {


    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        leftDriveMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightDriveMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        recursiveAuto(1);
    }


    public void recursiveAuto(int step) throws InterruptedException // 9000 encoder clicks equals 86"
    {
        if (step == 1) {
            travel(9444, 0, TravelDirection.FORWARD);
            rightDriveMotor.setPower(0);
            leftDriveMotor.setPower(0);
            Thread.sleep(1000);
            waitOneFullHardwareCycle();
            recursiveAuto(2);
        }

        if(step == 2)
        {
            while(Math.abs(rightScissorsMotor.getCurrentPosition()) < 7000) {
                leftScissorsMotor.setPower(0.5);
                rightScissorsMotor.setPower(0.5);
            }
            recursiveAuto(3);
        }
        //one full rotation for tetrix motor encoders is 1440 clicks, we want 1/4 rotation so 360 clicks (1440 / 4), added 10 for reasons
        if(step == 3)
        {
            while(Math.abs(dumpMotor.getCurrentPosition()) > 370)
            dumpMotor.setPower(.2);

        }
    }
}
