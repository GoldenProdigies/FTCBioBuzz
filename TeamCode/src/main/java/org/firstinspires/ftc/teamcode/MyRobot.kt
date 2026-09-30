package org.firstinspires.ftc.teamcode

import dev.nextftc.robot.Mechanism
import dev.nextftc.robot.NextRobot
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain

class MyRobot : NextRobot {
    val drivetrain = Drivetrain()

    override val mechanisms = setOf(drivetrain)
}