package org.firstinspires.ftc.teamcode.mechanisms

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.IMU
import dev.nextftc.hardware.RobotController
import dev.nextftc.hardware.actuators.NextMotor
import dev.nextftc.hardware.actuators.NextMotor.Direction
import dev.nextftc.hardware.sensors.NextIMU
import dev.nextftc.robot.Mechanism
import dev.nextftc.robot.drive.mecanumDriveFieldCentric
import dev.nextftc.units.radians

class Drivetrain : Mechanism {
    // TODO: Change ports to the correct ones
    val frontLeft = NextMotor("FL")
    val frontRight = NextMotor("FR")
    val backLeft = NextMotor("RL")
    val backRight = NextMotor("RR")
    val imu = NextIMU()

    fun initalizeIMU() {
        imu.initialize(IMU.Parameters(RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD)))
        imu.resetYaw()
    }

    fun startDrive(gamepad: Gamepad) {
        initalizeIMU()
        frontLeft.direction = Direction.REVERSE
        backLeft.direction = Direction.REVERSE
        frontLeft.zeroPowerBehavior = NextMotor.ZeroPowerBehavior.BRAKE
        frontRight.zeroPowerBehavior = NextMotor.ZeroPowerBehavior.BRAKE
        backLeft.zeroPowerBehavior = NextMotor.ZeroPowerBehavior.BRAKE
        backRight.zeroPowerBehavior = NextMotor.ZeroPowerBehavior.BRAKE
        mecanumDriveFieldCentric(frontLeft , frontRight, backLeft, backRight, gamepad, {imu.yaw}).schedule()
    }
}