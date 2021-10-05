package com.oyosite.ticon.furlib.client

import net.minecraft.util.Identifier
import io.github.apace100.calio.data.SerializableData as SD
import io.github.apace100.calio.data.SerializableData.Instance as SDI

class TexController(val tex:Identifier, val r:Float, val g:Float, val b:Float, val a:Float){
    constructor(data:SDI) : this(data.getId("tex"),data.getFloat("r"),data.getFloat("g"),data.getFloat("b"),data.getFloat("a"))
    companion object {fun toData(format:SD,tc:TexController):SDI{ val sdi:SDI = format.Instance();sdi["tex"]=tc.tex;sdi["r"]=tc.r;sdi["g"]=tc.g;sdi["b"]=tc.b;sdi["a"]=tc.a;return sdi }}
}