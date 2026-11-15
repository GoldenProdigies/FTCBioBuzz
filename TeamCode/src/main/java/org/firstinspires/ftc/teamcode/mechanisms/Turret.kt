package org.firstinspires.ftc.teamcode.mechanisms

import dev.nextftc.hardware.actuators.NextMotor
import dev.nextftc.robot.Mechanism
import org.firstinspires.ftc.teamcode.core.RobotHardware
import com.pedropathing.ivy.Command
import dev.nextftc.units.*

class Turret : Mechanism{
    val turretMotor = NextMotor(RobotHardware.M_TURRET.deviceName)

    init {
        turretMotor.direction = NextMotor.Direction.FORWARD
    }

    fun turnRight(): Command = Command.build()
        .setStart { turretMotor.throttle = -0.25 }
        .setEnd { turretMotor.throttle = 0.0 }
        .requiring(this)

    fun turnLeft(): Command = Command.build()
        .setStart { turretMotor.throttle = 0.25 }
        .setEnd { turretMotor.throttle = 0.0 }
        .requiring(this)


}