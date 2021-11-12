package com.oyosite.ticon.furlib.util

// Apparently you can't have lambdas defined in kotlin mixins, so they need to be somewhere else
object MixinUtils {
    val leftArmString: (String) -> Boolean = {s -> s=="leftArm"}
    val rightArmString: (String) -> Boolean = {s -> s=="rightArm"}
}