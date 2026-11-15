package org.firstinspires.ftc.teamcode.core

enum class RobotHardware(val deviceName: String) {
    M_WHEEL_FR("frontRight"), //port 1
    M_WHEEL_BR("backRight"), // port 0
    M_WHEEL_FL("frontLeft"), // port 3
    M_WHEEL_BL("backLeft"), // port 2

    S_ARM("arm"), // port 1
    M_INTAKE("intake"), // port 1

    M_KICKER("kicker"), // port 3

    M_TURRET("turret"), // port 2
    M_SHOOTER("shooter"), // port 0

    S_LED("led"), // port 0

}