package com.oyosite.ticon.furlib.client

import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import io.github.apace100.calio.data.SerializableData as SD
import io.github.apace100.calio.data.SerializableData.Instance as SDI

@Suppress("UNCHECKED_CAST")
class TexController(val tex:Identifier, val col:TexColor, val illuminate:Boolean = false, val alias:String){
    constructor(data:SDI) : this(
        data.getId("tex"),
        data.get("col") as TexColor,
        data.getBoolean("illuminate"),
        data.getString("alias")
    )
    constructor(nbt:NbtCompound) : this(Identifier(nbt.getString("tex")), TexColor.fromNbt(nbt),nbt.getBoolean("illuminate"), nbt.getString("alias"))
    companion object {
        //private fun extractCol(index:Int,data:SDI) = extractCol(index, data.get("col")!! as List<Int>)
        //private fun extractCol(index:Int, col:List<Int>):Int{
        //    return if(col.size<=index) 255 else if(col.size>index+4) if(col[index]>=col[index+4]) col[index] else RAND.nextInt(col[index],col[index+4]) else col[index]
        //}
        fun toData(format:SD,tc:TexController):SDI{
            val sdi:SDI = format.Instance()
            with(tc){ sdi["tex"]=tex;sdi["col"]=col;sdi["illuminate"]=illuminate;sdi["alias"]=alias }
            return sdi
        }
    }
    fun serialize():NbtCompound{
        val nbt = NbtCompound()
        nbt.putString("tex",tex.toString())
        nbt.putInt("r",col.r.toInt());nbt.putInt("g",col.g.toInt());nbt.putInt("b",col.b.toInt());nbt.putInt("a",col.a.toInt())
        nbt.putBoolean("illuminate",illuminate)
        //if(alias!==tex.toString())
        nbt.putString("alias",alias)
        return nbt
    }
}