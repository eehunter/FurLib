package com.oyosite.ticon.furlib.data

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.apace100.calio.data.SerializableData
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

class SlotFinderFactory(val registryId: Identifier, val data: SerializableData, private val write: SlotFinderFactory.(SerializableData.Instance, SlotFinder) -> Unit, private val read: SlotFinderFactory.(SerializableData.Instance) -> SlotFinder){
    fun read(json: JsonObject): SlotFinder = this.read(data.read(json))
    fun read(buf: PacketByteBuf): SlotFinder = this.read(data.read(buf))
    fun write(buf: PacketByteBuf, sf: SlotFinder) {val sdi = data.Instance(); write(sdi, sf); data.write(buf, sdi)}
}