package com.oyosite.ticon.furlib.client

import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import io.github.apace100.calio.data.SerializableData as SD
import io.github.apace100.calio.data.SerializableData.Instance as SDI
import com.oyosite.ticon.furlib.FurLib.RAND

class TexController(val tex:Identifier, var r:Int, var g:Int, val b:Int, val a:Int, val illuminate:Boolean = false){
    constructor(data:SDI) : this(
        data.getId("tex"),
        if(!data.isPresent("rm")||data.getInt("rm")<=data.getInt("r"))data.getInt("r")else(RAND.nextInt(data.getInt("r"),data.getInt("rm"))),
        if(!data.isPresent("gm")||data.getInt("gm")<=data.getInt("g"))data.getInt("g")else(RAND.nextInt(data.getInt("g"),data.getInt("gm"))),
        if(!data.isPresent("bm")||data.getInt("bm")<=data.getInt("b"))data.getInt("b")else(RAND.nextInt(data.getInt("b"),data.getInt("bm"))),
        if(!data.isPresent("am")||data.getInt("am")<=data.getInt("a"))data.getInt("a")else(RAND.nextInt(data.getInt("a"),data.getInt("am"))),
        data.getBoolean("illuminate")
    )
    constructor(nbt:NbtCompound) : this(Identifier(nbt.getString("tex")),nbt.getInt("r"),nbt.getInt("g"),nbt.getInt("b"),nbt.getInt("a"),nbt.getBoolean("illuminate"))
    companion object {
        fun toData(format:SD,tc:TexController):SDI{
            val sdi:SDI = format.Instance();
            //sdi["tex"]=tc.tex;sdi["r"]=tc.r;sdi["g"]=tc.g;sdi["b"]=tc.b;sdi["a"]=tc.a;sdi["illuminate"]=tc.illuminate;
            with(tc){ mapOf("tex" to tex, "r" to r, "g" to g, "b" to b, "a" to a, "illuminate" to illuminate, "rm" to r, "gm" to g, "bm" to b, "am" to a) }.entries.forEach{sdi[it.key]=it.value}

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