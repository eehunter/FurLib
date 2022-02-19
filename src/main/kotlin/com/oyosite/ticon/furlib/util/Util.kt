package com.oyosite.ticon.furlib.util

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.oyosite.ticon.furlib.data.SlotFinder
import io.github.apace100.apoli.power.factory.condition.ConditionFactory
import io.github.apace100.apoli.power.factory.condition.ConditionType
import io.github.apace100.calio.data.SerializableDataType

@Suppress("UNCHECKED_CAST")
fun <T> conditionClassOf() = ConditionFactory.Instance::class.java as Class<T>
@Suppress("UNCHECKED_CAST")
fun <T> slotFinderClassOf() = SlotFinder::class.java as Class<T>
fun json(json: String): JsonElement = JsonParser.parseString(json)
fun <T> ConditionType<T>.read(json: String): ApoliCondition<T> = read(json(json))
fun <T> SerializableDataType<T>.read(json: String): T = read(json(json))
