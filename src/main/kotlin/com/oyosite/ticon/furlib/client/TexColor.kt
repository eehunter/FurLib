package com.oyosite.ticon.furlib.client

import com.google.gson.JsonElement
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import java.util.*

class TexColor(color:List<UByte>) {
    constructor(str:String) : this(parseFromString(str))
    val col = MutableList(4, color::get)
    var r:UByte
        set(value) {col[0]=value}
        get() = col[0]
    var g:UByte
        set(value) {col[1]=value}
        get() = col[1]
    var b:UByte
        set(value) {col[2]=value}
        get() = col[2]
    var a:UByte
        set(value) {col[3]=value}
        get() = col[3]
    fun copy(other:TexColor) = copy(other.col)
    fun copy(other:List<UByte>) {for(i in 0..3) col[i] = other[i]}
    fun copyOf() = TexColor(col)
    override fun toString(): String = col.joinToString("") { it.toString(16).uppercase() }

    companion object{
        private val rand = Random()
        fun send(buf: PacketByteBuf, tc: TexColor)=tc.col.map(UByte::toInt).forEach(buf::writeByte)
        fun receive(buf: PacketByteBuf):TexColor = TexColor(MutableList(4){buf.readByte().toUByte()})
        fun read(json: JsonElement):TexColor = TexColor(json.asString)
        fun fromNbt(nbt:NbtCompound) = TexColor(listOf(nbt.getInt("r"),nbt.getInt("g"),nbt.getInt("b"),nbt.getInt("a")).map(Int::toUByte))
        private fun parseFromString(str:String):List<UByte>{
            val arr = str.split(" ")
            val c = arr[0]
            var col:List<UByte> = parseColor(c)
            //println(c)
            //println(col.joinToString(transform = UByte::toString))
            if(arr.size>1){
                val c1 = parseColor(arr[1])
                //println(arr[1])
                col = List(4){ if (c1[it]>col[it]) (col[it]+rand.nextInt((c1[it]-col[it]).toInt()).toUInt()).toUByte() else col[it] }
                //println(col.joinToString(transform = UByte::toString))
            }
            return col
        }
        fun parseColor(str:String):List<UByte>{
            val i = str.toInt(16)
            //println(str.length)
            return when(str.length){
                8->listOf(i shr 24, i shr 16, i shr 8, i)
                6->listOf(i shr 16, i shr 8, i, 255)
                4->listOf(i shr 8, i shr 8, i shr 8, i)
                2->listOf(i,i,i,255)
                else -> List(4){255}
            }.map(Int::toUByte)
        }
    }
}