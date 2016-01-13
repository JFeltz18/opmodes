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

import com.qualcomm.hardware.ModernRoboticsI2cGyro;


import com.qualcomm.hardware.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceReader;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.*;
/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class GyroTeleOpTest extends OpMode {

	DcMotor motorRight;
	DcMotor motorLeft;
	Servo dump;
	Servo rotate;
	Servo light;
	Servo leftLever;
	Servo rightLever;

	ServoController sc;

	I2cDevice sensorGyro;
	I2cDeviceReader reeder;
	I2cController con;









	/**
	 * Constructor
	 */
	public GyroTeleOpTest() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 */
	@Override
	public void init() {
		motorRight = hardwareMap.dcMotor.get("rightMotor");
		motorLeft = hardwareMap.dcMotor.get("leftMotor");

		dump = hardwareMap.servo.get("dumpServo");
		rotate = hardwareMap.servo.get("rotateServo");
		light = hardwareMap.servo.get("lightServo");
		leftLever = hardwareMap.servo.get("leftLever");
		rightLever = hardwareMap.servo.get("rightLever");



		dump.setPosition(0.15);
		rotate.setPosition(0.8);
		light.setPosition(0.0);
		leftLever.setPosition(0.25);
		rightLever.setPosition(0.75);

		sc = hardwareMap.servoController.get("tetrixServoController");
		sc.pwmEnable();






	}

	@Override
	public void loop() {

		// write the values to the motors
		motorRight.setDirection(DcMotor.Direction.REVERSE);
		motorLeft.setDirection(DcMotor.Direction.FORWARD);


		if (!gamepad1.left_stick_button) {
			motorLeft.setPower(gamepad1.left_stick_y * (1 / 1.2));
		} else {
			motorLeft.setPower(gamepad1.left_stick_y * 0.5 * (1 / 1.2));
		}

		if (!gamepad1.right_stick_button) {
			motorRight.setPower(gamepad1.right_stick_y);
		} else {
			motorRight.setPower(gamepad1.right_stick_y * 0.5);
		}

		if (gamepad1.dpad_down) {
			motorRight.setPower(0.3);
			motorLeft.setPower(0.3);
		}

		if (gamepad1.dpad_up) {
			motorRight.setPower(-0.3);
			motorLeft.setPower(-0.3);
		}
		if (gamepad1.a) {
			dump.setPosition(0.8);

		}

		if (gamepad1.b) {
			rotate.setPosition(0.8);
		}

		if (gamepad1.x) {
			rotate.setPosition(0.0);
		}

		if (gamepad1.y) {
			dump.setPosition(0.0);
		}

		if (gamepad1.left_bumper) {
			leftLever.setPosition(0.25);
		}

		if (gamepad1.left_trigger > 0.7) {
			leftLever.setPosition(1.0);
		}

		if (gamepad1.right_bumper) {
			rightLever.setPosition(0.75);
		}

		if (gamepad1.right_trigger > 0.7) {
			rightLever.setPosition(0.0);
		}

		if (gamepad2.x) {
			light.setPosition(0.0);
		}

		if (gamepad2.b)
		{
			light.setPosition(1.0);
		}
		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("dump", "dump:  " + String.format("%.2f", dump.getPosition()));
        telemetry.addData("rotate", "rotate:  " + String.format("%.2f", rotate.getPosition()));
        telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", motorLeft.getPower()));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", motorRight.getPower()));

		//telemetry.addData("gyro reeder", reeder.getReadBuffer()[0]);
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
