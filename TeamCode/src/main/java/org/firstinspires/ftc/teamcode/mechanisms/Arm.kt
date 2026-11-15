package org.firstinspires.ftc.teamcode.mechanisms

import com.pedropathing.ivy.Command
import dev.nextftc.hardware.actuators.NextMotor
import dev.nextftc.hardware.actuators.NextServo
import dev.nextftc.robot.Mechanism
import org.firstinspires.ftc.teamcode.core.RobotHardware
import kotlin.math.abs

class Arm  : Mechanism{
    val armServo = NextServo(RobotHardware.S_ARM.deviceName)

    val isEngaged: Boolean
        get() = abs(armServo.position) > 0.1

    init {
        armServo.direction = NextMotor.Direction.FORWARD
    }

    fun engage(): Command = Command.build()
        .setStart { armServo.position = 0.4 }
        .setEnd {_ -> armServo.position = 0.0}
        .requiring(this)

    fun disengage(): Command = instant { armServo.position = 0.0 }
}