package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by pignault_874022 on 11/3/2015.
 */
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class TestTeleOP extends OpMode {

    /*
     * Note: the configuration of the servos is such that
     * as the arm servo approaches 0, the arm position moves up (away from the floor).
     * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
     */
    // TETRIX VALUES.
    final static double BR_MIN_RANGE  = 0.20;
    final static double BR_MAX_RANGE  = 0.90;
    final static double MID_MIN_RANGE  = 0.20;
    final static double MID_MAX_RANGE  = 0.7;
    final static double BL_MIN_RANGE  = 0.20;
    final static double BL_MAX_RANGE  = 0.90;
    final static double END_MIN_RANGE  = 0.20;
    final static double END_MAX_RANGE  = 0.7;
    final static double ARM_MIN_RANGE  = 0.20;
    final static double ARM_MAX_RANGE  = 0.7;
    // position of the arm servo.
    double bRPosition;

    // amount to change the arm servo position.
    double bRDelta = 0.05;

    // position of the claw servo
    double bLPosition;

    // amount to change the claw servo position by
    double bLDelta = 0.05;
    // position of the arm servo.

    double midPosition;

    // amount to change the arm servo position.
    double midDelta = 0.1;

    // position of the claw servo
    double endPosition;

    // amount to change the claw servo position by
    double endDelta = 0.1;

    // position of the claw servo
    double armPosition;

    // amount to change the claw servo position by
    double armDelta = 0.1;


// ////////////////////////////////////////////////////////////////////////FIN DEFIN////////////////////////////
    DcMotor motorRight;
    DcMotor motorRight_2;
    DcMotor motorLeft;
    DcMotor motorLeft_2;
    Servo bR;
    Servo bL;
    Servo mid;
    Servo end;
    Servo arm;
    /**
     * Constructor
     */
    public TestTeleOP() {

    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot.
		 *
		 //We also assume that there are two servos "servo_1" and "servo_6"
		 //   "servo_1" controls the arm joint of the manipulator.
		 *    "servo_6" controls the claw joint of the manipulator.
		 */
        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);


        bR = hardwareMap.servo.get("base_R");
        bL = hardwareMap.servo.get("base_L");
        mid = hardwareMap.servo.get("middle");
        end = hardwareMap.servo.get("end");
        bL.setDirection(Servo.Direction.REVERSE);
        arm = hardwareMap.servo.get("arm");

        // assign the starting position of the wrist and claw
        bRPosition = 0.2;
        bLPosition = 0.2;
        midPosition = 0.2;
        endPosition = 0.2;
        armPosition = 0.2;
    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

		/*
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * wrist/claw via the a,b, x, y buttons
		 */

        // tank drive
        // note that if y equal -1 then joystick is pushed all of the way forward.
        float left = -gamepad1.left_stick_y;
        float right = -gamepad1.right_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        // write the values to the motors
        motorRight.setPower(right);
        motorLeft.setPower(left);

        // update the position of the arm.
        if (gamepad2.right_trigger > 0.25) {
            // if the A button is pushed on gamepad1, increment the position of
            // the arm servo.
            bRPosition += bRDelta;
        }

        if (gamepad2.left_trigger > 0.25) {
            // if the Y button is pushed on gamepad1, decrease the position of
            // the arm servo.
            bRPosition -= bRDelta;
        }

        // update the position of the claw
        if (gamepad2.right_trigger > 0.25) {
            bLPosition += bLDelta;
        }

        if (gamepad2.left_trigger > 0.25) {
            bLPosition -= bLDelta;
        }

        // update the position of the claw
        if (gamepad2.right_bumper) {
            midPosition += midDelta;
        }

        if (gamepad2.left_bumper) {
            midPosition -= midDelta;
        }
        if (gamepad2.x) {
            endPosition += endDelta;
        }

        if (gamepad2.y) {
            endPosition -= endDelta;
        }
        if (gamepad2.a) {
            armPosition += armDelta;
        }

        if (gamepad2.b) {
            armPosition -= armDelta;
        }



        // clip the position values so that they never exceed their allowed range.
        bRPosition = Range.clip(bRPosition, BR_MIN_RANGE, BR_MAX_RANGE);
        bLPosition = Range.clip(bLPosition, BL_MIN_RANGE, BL_MAX_RANGE);

        // write position values to the wrist and claw servo
        bR.setPosition(bRPosition);
        bL.setPosition(bLPosition);
        midPosition = Range.clip(midPosition, MID_MIN_RANGE, MID_MAX_RANGE);
        endPosition = Range.clip(endPosition, END_MIN_RANGE, END_MAX_RANGE);
        mid.setPosition(midPosition);
        end.setPosition(endPosition);
        armPosition = Range.clip(armPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
        arm.setPosition(armPosition);
		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */

        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("arm", "arm:  " + String.format("%.2f", armPosition));
        telemetry.addData("bR", "bR:  " + String.format("%.2f", bRPosition));
        telemetry.addData("bL", "bL:  " + String.format("%.2f", bLPosition));
        telemetry.addData("mid", "mid:  " + String.format("%.2f", midPosition));
        telemetry.addData("end", "end:  " + String.format("%.2f", endPosition));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}
