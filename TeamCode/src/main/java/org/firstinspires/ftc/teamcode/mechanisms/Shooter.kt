package org.firstinspires.ftc.teamcode.mechanisms

import com.pedropathing.ivy.Command
import dev.nextftc.hardware.actuators.NextMotor
import dev.nextftc.robot.Mechanism
import org.firstinspires.ftc.teamcode.core.RobotHardware
import kotlin.math.abs

class Shooter : Mechanism {
    companion object {
        private const val TARGET_VELOCITY = 1150.0
        private const val VELOCITY_TOLERANCE = 25.0
        private const val SHOOTER_POWER = 1.0
    }

    val shooterMotor = NextMotor(RobotHardware.M_SHOOTER.deviceName)

    val isRunning: Boolean
        get() = abs(shooterMotor.throttle) > 0.1

    val isAtTarget: Boolean
        get() = abs(shooterMotor.encoderVelocity.magnitude - TARGET_VELOCITY) <= VELOCITY_TOLERANCE

    init {
        shooterMotor.direction = NextMotor.Direction.REVERSE
        shooterMotor.zeroPowerBehavior = NextMotor.ZeroPowerBehavior.FLOAT
    }

    fun shoot(): Command = Command.build()
        .setStart {
            shooterMotor.throttle = SHOOTER_POWER
        }
        .setExecute {
            val velocity = shooterMotor.encoderVelocity.magnitude
            when {
                velocity <= TARGET_VELOCITY - VELOCITY_TOLERANCE -> {
                    shooterMotor.throttle = SHOOTER_POWER
                }
                velocity >= TARGET_VELOCITY + VELOCITY_TOLERANCE -> {
                    shooterMotor.throttle = 0.0
                }
            }
        }
        .setEnd { shooterMotor.throttle = 0.0 }
        .requiring(this)

    fun stop(): Command = instant { shooterMotor.throttle = 0.0 }
}
