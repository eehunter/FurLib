package com.oyosite.ticon.furlib.power

import com.oyosite.ticon.furlib.util.MiscRegistries
import io.github.apace100.apoli.power.factory.condition.ConditionType

object FLConditionTypes {
    val VISIBILITY_DATA = ConditionType("VisibilityDataCondition", MiscRegistries.VISIBILITY_DATA_CONDITION)
    val STRING = ConditionType("StringCondition", MiscRegistries.STRING_CONDITION)
}