/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import android.graphics.Color;

import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;


@TeleOp(name="Basic: Linear OpMode", group="Linear OpMode")
//@Override
public class TeleOp2026 extends LinearOpMode {
    final float[] hsvValues = new float[3];

//OUR THINGS
    DcMotor fLeft = hardwareMap.dcMotor.get("fLeft");
    DcMotor bLeft = hardwareMap.dcMotor.get("bLeft");
    DcMotor fRight = hardwareMap.dcMotor.get("fRight");
    DcMotor bRight = hardwareMap.dcMotor.get("bRight");
    DcMotor Gecko = hardwareMap.dcMotor.get("Gecko");
    DcMotor Intake = hardwareMap.dcMotor.get("Intake");

    NormalizedColorSensor colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
    //Plug in color sensor into one of the I2C busses

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();





    //Change directions when a motor isn't going the way it should
        bRight.setDirection(DcMotor.Direction.REVERSE);
        fRight.setDirection(DcMotor.Direction.REVERSE);
        fLeft.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses START)
        waitForStart();

        if (isStopRequested()) return;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
//DRIVING
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
                //Get rid of the 1.1 above if it's not working. Supposed to counteract imperfect strafing
            double rx = -gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                /*
                Denominator is the largest motor power (absolute value) or 1. This ensures all the powers
                 maintain the same ratio but only if at least one is out of the rage [-1,1].
                 */

            double FLeft = (y + x + rx)/denominator;
            double BLeft = (y - x + rx)/denominator;
            double FRight = (y - x - rx)/denominator;
            double BRight = (y + x - rx)/denominator;


            fLeft.setPower(FLeft/2);
            bLeft.setPower(BLeft/2);
            fRight.setPower(FRight/2);
            bRight.setPower(BRight/2);

//OUTPUT
            boolean geckoButton = gamepad2.a;
            double GeckoPower = 1;
                //Write here what shape is the 'a' button

            /*
            We can change this but I think it's best if the second driver does everything other than the driving.
            Also matches might be more consistent if the Gecko wheel only spins if a button is being held and as
            soon as it's released, it stops (easier than switching buttons).
            */
            if (geckoButton) {
                Gecko.setPower(GeckoPower);
            }
                else {
                    Gecko.setPower(0);
                }

//PARKING
            //Go to SensorColor.java if we need to adjust gain for accuracy
            NormalizedRGBA colors = colorSensor.getNormalizedColors();
            Color.colorToHSV(colors.toColor(), hsvValues);
            double parkingSpeed = gamepad1.left_trigger;


            /*There's gotta be a better way to do this if statement because right now I'm saying that if a driver
            if holding the trigger it will go parkingSpeed unless red or blue is greater than green and in that case
            The motors will stop but once the driver releases the trigger the colors part becomes false and the motors
            will go to parkingSpeed except parkingSpeed will be 0 since the trigger is completely released
             */
            if (parkingSpeed > 0) {
                //Might have to use RGBA values if this doesn't work
                if (colors.red > colors.green || colors.blue > colors.green) {
                    setAllMotors(0);


                }
            }
            else {
                fLeft.setPower(parkingSpeed);
                fRight.setPower(parkingSpeed);
                bLeft.setPower(parkingSpeed);
                bRight.setPower(parkingSpeed);
            }
            telemetry.addLine()
                    .addData("Red", "%.3f", colors.red)
                    .addData("Green", "%.3f", colors.green)
                    .addData("Blue", "%.3f", colors.blue);
            telemetry.update();

//BUTTON INTAKE
            boolean intakeButton = gamepad2.bWasPressed();
            double intakeSpeed = 1;
            boolean motorsRunning = Intake.isBusy();
            //Here we can either do a similar approach to the output or what I have below which is just pressing the button once
            if (intakeButton) {
                Intake.setPower(intakeSpeed);
            }
            if (intakeButton && motorsRunning) {
                Intake.setPower(0);
            }

//CONTINUOUS INTAKE OPTION
            //Uncomment the section below and comment the BUTTON INTAKE section if we ever want to switch
            /*
            double intakeSpeed = 1;
            Intake.setPower(intakeSpeed);
            */


        }
    }
    private void setAllMotors(double power) {
        fLeft.setPower(power);
        fRight.setPower(power);
        bLeft.setPower(power);
        bRight.setPower(power);
    }
}
