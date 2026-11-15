package org.firstinspires.ftc.teamcode

import com.pedropathing.follower.Follower
import dev.nextftc.hardware.RobotController
import dev.nextftc.robot.NextRobot
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.mechanisms.*

class RedneckRobot : NextRobot{
    val intake by lazy { Intake() }
    val arm by lazy { Arm() }
    val kicker by lazy { Kicker() }
    val shooter by lazy { Shooter() }
    val drivetrain by lazy { Drivetrain() }
    val turret by lazy { Turret() }
    val led by lazy { LED() }

    override val mechanisms by lazy {setOf(intake, arm, kicker, shooter, drivetrain, turret, led)}

    private var _follower : Follower? = null

    val follower: Follower
        get() {
            if (_follower == null) {
                _follower = Constants.create(RobotController.hardwareMap)
            }
            return _follower!!
        }

    fun updateFollower() {
        follower.update()
    }


}