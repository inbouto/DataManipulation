import data_manipulation.basics.DifferentArraySizesException
import data_manipulation.basics.InvalidBitPositionException
import data_manipulation.basics.InvalidByteValueException
import java.io.File
import java.security.MessageDigest
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or
import kotlin.experimental.xor
import kotlin.math.pow

/**
 * @author Inbouto
 */



/**
 * Generates a sha-256 hash of a [ByteArray]
 * Uses [MessageDigest]
 * @return the sha-256 hash of the [ByteArray]
 */
fun ByteArray.sha() : ByteArray{
    return MessageDigest.getInstance("SHA-256").digest(this)
}

/**
 * Checks whether certain bits of a byte are at 1 or not
 * For example, if checkValue is 128, will check for the first bit (10000000). If at 192 (11000000), will check for first and second bits, etc...
 * Useful when used through [Byte.checkBit]
 * @param checkValue unsigned decimal value of the binary mask that will decide which bits are to be checked for 1
 * @return true if all checked bits are 1s, false if at least one bit is a 0. will always return true if [checkValue] is 0
 * @trows [InvalidByteValueException] if given [checkValue] is lesser than 0 or greater than 255
 */
fun Byte.BinaryCheck(checkValue : Int) : Boolean{
    if(checkValue < 0 || checkValue > 255)
        throw InvalidByteValueException()
    return (checkValue and this.toInt()) == checkValue
}

/**
 * Checks whether a certain bit at a given position in the byte is a 1 or a 0
 * Useful for sequential checks of bits in a byte
 * Uses [Byte.BinaryCheck]
 * @param position the position of the bit to check between 1 and 8 (heaviest bit is bit 1, lightest bit is bit 8)
 * @return true if bit is a 1, false if bit is a 0
 * @throws [InvalidBitPositionException] when [position] is lesser than 1 or greater than 8
 */
fun Byte.checkBit(position : Int) : Boolean{
    if(position < 1 || position > 8)
        throw InvalidBitPositionException()
    return this.BinaryCheck(2.0.pow(8-position).toInt())
}

/**
 * Does a bitwise XOR (exclusive OR) comparison between two [ByteArray]s
 * Both [ByteArray]s have to be the same size
 * Uses [Byte.xor]
 * @param comp [ByteArray] to compare with [this]
 * @return the result of the bitwise XOR operation between [this] and [comp]
 * @throws DifferentArraySizesException
 */
fun ByteArray.xor(comp : ByteArray) : ByteArray {
    if(this.size != comp.size)
        throw DifferentArraySizesException()

    val res = ByteArray(this.size)
    for(i in 0 until this.size)     //simply does a byte-per-byte xor comparison and returns the array of results
        res[i] = (this[i] xor comp[i])
    return res
}

/**
 * Does a bitwise AND comparison between two [ByteArray]s.
 * Both [ByteArray]s have to be the same size
 * Uses [Byte.and]
 * @param comp [ByteArray] to compare with [this]
 * @return the result of the bitwise AND operation between [this] and [comp]
 * @throws DifferentArraySizesException
 */
fun ByteArray.and(comp : ByteArray) : ByteArray {
    if(this.size != comp.size)
        throw DifferentArraySizesException()

    val res = ByteArray(this.size)
    for(i in 0 until this.size)     //simply does a byte-per-byte and comparison and returns the array of results
        res[i] = (this[i] and comp[i])
    return res
}


/**
 * Does a bitwise OR comparison between two [ByteArray]s.
 * Both [ByteArray]s have to be the same size
 * Uses [Byte.or]
 * @param comp [ByteArray] to compare with [this]
 * @return the result of the bitwise OR operation between [this] and [comp]
 * @throws DifferentArraySizesException
 */
fun ByteArray.or(comp : ByteArray) : ByteArray {
    if(this.size != comp.size)
        throw Exception()
    val res = ByteArray(this.size)
    for(i in 0 until this.size)     //simply does a byte-per-byte or comparison and returns the array of results
        res[i] = (this[i] or comp[i])
    return res
}

/**
 * Does a bitwise inversion (NOT operation) of [this]
 * Uses [Byte.inv]
 * @return inverted bit values for [this]
 */
fun ByteArray.inv() : ByteArray {
    val res = ByteArray(this.size)

    for(i in 0 until this.size)     //simply does a byte-per-byte inversion and returns the array of results
        res[i] = this[i].inv()
    return res
}

/**
 * Checks whether a given bit of a [ByteArray] is a 1 or a 0
 * Uses [Byte.checkBit]
 * @param index the position of the bit to check (max value is 8 times [this.size])
 * @return true if the bit is a 1, false if it's a 0
 * @throws IndexOutOfBoundsException when [index] is lesser than 0 or greater than 8 * [this.size]
 */
fun ByteArray.checkBit(index : Int) : Boolean{
    if(index/8 > this.size || index < 0)
        throw IndexOutOfBoundsException()
    return this[index/8].checkBit(index%8)
}

/**
 * Checks whether a [this] contains only bits at 0
 * @return true if [this] only contains bits at 0, false if [this] contains at least one bit at 1
 */
fun ByteArray.isMinValue() : Boolean{
    this.forEach {
        if((it.toUByte() != UByte.MIN_VALUE))
            return false
    }
    return true
}

/**
 * Checks if [this] matches a condition based on an [wildcard mask][wcMask] and a prefix
 * Note : while the concept of wildcard mask comes from network engineering, it is perfectly applicable for any array of bytes.
 * See https://en.wikipedia.org/wiki/Wildcard_mask
 * @param prefix The reference array against which to test the match
 * @param wcMask will determine which bits to test (will test a given bit of [this] against the one on the same position in [prefix] if at 0, will not test if at 1)
 * @return true if the bits selected by [wcMask] in [this] and [prefix] are the same, false if there is at least a single difference
 * @throws DifferentArraySizesException if [this], [prefix] and [wcMask] have a different size
 */
fun ByteArray.checkMatch(prefix : ByteArray, wcMask : ByteArray) : Boolean{
    if(!(this.size==prefix.size && this.size==wcMask.size)){
        throw DifferentArraySizesException()
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


operator fun ByteArray.compareTo(bytes: ByteArray): Int {

    if(this.size != bytes.size)
        throw DifferentArraySizesException()

    for(i in 0 until this.size){
        if(this[i].toUByte() < bytes[i].toUByte())
            return -1
        else if(this[i].toUByte() > bytes[i].toUByte())
            return 1
    }
    return 0
}

fun ByteArray.toHexFormat() : String {
    var res = ""
    this.forEach {
        res += String.format("%02X", it)
    }
    return res
}

fun ByteArray.toFile(path : String){
    var file = File(path)
    file.writeBytes(this)
}


fun bytesFromFile(path : String) : ByteArray{
    var file = File(path)
    return file.readBytes()
}

fun ByteArray.printHex(basic : Boolean = false){
    var count = 0
    this.forEach {
        val st = String.format("%02X", it)
        print("$st")
        if(!basic) {
            count++
            if (count % 32 == 0)
                println("\n-------------------------------------------------------------------------------------")
            else if (count % 8 == 0)
                print(" | ")
            else if (count % 2 == 0)
                print(".")
        }
    }

}