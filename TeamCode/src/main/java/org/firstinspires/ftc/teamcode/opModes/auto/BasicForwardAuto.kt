package org.firstinspires.ftc.teamcode.opModes.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

@Autonomous(name = "Basic Forward Auto", group = "Auto")
class BasicForwardAuto : LinearOpMode() {
    private val drivePower = 0.4

    override fun runOpMode() {
        val frontLeft = hardwareMap.get(DcMotor::class.java, "frontLeft")
        val frontRight = hardwareMap.get(DcMotor::class.java, "frontRight")
        val backLeft = hardwareMap.get(DcMotor::class.java, "backLeft")
        val backRight = hardwareMap.get(DcMotor::class.java, "backRight")
        val motors = listOf(frontLeft, frontRight, backLeft, backRight)

        frontLeft.direction = DcMotorSimple.Direction.REVERSE
        backLeft.direction = DcMotorSimple.Direction.REVERSE
        motors.forEach {
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            it.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            it.power = 0.0
        }

        telemetry.addLine("Ready")
        telemetry.update()
        waitForStart()

        if (!opModeIsActive()) {
            return
        }

        while (opModeIsActive()) {
            motors.forEach { it.power = drivePower }
            telemetry.addData("Drive power", drivePower)
            telemetry.update()
        }

        motors.forEach { it.power = 0.0 }
    }
}
