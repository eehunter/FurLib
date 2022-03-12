package com.oyosite.ticon.furlib.util

import io.github.apace100.apoli.power.factory.condition.ConditionFactory
import net.minecraft.entity.Entity
import net.minecraft.util.Pair

typealias MCPair<A, B> = Pair<A, B>
typealias BiEntity = MCPair<Entity, Entity>
typealias ApoliCondition<T> = ConditionFactory<T>.Instance
typealias BiEntityCondition = ApoliCondition<BiEntity>
//typealias BiEntityBone = MCPair<String,BiEntity>
typealias VisibilityDataCondition = ApoliCondition<VisibilityData>
class VisibilityData(val tex: String, val bone: String, val target: Entity, val viewer: Entity){
    companion object{val VisibilityData.biEntity get() = MCPair(target, viewer)}
}
