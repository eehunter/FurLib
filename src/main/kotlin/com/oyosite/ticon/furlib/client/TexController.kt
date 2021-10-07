package com.oyosite.ticon.furlib.client

import net.minecraft.util.Identifier
import io.github.apace100.calio.data.SerializableData as SD
import io.github.apace100.calio.data.SerializableData.Instance as SDI

class TexController(val tex:Identifier, val r:Int, val g:Int, val b:Int, val a:Int, val illuminate:Boolean = false){
    constructor(data:SDI) : this(data.getId("tex"),data.getInt("r"),data.getInt("g"),data.getInt("b"),data.getInt("a"),data.getBoolean("illuminate"))
    companion object {fun toData(format:SD,tc:TexController):SDI{ val sdi:SDI = format.Instance();sdi["tex"]=tc.tex;sdi["r"]=tc.r;sdi["g"]=tc.g;sdi["b"]=tc.b;sdi["a"]=tc.a;sdi["illuminate"]=tc.illuminate;return sdi }}
}