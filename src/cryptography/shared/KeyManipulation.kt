import cryptography.shared.IncorrectBlockSizeException
import kotlin.Exception

/**
 * @author Inbouto
 */


/**
 * Retrieves a "block" of data of any blockSize for a [ByteArray]
 * @see [setBlock]
 * @param blockIndex which block to retrieve. The block will start at index [blockIndex] * [blockSize]
 * @param blockSize size of blocks to use
 * @return
 * @throws IncorrectBlockSizeException if the size of the [ByteArray] is not a multiple of [blockSize]
 */
fun ByteArray.getBlock(blockIndex : Int, blockSize : Int) : ByteArray{
    if(this.size%blockSize != 0)
        throw IncorrectBlockSizeException()

    return this.copyOfRange(blockSize*blockIndex, blockSize*(blockIndex+1))
}

/**
 * Sets a "block" of data of any blockSize in a [ByteArray]
 * @see [getBlock]
 * @param blockIndex which block to update. The block will start at index [blockIndex] * [blockSize]
 * @param blockSize blockSize of blocks to use
 * @param block_from block to copy from
 * @return
 * @throws IncorrectBlockSizeException if the blockSize of the [ByteArray] is not a multiple of the size of [block_from]
 */
fun ByteArray.setBlock(position : Int, block_from : ByteArray){
    if(this.size%block_from.size != 0)
        throw IncorrectBlockSizeException()

    block_from.copyInto(this, position * block_from.size)
}


/**
 * Hashes [this] block by block and puts each hash one after another in a [ByteArray]
 *
 * @param blk_size size of blocks to use. Probably should be 32
 * @return the [ByteArray] with all hashes concatenated in the right order
 */
fun ByteArray.hashBlockByBlock(blk_size : Int) : ByteArray{
    if(this.size%blk_size != 0) //If the ByteArray isnt an integer number of blocks long, stop
        throw Exception()
    var res = ByteArray(this.size)
    for(i in 0 until (this.size/blk_size)){
        res.setBlock(i, this.getBlock(i, blk_size).sha())
    }

    return res
}