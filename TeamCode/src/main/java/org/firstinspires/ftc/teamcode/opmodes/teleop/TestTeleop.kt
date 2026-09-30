package org.firstinspires.ftc.teamcode.opmodes.teleop

import dev.nextftc.robot.opmode.NextOpMode
import dev.nextftc.robot.opmode.NextTeleop
import org.firstinspires.ftc.teamcode.MyRobot

@NextTeleop
class TestTeleop(val robot: MyRobot) : NextOpMode(robot) {
    override fun start() {
        robot.drivetrain.startDrive(gamepad1)
    }
}