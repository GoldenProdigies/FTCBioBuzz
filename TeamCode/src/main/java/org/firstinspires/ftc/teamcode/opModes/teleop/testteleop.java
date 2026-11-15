package org.firstinspires.ftc.teamcode.opModes.teleop;

import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp(name = "Three Motor Toggle", group = "Test")
public class testteleop extends LinearOpMode {
    private static final double MOTOR3_TARGET_VELOCITY_TICKS_PER_SECOND = 1050.0;
    private static final PIDFCoefficients MOTOR3_PIDF =
            new PIDFCoefficients(44.0, 0.0, 0.0, 20.0);


    @Override
    public void runOpMode() {
        DcMotor motor1 = hardwareMap.get(DcMotor.class, "motor1");
        DcMotor motor2 = hardwareMap.get(DcMotor.class, "motor2");
        DcMotorEx motor3 = hardwareMap.get(DcMotorEx.class, "motor3");
        motor2.setDirection(DcMotorSimple.Direction.REVERSE);
        motor3.setDirection(DcMotorSimple.Direction.REVERSE);
        motor3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor3.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, MOTOR3_PIDF);

        boolean motorsRunning = false;
        boolean previousA = false;

        telemetry.addLine("Press A to toggle both motors");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            boolean currentA = gamepad1.a;
            if (currentA && !previousA) {
                motorsRunning = !motorsRunning;
            }
            previousA = currentA;

            double power = motorsRunning ? 1.0 : 0.0;
            motor1.setPower(power);
            motor2.setPower(power);
            motor3.setVelocity(motorsRunning ? MOTOR3_TARGET_VELOCITY_TICKS_PER_SECOND : 0.0);

            PanelsTelemetry.INSTANCE.getFtcTelemetry().addData(
                    "Motor 3 target velocity",
                    MOTOR3_TARGET_VELOCITY_TICKS_PER_SECOND
            );
            PanelsTelemetry.INSTANCE.getFtcTelemetry().addData(
                    "Motor 3 velocity",
                    motor3.getVelocity()
            );
            PanelsTelemetry.INSTANCE.getFtcTelemetry().update();

            telemetry.addData("Motors", motorsRunning ? "Running" : "Stopped");
            telemetry.addData("Power", power);
            telemetry.addData("Motor 3 velocity", "%.1f ticks/s", motor3.getVelocity());
            telemetry.update();
            idle();
        }

        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);

    }
}
