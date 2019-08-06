import kotlin.Exception

/**
 * @author Inbouto
 */


const val LAMPORT_BLK_SIZE = 32

fun ByteArray.getBlock(position : Int, size : Int) : ByteArray{
    if(this.size%size != 0) {
        println("${this.size} ; $size")
        throw Exception()
    }
    return this.copyOfRange(size*position, size*(position+1))
}

fun ByteArray.setBlock(position : Int, size : Int, block_from : ByteArray){
    if(block_from.size != size)
        throw Exception()

    block_from.copyInto(this, position * size)
}



fun ByteArray.hashBlockByBlock(blk_size : Int) : ByteArray{
    if(this.size%blk_size != 0) //If the ByteArray isnt an integer number of blocks long, stop
        throw Exception()
    var res = ByteArray(this.size)
    for(i in 0..(this.size/blk_size)-1){
        res.setBlock(i, blk_size, this.getBlock(i, blk_size).sha())
    }

    return res
}