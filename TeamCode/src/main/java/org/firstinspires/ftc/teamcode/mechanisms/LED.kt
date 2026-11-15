package org.firstinspires.ftc.teamcode.mechanisms

import com.pedropathing.ivy.Command
import dev.nextftc.hardware.actuators.NextRGBIndicator
import dev.nextftc.robot.Mechanism
import org.firstinspires.ftc.teamcode.core.RobotHardware

class LED : Mechanism {
    val indicator = NextRGBIndicator(RobotHardware.S_LED.deviceName)

    fun setColor(color: NextRGBIndicator.Color) {
        indicator.setColor(color)
    }

    init {
    }

    fun off(): Command = instant {indicator.setColor(NextRGBIndicator.Color.OFF)}
    fun red(): Command = instant {indicator.setColor(NextRGBIndicator.Color.RED)}
    fun orange(): Command = instant {indicator.setColor(NextRGBIndicator.Color.ORANGE)}
    fun yellow(): Command = instant {indicator.setColor(NextRGBIndicator.Color.YELLOW)}
    fun sage(): Command = instant {indicator.setColor(NextRGBIndicator.Color.SAGE)}
    fun green(): Command = instant {indicator.setColor(NextRGBIndicator.Color.GREEN)}
    fun azure(): Command = instant {indicator.setColor(NextRGBIndicator.Color.AZURE)}
    fun blue(): Command = instant {indicator.setColor(NextRGBIndicator.Color.BLUE)}
    fun indigo(): Command = instant {indicator.setColor(NextRGBIndicator.Color.INDIGO)}
    fun violet(): Command = instant {indicator.setColor(NextRGBIndicator.Color.VIOLET)}
    fun white(): Command = instant {indicator.setColor(NextRGBIndicator.Color.WHITE)}
}