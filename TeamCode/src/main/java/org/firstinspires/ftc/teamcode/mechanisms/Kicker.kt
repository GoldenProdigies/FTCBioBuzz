package org.firstinspires.ftc.teamcode.mechanisms

import com.pedropathing.ivy.Command
import dev.nextftc.hardware.actuators.NextMotor
import dev.nextftc.robot.Mechanism
import org.firstinspires.ftc.teamcode.core.RobotHardware
import kotlin.math.abs

class Kicker : Mechanism {
    val kickerMotor = NextMotor(RobotHardware.M_KICKER.deviceName)

    val isRunning: Boolean
        get() = abs(kickerMotor.throttle) > 0.1

    init {
        kickerMotor.direction = NextMotor.Direction.FORWARD
    }

    fun intake(): Command = Command.build()
        .setStart { kickerMotor.throttle = 1.0 }
        .setEnd { _ -> kickerMotor.throttle = 0.0 }
        .requiring(this)

    fun outtake(): Command = Command.build()
        .setStart { kickerMotor.throttle = -1.0 }
        .setEnd {_ -> kickerMotor.throttle = 0.0}
        .requiring(this)

    fun stop(): Command = instant { kickerMotor.throttle = 0.0 }
}