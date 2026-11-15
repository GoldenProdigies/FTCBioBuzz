package org.firstinspires.ftc.teamcode.opModes.teleop

import com.pedropathing.follower.ManualDrive
import com.pedropathing.ivy.commands.Commands.instant
import dev.nextftc.linalg.times
import dev.nextftc.robot.opmode.NextOpMode
import dev.nextftc.robot.opmode.NextTeleop
import dev.nextftc.robot.triggers.CommandGamepad
import dev.nextftc.units.rpm
import org.firstinspires.ftc.teamcode.RedneckRobot
import kotlin.time.times

@NextTeleop("Competition Teleop")
class CompetitionTeleop(val redneckRobot: RedneckRobot): NextOpMode(redneckRobot) {
    private var scalar = 1.0
    private var slowMode = false
    override fun start() {
        buildDriverControls()
    }

    override fun periodic() {
        redneckRobot.follower.manual(ManualDrive.fieldCentric(
            -gamepad1.left_stick_y.toDouble() * scalar,
            -gamepad1.left_stick_x.toDouble() * scalar,
            -gamepad1.right_stick_x.toDouble() * scalar,
            redneckRobot.drivetrain.imu.yaw
        ))
        redneckRobot.updateFollower()

        redneckRobot.led.setColor(
            if (redneckRobot.shooter.isAtTarget) {
                dev.nextftc.hardware.actuators.NextRGBIndicator.Color.GREEN
            } else {
                dev.nextftc.hardware.actuators.NextRGBIndicator.Color.RED
            }
        )

        telemetry.addData("Robot Pos:", redneckRobot.follower.pose())

        telemetry.addData("IMU Yaw: ", redneckRobot.drivetrain.imu.yaw)

        telemetry.addData("Shooter RPM:", redneckRobot.shooter.shooterMotor.encoderVelocity.magnitude)

        telemetry.addData("Shooter", redneckRobot.shooter.shooterMotor.encoderVelocity)

        telemetry.update()
    }

    fun buildDriverControls() {
        val driver = CommandGamepad(gamepad1) // Drives the robot and controls the intake + servo arm
        val operator  = CommandGamepad(gamepad2) // Controls the intake, kicker and the shooter

        // Reset IMU
        driver.rightStickButton.onTrue(redneckRobot.drivetrain.resetYaw())

        // Slow mode
        driver.b.onTrue(instant {
            slowMode = !slowMode
            scalar = if (slowMode) 0.5 else 1.0
        })

        // Intake
        driver.rightBumper.toggleOnTrue(redneckRobot.intake.intake())
        driver.leftBumper.toggleOnTrue(redneckRobot.intake.outtake())

        // Servo arm
        driver.y.toggleOnTrue(redneckRobot.arm.engage())

        // Intake
        operator.dpadUp.toggleOnTrue(redneckRobot.intake.intake())
        operator.dpadDown.onTrue(redneckRobot.intake.stop())

        // Turret
        operator.rightBumper.whileTrue(redneckRobot.turret.turnRight())
        operator.leftBumper.whileTrue(redneckRobot.turret.turnLeft())

        // Shooter
        operator.y.toggleOnTrue(redneckRobot.shooter.shoot())

        // Kicker
        operator.x.whileTrue(redneckRobot.kicker.intake())
        operator.b.whileTrue(redneckRobot.kicker.outtake())
    }
}