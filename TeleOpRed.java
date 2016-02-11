/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class TeleOpRed extends OpMode {

	// test
	DcMotor rightScissorsMotor;
	DcMotor leftScissorsMotor;
	DcMotor rightDriveMotor;
	DcMotor leftDriveMotor;
	DcMotor debrisCollectMotor;
	DcMotor dumpMotor;

	float scale = (float) 1.0;
	boolean collectionOn = true;
	boolean collectionReversed = false;



	public void init() {
		rightScissorsMotor = hardwareMap.dcMotor.get("rightScissorsMotor");
		leftScissorsMotor = hardwareMap.dcMotor.get("leftScissorsMotor");
		debrisCollectMotor = hardwareMap.dcMotor.get("debrisCollectMotor");
		rightDriveMotor = hardwareMap.dcMotor.get("rightDriveMotor");
		leftDriveMotor = hardwareMap.dcMotor.get("leftDriveMotor");
		dumpMotor = hardwareMap.dcMotor.get("dumpMotor");

		dumpMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		rightScissorsMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
	}

	public void start() {
		dumpMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		rightScissorsMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
	}
	// Slow down after 1850 and before 500
	public void loop() {
		rightScissorsMotor.setDirection(DcMotor.Direction.REVERSE);
		leftScissorsMotor.setDirection(DcMotor.Direction.FORWARD);
		rightDriveMotor.setDirection(DcMotor.Direction.FORWARD);
		leftDriveMotor.setDirection(DcMotor.Direction.REVERSE);
		debrisCollectMotor.setDirection(DcMotor.Direction.REVERSE);
		dumpMotor.setDirection(DcMotor.Direction.REVERSE);


		if (!gamepad2.a && !gamepad2.b)
		{
			debrisCollectMotor.setPower(1.0);
		}
		else
		{
			if (gamepad2.b && !gamepad2.a)
			{
				debrisCollectMotor.setPower(-1.0);
			}
			else
			{
				debrisCollectMotor.setPower(0.0);
			}
		}

		if (gamepad1.a)
		{
			if (rightScissorsMotor.getCurrentPosition() * -1 >= 1850 || rightScissorsMotor.getCurrentPosition() * -1 <= 500)
			{
				leftScissorsMotor.setPower(-scale);
				rightScissorsMotor.setPower(-scale);
			}
			else {
				leftScissorsMotor.setPower(-0.5 * scale);
				rightScissorsMotor.setPower(-0.5 * scale);
			}
		}
		else {
			if (gamepad1.y)
			{
				if (rightScissorsMotor.getCurrentPosition() * -1 >= 1850)
				{
					leftScissorsMotor.setPower(scale);
					rightScissorsMotor.setPower(scale);
				}
				else {
					leftScissorsMotor.setPower(0.5 * scale);
					rightScissorsMotor.setPower(0.5 * scale);
				}
			}
			else {
				rightScissorsMotor.setPower(0.0);
				leftScissorsMotor.setPower(0.0);
			}
		}

		if(Math.abs(gamepad1.left_stick_y) > 0.1)
		{
			leftDriveMotor.setPower(gamepad1.left_stick_y);
		}
		else
		{
			leftDriveMotor.setPower(0.0);
		}

		if(Math.abs(gamepad1.right_stick_y) > 0.1)
		{
			rightDriveMotor.setPower(gamepad1.right_stick_y);
		}
		else
		{
			rightDriveMotor.setPower(0.0);
		}


		if (gamepad2.right_bumper)
		{
			dumpMotor.setPower(0.025);
		}
		else {
			if (gamepad2.left_bumper) {
				dumpMotor.setPower(-0.025);
			} else {
				dumpMotor.setPower(0.0);
			}
		}

		telemetry.addData("Lift Encoder Value: ", rightScissorsMotor.getCurrentPosition());
		telemetry.addData("Dump Encoder Value: ", dumpMotor.getCurrentPosition());
	}

	@Override
	public void stop() {

	}


	/*
	 * This method scales the joystick input so for low joystick values, the
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
	double scaleInput(double dVal)  {
		double[] scale = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

		// get the corresponding index for the scaleInput array.
		int i = (int) (dVal * 16.0);

		// index should be positive.
		if (i < 0) {
			i = -i;
		}

		// index cannot exceed size of array minus 1.
		if (i > 16) {
			i = 16;
		}

		// get value from the array.
		double dScale = 0.0;
		if (dVal < 0) {
			dScale = -scale[i];
		} else {
			dScale = scale[i];
		}

		// return scaled value.
		return dScale;
	}

}