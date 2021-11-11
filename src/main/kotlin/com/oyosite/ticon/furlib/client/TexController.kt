package com.oyosite.ticon.furlib.client

import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import io.github.apace100.calio.data.SerializableData as SD
import io.github.apace100.calio.data.SerializableData.Instance as SDI
import com.oyosite.ticon.furlib.FurLib.RAND

@Suppress("UNCHECKED_CAST")
class TexController(val tex:Identifier, val r:Int, val g:Int, val b:Int, val a:Int, val illuminate:Boolean = false){
    constructor(data:SDI) : this(
        data.getId("tex"),
        extractCol(0,data),
        extractCol(1,data),
        extractCol(2,data),
        extractCol(3,data),
        data.getBoolean("illuminate")
    )
    constructor(nbt:NbtCompound) : this(Identifier(nbt.getString("tex")),nbt.getInt("r"),nbt.getInt("g"),nbt.getInt("b"),nbt.getInt("a"),nbt.getBoolean("illuminate"))
    companion object {
        private fun extractCol(index:Int,data:SDI) = extractCol(index, data.get("col")!! as List<Int>)
        private fun extractCol(index:Int, col:List<Int>):Int{
            return if(col.size<index-1) 255 else if(col.size>=index+4) if(col[index]>=col[index+4]) col[index] else RAND.nextInt(col[index],col[index+4]) else col[index]
        }
        fun toData(format:SD,tc:TexController):SDI{
            val sdi:SDI = format.Instance()
            with(tc){ sdi["tex"]=tex;sdi["col"]=listOf(r,g,b,a,r,g,b,a);sdi["illuminate"]=illuminate }
            return sdi
        }
    }
    fun serialize():NbtCompound{
        val nbt = NbtCompound()
        nbt.putString("tex",tex.toString())
        nbt.putInt("r",r);nbt.putInt("g",g);nbt.putInt("b",b);nbt.putInt("a",a)
        nbt.putBoolean("illuminate",illuminate)
        return nbt;
    }
}