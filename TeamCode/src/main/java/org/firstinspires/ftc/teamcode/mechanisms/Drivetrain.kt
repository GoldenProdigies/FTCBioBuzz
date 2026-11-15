package org.firstinspires.ftc.teamcode.mechanisms


import com.pedropathing.ivy.Command
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.IMU
import dev.nextftc.hardware.actuators.NextMotor
import dev.nextftc.hardware.sensors.NextIMU
import dev.nextftc.robot.Mechanism
import dev.nextftc.robot.drive.mecanumDriveFieldCentric
import org.firstinspires.ftc.teamcode.core.RobotHardware

class Drivetrain : Mechanism {
    val frontLeft = NextMotor(RobotHardware.M_WHEEL_FL.deviceName)
    val frontRight = NextMotor(RobotHardware.M_WHEEL_FR.deviceName)
    val backLeft = NextMotor(RobotHardware.M_WHEEL_BL.deviceName)
    val backRight = NextMotor(RobotHardware.M_WHEEL_BR.deviceName)

    val imu = NextIMU()
    init {
        imu.initialize(IMU.Parameters(RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.RIGHT, RevHubOrientationOnRobot.UsbFacingDirection.UP)))
        imu.resetYaw()
    }

    init{
        frontLeft.direction = NextMotor.Direction.REVERSE
        backLeft.direction = NextMotor.Direction.REVERSE
    }

    fun resetYaw(): Command = instant { imu.resetYaw() }

    fun startDrive(gamepad: Gamepad) {
        mecanumDriveFieldCentric(frontLeft,frontRight,backLeft,backRight,gamepad,{imu.yaw}).schedule()
    }
}