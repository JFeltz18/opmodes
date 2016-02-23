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
        if (step == 1)
        {
            travel(8275, rightDriveMotor.getCurrentPosition(), TravelDirection.FORWARD);
            recursiveAuto(2);
        }
        if (step == 2)
        {
            rotate(45, sensorGyro.getIntegratedZValue(), TurnDirection.COUNTER_CLOCKWISE);
        }
    }
}
