package com.oyosite.ticon.furlib.util

class BoneVisibilityCache(private val visibilityCondition: VisibilityDataCondition) {
    private val map: MutableMap<VisibilityData, Boolean?> = mutableMapOf()
    operator fun get(vis: VisibilityData): Boolean = map.getOrPut(vis) { visibilityCondition.test(vis) } ?: map.put(vis, visibilityCondition.test(vis))!!
    fun refresh(vis: (VisibilityData)->Boolean) = map.replaceAll{v, o -> if(vis(v))null else o}
    fun refreshBone(bone: String) = refresh { it.bone == bone }
    fun refreshTexture(tex: String) = refresh { it.tex == tex }
    fun refreshAll() = refresh { true }
}