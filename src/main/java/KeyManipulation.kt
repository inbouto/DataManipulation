import java.io.File
import kotlin.Exception

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

fun ByteArray.toHexFormat() : String {
    var res = ""
    this.forEach {
        res += String.format("%02X", it)
    }
    return res
}


fun keyFromFile(path : String) : ByteArray{
    var file = File(path)
    return file.readBytes()
}

fun ByteArray.toFile(path : String){
    var file = File(path)
    file.writeBytes(this)
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