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
 * Generates a sha-256 hash of a [ByteArray].
 * @see [MessageDigest]
 * @return the sha-256 hash of the [ByteArray]
 */
fun ByteArray.sha() : ByteArray{
    return MessageDigest.getInstance("SHA-256").digest(this)
}

/**
 * Checks whether certain bits of a byte are at 1 or not.
 * For example, if checkValue is 128, will check for the first bit (10000000). If at 192 (11000000), will check for first and second bits, etc...
 * Useful when used through [Byte.checkBit].
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
 * Checks whether a certain bit at a given position in the byte is a 1 or a 0.
 * Useful for sequential checks of bits in a byte.
 * @see [Byte.BinaryCheck]
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
 * Does a bitwise XOR (exclusive OR) comparison between two [ByteArrays][ByteArray].
 * Both [ByteArray]s have to be the same size.
 * @see [Byte.xor]
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
 * Does a bitwise AND comparison between two [ByteArrays][ByteArray].
 * Both [ByteArray]s have to be the same size.
 * @see [Byte.and]
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
 * Does a bitwise OR comparison between two [ByteArrays][ByteArray].
 * Both [ByteArray]s have to be the same size.
 * @see [Byte.or]
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
 * Does a bitwise inversion (NOT operation) of [this].
 * @see [Byte.inv]
 * @return inverted bit values for [this]
 */
fun ByteArray.inv() : ByteArray {
    val res = ByteArray(this.size)

    for(i in 0 until this.size)     //simply does a byte-per-byte inversion and returns the array of results
        res[i] = this[i].inv()
    return res
}

/**
 * Checks whether a given bit of a [ByteArray] is a 1 or a 0.
 * @see [Byte.checkBit]
 * @param index the position of the bit to check (max value is 8 times [this.size])
 * @return true if the bit is a 1, false if it's a 0
 * @throws IndexOutOfBoundsException when [index] is lesser than 0 or greater than 8 * [this.size]
 */
fun ByteArray.checkBit(index : Int) : Boolean{
    if(index/8 > this.size || index < 0)
        throw IndexOutOfBoundsException()
    return this[index/8].checkBit((index%8)+1)
}

/**
 * Checks whether [this] contains only bits at 0.
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
 * Checks if [this] matches a condition based on an [wildcard mask][wcMask] and a prefix.
 * Note : while the concept of wildcard mask comes from network engineering, it is perfectly applicable for any array of bytes.
 * For more information, see https://en.wikipedia.org/wiki/Wildcard_mask
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
/**
 * @return the amount of bits in [this] with a value of 1
 */
fun ByteArray.amountOfOnes() : Int{
    var ones = 0
    for(i in 0 until 256)
        if(this.checkBit(i))
            ones++
    return ones
}

/**
 * Enables us to directly compare two [ByteArrays][ByteArray] (with > or < or == or !=).
 * @see [UByte.compareTo]
 * @param bytes [ByteArray] against which to compare [this]
 * @return -1 if [this] < [bytes], 1 if [this] > [bytes], 0 if [this] == [bytes]
 * @throws DifferentArraySizesException if [bytes] and [this] have different sizes
 */
operator fun ByteArray.compareTo(bytes: ByteArray): Int {

    if(this.size != bytes.size)
        throw DifferentArraySizesException()

    for(i in 0 until this.size){                        //goes through the array, starting with the heaviest bytes, then compares the unsigned values of the corresponding bytes.
        if(this[i].toUByte() < bytes[i].toUByte())          //repeats either until a difference is found, or we hit the end of the arrays (therefor the arrays are equal)
            return -1
        else if(this[i].toUByte() > bytes[i].toUByte())
            return 1
    }
    return 0
}

/**
 * Converts a [ByteArray] into a readable hexadecimal [String].
 * @return [this] with each [Byte] converted into a 2 digit hexadecimal string
 */
fun ByteArray.toHexFormat() : String {
    var res = ""
    this.forEach {
        res += String.format("%02X", it)
    }
    return res
}

/**
 * Saves a [ByteArray] into a file. Will erase the file if it exists.
 * @see [File.writeBytes]
 * @param path path to the file to write [this] into
 */
fun ByteArray.toFile(path : String){
    var file = File(path)
    file.writeBytes(this)
}

/**
 * Saves a [ByteArray] into a file. Will erase the file if it exists.
 * @see [File.readBytes]
 * @param path path to the file to read bytes from
 * @return a [ByteArray] containing the read [Bytes][Byte]
 */
fun bytesFromFile(path : String) : ByteArray{
    var file = File(path)
    return file.readBytes()
}

/**
 * Prints a user-friendly display of [this].
 * Each line is 32 bytes long, cut into 4 8-bytes long blocks.
 * @param basic if true, will only print [this] in a single, unformatted hexadecimal string. false by default
 */
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

/**
 * Generates an indexed hash. Simply appends the [index] and generates a hash from it.
 * Warning : this method will make attempts at brute forcing a hash a lot less quantum resilient
 *
 * @param index value to append to [this]. Must be between 0 and 255 included
 * @return the sha256 hash of the concatenation of [this] and [index]
 */
fun ByteArray.indexedHash(index : Int) : ByteArray{

    return (this + index.toUInt().toByteArray()).sha()
}

/**
 * Turns an [Int] into a 4-long [ByteArray]
 * @see [ByteArray.toInt]
 * @return the [ByteArray] containing all 4 bytes previously contained in [this]. First [Byte] is heaviest, last one is lightest
 */
fun UInt.toByteArray() : ByteArray{
    val res = ByteArray(4)
    for(i in 0 until 4)
        res[3-i] = (this/(256.0.pow(i)).toUInt()).toUInt().toByte()
    return res
}

/**
 * Turns the last 4 [Bytes][Byte] of a [ByteArray] into an [Int]
 * @see [Int.toByteArray]
 * @return the [Int] value equivalent to the last 4 [Bytes][Byte] of the [ByteArray]
 */
fun ByteArray.toUInt() : UInt{


    var res = 0u
    for(i in 0 until 4) {
        if (i == 3)
            res += this[3 - i].toUInt() * 256.0.pow(i).toUInt()       //Only the heaviest byte should be signed. Every other byte has to be turned into a UByte.
        else
            res += this[3 - i].toUByte().toUInt() * 256.0.pow(i).toUInt()
    }
    return res
}

/**
 * Returns the [ByteArray] in dotted decimal form, similar to an IP address
 *
 * @return the decimal representation of the [ByteArray]
 */
fun ByteArray.toDecFormat() : String{

    var res = ""
    this.forEach {
        res += ".${it.toUByte()}"
    }
    return res
}

/**
 * Returns the [ByteArray] in dotted binary form. This is horribly unoptimized. Please use only for testing purposes.
 *
 * @return the binary representation of the [ByteArray]
 */
fun ByteArray.toBinaryFormat() : String{
    var res = ""
    for(i in 0 until this.size*8){
        if(this.checkBit(i))
            res += '1'
        else
            res += '0'
        if(i%8==7)
            res+='.'
    }
    return res
}
