package com.oyosite.ticon.furlib.data

import com.oyosite.ticon.furlib.util.MiscRegistries
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

class SlotFinder(val factory: Identifier, val find: SlotFinder.(Entity) -> List<ItemStack>, val data: Any? = null){
    //constructor(factory: Identifier, find: SlotFinder.(Entity) -> ItemStack, data: Any? = null) = this(factory, {listOf(find(it))}, data)
    fun findSlot(entity: Entity) = this.find(entity)
}
