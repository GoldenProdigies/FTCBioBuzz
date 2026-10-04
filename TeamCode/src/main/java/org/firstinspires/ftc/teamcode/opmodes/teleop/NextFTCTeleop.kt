package org.firstinspires.ftc.teamcode.opmodes.teleop

import dev.nextftc.robot.Telemetry
import dev.nextftc.robot.opmode.BulkReadHook
import dev.nextftc.robot.opmode.NextOpMode
import dev.nextftc.robot.opmode.NextTeleop
import org.firstinspires.ftc.teamcode.MyRobot

@NextTeleop("NextFTCTeleop", "Teleop")
class NextFTCTeleop(val robot: MyRobot) : NextOpMode(robot, BulkReadHook) {
    override fun start() {
        robot.drivetrain.startDrive(gamepad1)
    }
    override fun periodic() {
        Telemetry.log("Status", "Running")
    }
}