package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.opmodes.ShooterConfig;

@TeleOp(name = "Field-Centric TeleOp", group = "TeleOp")
public class fieldTeleop2 extends LinearOpMode {

    private static final double STRAFE_CORRECTION = 1.1;

    @Override
    public void runOpMode() throws InterruptedException {

        /*
         * Drivetrain motors
         */
        DcMotor frontLeftMotor = hardwareMap.get(DcMotor.class, "FL");
        DcMotor backLeftMotor = hardwareMap.get(DcMotor.class, "RL");
        DcMotor frontRightMotor = hardwareMap.get(DcMotor.class, "FR");
        DcMotor backRightMotor = hardwareMap.get(DcMotor.class, "RR");

        /*
         * Intake, shooter, and kick motors
         */
        DcMotor frontIntakeMotor = hardwareMap.get(DcMotor.class, "FIM");
        DcMotor backIntakeMotor = hardwareMap.get(DcMotor.class, "RIM");
        DcMotorEx shootMotor = hardwareMap.get(DcMotorEx.class, "SM");
        DcMotor kickIntakeMotor = hardwareMap.get(DcMotor.class, "KIM");

        /*
         * Drivetrain motor directions
         */
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        /*
         * Mechanism motor directions
         *
         * Reverse individual intake motors if needed so they work together.
         */
        frontIntakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backIntakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        kickIntakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        shootMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        /*
         * Zero-power behavior
         */
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontIntakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backIntakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        kickIntakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shootMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        /*
         * Shooter encoder and PIDF setup
         */
        shootMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shootMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        applyShooterPIDF(shootMotor);

        /*
         * IMU setup
         */
        IMU imu = hardwareMap.get(IMU.class, "imu");

        IMU.Parameters imuParameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                )
        );

        imu.initialize(imuParameters);

        /*
         * Mechanism states
         */
        boolean intakeIsOn = false;
        boolean intakeIsReversed = false;
        boolean kickIsOn = false;
        boolean shooterIsOn = false;
        boolean slowMode = false;

        /*
         * Previous button states
         */
        boolean previousLeftBumper = false;
        boolean previousRightBumper = false;
        boolean previousResetButton = false;
        boolean previousBButton = false;
        boolean previousYButton = false;
        boolean previousXButton = false;

        /*
         * PIDF values previously applied to the motor
         */
        double lastKP = ShooterConfig.kP;
        double lastKI = ShooterConfig.kI;
        double lastKD = ShooterConfig.kD;
        double lastKF = ShooterConfig.kF;

        telemetry.addLine("Initialized");
        telemetry.addLine("Press PLAY to start");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) {
            return;
        }

        while (opModeIsActive()) {

            /*
             * Reapply PIDF values if they are changed in Panels.
             */
            boolean pidfChanged =
                    Double.compare(lastKP, ShooterConfig.kP) != 0
                            || Double.compare(lastKI, ShooterConfig.kI) != 0
                            || Double.compare(lastKD, ShooterConfig.kD) != 0
                            || Double.compare(lastKF, ShooterConfig.kF) != 0;

            if (pidfChanged) {
                applyShooterPIDF(shootMotor);

                lastKP = ShooterConfig.kP;
                lastKI = ShooterConfig.kI;
                lastKD = ShooterConfig.kD;
                lastKF = ShooterConfig.kF;
            }

            /*
             * Toggle slow mode when B is pressed.
             */
            boolean bButtonPressed = gamepad1.b && !previousBButton;

            if (bButtonPressed) {
                slowMode = !slowMode;
            }

            previousBButton = gamepad1.b;

            /*
             * Toggle intake direction when Y is pressed.
             *
             * Press Y once: reverse intake direction
             * Press Y again: return to normal intake direction
             */
            boolean yButtonPressed = gamepad1.y && !previousYButton;

            if (yButtonPressed) {
                intakeIsReversed = !intakeIsReversed;
            }

            previousYButton = gamepad1.y;

            /*
             * Toggle kick motor when X is pressed.
             */
            boolean xButtonPressed = gamepad1.x && !previousXButton;

            if (xButtonPressed) {
                kickIsOn = !kickIsOn;
            }

            previousXButton = gamepad1.x;

            /*
             * Read driver controls.
             */
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rotation = gamepad1.right_stick_x;

            /*
             * Apply slow-mode multiplier.
             */
            double driveMultiplier = slowMode
                    ? ShooterConfig.slowModeSpeed
                    : 1.0;

            y *= driveMultiplier;
            x *= driveMultiplier;
            rotation *= driveMultiplier;

            /*
             * Reset IMU yaw once per right-stick-button press.
             */
            boolean resetButtonPressed =
                    gamepad1.right_stick_button && !previousResetButton;

            if (resetButtonPressed) {
                imu.resetYaw();
            }

            previousResetButton = gamepad1.right_stick_button;

            /*
             * Field-centric drive calculation.
             */
            double heading = imu
                    .getRobotYawPitchRollAngles()
                    .getYaw(AngleUnit.RADIANS);

            double rotatedX =
                    x * Math.cos(-heading) - y * Math.sin(-heading);

            double rotatedY =
                    x * Math.sin(-heading) + y * Math.cos(-heading);

            rotatedX *= STRAFE_CORRECTION;

            /*
             * Normalize drivetrain powers.
             */
            double denominator = Math.max(
                    Math.abs(rotatedY)
                            + Math.abs(rotatedX)
                            + Math.abs(rotation),
                    1.0
            );

            double frontLeftPower =
                    (rotatedY + rotatedX + rotation) / denominator;

            double backLeftPower =
                    (rotatedY - rotatedX + rotation) / denominator;

            double frontRightPower =
                    (rotatedY - rotatedX - rotation) / denominator;

            double backRightPower =
                    (rotatedY + rotatedX - rotation) / denominator;

            /*
             * Apply drivetrain power.
             */
            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);

            /*
             * Toggle main intake and shooter once per bumper press.
             */
            boolean leftBumperPressed =
                    gamepad1.left_bumper && !previousLeftBumper;

            boolean rightBumperPressed =
                    gamepad1.right_bumper && !previousRightBumper;

            if (leftBumperPressed) {
                intakeIsOn = !intakeIsOn;
            }

            if (rightBumperPressed) {
                shooterIsOn = !shooterIsOn;
            }

            previousLeftBumper = gamepad1.left_bumper;
            previousRightBumper = gamepad1.right_bumper;

            /*
             * Intake control.
             *
             * The Y button changes the direction of all intake motors.
             */
            double intakePower = 0.0;

            if (intakeIsOn) {
                intakePower = intakeIsReversed ? -1.0 : 1.0;
            }

            frontIntakeMotor.setPower(intakePower);
            backIntakeMotor.setPower(intakePower);

            /*
             * Kick motor control.
             *
             * The X button toggles the kick motor.
             * It follows the same direction as the intake motors.
             */
            double kickPower = 0.0;

            if (kickIsOn) {
                kickPower = intakeIsReversed ? -1.0 : 1.0;
            }

            kickIntakeMotor.setPower(kickPower);

            /*
             * Shooter velocity control.
             */
            if (shooterIsOn) {
                shootMotor.setVelocity(ShooterConfig.targetVelocity);
            } else {
                shootMotor.setVelocity(0.0);
            }

            /*
             * Telemetry.
             */
            telemetry.addData(
                    "Heading",
                    "%.1f degrees",
                    Math.toDegrees(heading)
            );

            telemetry.addData("Slow Mode", slowMode ? "ON" : "OFF");
            telemetry.addData("Intake", intakeIsOn ? "ON" : "OFF");

            telemetry.addData(
                    "Intake Direction",
                    intakeIsReversed ? "REVERSE" : "FORWARD"
            );

            telemetry.addData("Kick Motor", kickIsOn ? "ON" : "OFF");
            telemetry.addData("Shooter", shooterIsOn ? "ON" : "OFF");

            telemetry.addData(
                    "Target Velocity",
                    "%.0f ticks/sec",
                    shooterIsOn ? ShooterConfig.targetVelocity : 0.0
            );

            telemetry.addData(
                    "Actual Velocity",
                    "%.0f ticks/sec",
                    shootMotor.getVelocity()
            );

            telemetry.addData("kP", "%.4f", ShooterConfig.kP);
            telemetry.addData("kI", "%.4f", ShooterConfig.kI);
            telemetry.addData("kD", "%.4f", ShooterConfig.kD);
            telemetry.addData("kF", "%.4f", ShooterConfig.kF);

            telemetry.update();

            idle();
        }

        /*
         * Stop all motors when the OpMode ends.
         */
        frontLeftMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
        backRightMotor.setPower(0.0);

        frontIntakeMotor.setPower(0.0);
        backIntakeMotor.setPower(0.0);
        kickIntakeMotor.setPower(0.0);
        shootMotor.setVelocity(0.0);
    }

    private void applyShooterPIDF(DcMotorEx shootMotor) {
        PIDFCoefficients coefficients = new PIDFCoefficients(
                ShooterConfig.kP,
                ShooterConfig.kI,
                ShooterConfig.kD,
                ShooterConfig.kF
        );

        shootMotor.setPIDFCoefficients(
                DcMotor.RunMode.RUN_USING_ENCODER,
                coefficients
        );
    }
}