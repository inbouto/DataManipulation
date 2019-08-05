import java.security.MessageDigest
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or
import kotlin.experimental.xor
import kotlin.math.pow

fun ByteArray.sha() : ByteArray{
    val md = MessageDigest.getInstance("SHA-256")
    return md.digest(this)
}

//Checks whether certain bits of a byte are at 1
//if checkValue is 128, will check for the first bit. if at 192, will check for first and second bits, etc...
fun Byte.BinaryCheck(checkValue : Int) : Boolean{
    return (checkValue and this.toInt()) == checkValue
}

//checks if a certain bit at a given position is a 1 or a 0
//heaviest bit is bit 1, weakest bit is bit 8
//useful for sequential checks of a byte
fun Byte.reverseBitCheckInByte(pos : Int) : Boolean{
    return this.BinaryCheck(2.0.pow(8-pos).toInt())
}

fun ByteArray.xor(comp : ByteArray) : ByteArray {
    if(this.size != comp.size)
        throw Exception()
    val res = ByteArray(this.size)
    for(i in 0..this.size-1)
        res[i] = (this[i] xor comp[i])
    return res
}

fun ByteArray.and(comp : ByteArray) : ByteArray {
    if(this.size != comp.size)
        throw Exception()
    val res = ByteArray(this.size)
    for(i in 0..this.size-1)
        res[i] = (this[i] and comp[i])
    return res
}

fun ByteArray.or(comp : ByteArray) : ByteArray {
    if(this.size != comp.size)
        throw Exception()
    val res = ByteArray(this.size)
    for(i in 0..this.size-1)
        res[i] = (this[i] or comp[i])
    return res
}

fun ByteArray.inv() : ByteArray {
    val res = ByteArray(this.size)
    for(i in 0..this.size-1)
        res[i] = this[i].inv()
    return res
}


fun ByteArray.checkBit(index : Int) : Boolean{
    if(index/8 > this.size)
        throw Exception()
    return this[index/8].reverseBitCheckInByte(index%8)
}

fun ByteArray.isMinValue() : Boolean{
    this.forEach {
        if(!(it.toInt() == 0))
            return false
    }
    return true
}

fun ByteArray.checkMatch(prefix : ByteArray, wcMask : ByteArray) : Boolean{
    if(!(this.size==prefix.size && this.size==wcMask.size)){
        throw Exception()
    }
    val res = prefix.xor(this)      //This line generates 0000... if the ByteArray matches the prefix + mask
                            .inv()                   //Mask XOR (Mask AND (NOT (Prefix XOR Challenger)))
                            .and(wcMask.inv())       //note that this formula assumes the mask has 1s where we're supposed to match, which is the inverse of our wildcard mask,
                            .xor(wcMask.inv())       //hence the need to invert it each time
    return res.isMinValue()
}

fun ByteArray.amountOfOnes() : Int{
    var ones = 0
    for(i in 0 until 256)
        if(this.checkBit(i))
            ones++
    return ones
}







