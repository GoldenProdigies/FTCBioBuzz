package org.firstinspires.ftc.teamcode.opmodes;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class ShooterConfig {

    public static double kP = 10.0;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kF = 12.0;

    public static double targetVelocity = 2000.0;

    /*
     * Change this value in Panels to adjust slow-mode speed.
     * 0.35 means 35% drivetrain speed.
     */
    public static double slowModeSpeed = 0.35;
}