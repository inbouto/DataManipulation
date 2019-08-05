
import java.io.File
import kotlin.random.Random





//Lamport key based signature scheme


fun quick_send_message(message : String, path : String){
    println("Generating key pair")
    val keys = generateKeys()
    println("Saving private key to $path\\private_key")
    keys.first.toFile("$path\\private_key")
    println("Saving public key to $path\\public_key")
    keys.second.toFile("$path\\public_key")
    println("Generating signature")
    val sig = keys.first.sign(message)
    println("saving signature at $path\\signature")
    sig.toFile("$path\\signature")
    print("Process complete. Send the signature, message and public key to the recipient !")
}

fun quick_read_message(path : String){
    println("Reading message at $path\\message")
    val msg = File("$path\\message").readText()
    println("Retrieving signature at $path\\signature")
    val sig = File("$path\\signature").readBytes()

    println("Retrieving public key at $path\\pub")
    val pub : LPublicKey = lamportFromFile("$path\\pub") as LPublicKey
    println("Verifying signature...")
    if (pub.verify(msg, sig))
        println("Message authenticated !")
    else
        println("Authentication failed")
}

open class LamportKey(val key0 : ByteArray, val key1 : ByteArray){
    companion object {
        const val BLOCK_SIZE = 32
        const val KEY_LENGTH = 256*BLOCK_SIZE
    }
}

class LSecretKey(key0 : ByteArray, key1 : ByteArray) : LamportKey(key0, key1){

}

class LPublicKey(key0 : ByteArray, key1 : ByteArray) : LamportKey(key0, key1){

}

fun generateKeys(): Pair<LSecretKey, LPublicKey> {
    //Keys are comprised of 256 blocks of 32 bytes.
    //the private key (secret) is fully randomized.
    //the public key is generated as follow : each block is the private key's corresponding block's sha256 hash
    val secret1= Random.Default.nextBytes(ByteArray(LamportKey.KEY_LENGTH))
    val secret0= Random.Default.nextBytes(ByteArray(LamportKey.KEY_LENGTH))
    val public1 = secret1.hashBlockByBlock(LamportKey.BLOCK_SIZE)
    val public0 = secret0.hashBlockByBlock(LamportKey.BLOCK_SIZE)

    return Pair(LSecretKey(secret0, secret1), LPublicKey(public0, public1))
}


fun LPublicKey.checkMessage(inputFile : String) : Boolean{
    val message = File("$inputFile.txt").readText()
    val signature = File("$inputFile.sig").readBytes()
    return verify(message, signature)
}
fun LPublicKey.verify(message : String, sig : ByteArray) : Boolean{

    //Step one : generating hashed blocks from the signature :

    val hashedSig = sig.hashBlockByBlock(LamportKey.BLOCK_SIZE)



    //Step two : generating a hash of the message :
    val hashedMsg =
        message.toByteArray()
            .sha()
    //Step three : comparing the hashed signature to key0 and key1 of the public key
    for(i in 0 until (HASH_SIZE)*8){        //For each bit of the sha256 hash of the message...
        if (hashedMsg.checkBit(i)) {  //If we find '1'...
            if (!(hashedSig.getBlock(i, LamportKey.BLOCK_SIZE) contentEquals
                        (this.key1.getBlock(i, LamportKey.BLOCK_SIZE)))  ) {    //If the hash of the signature doesnt match the same block of the public key...
                sig.getBlock(i, LamportKey.BLOCK_SIZE).printHex()
                hashedSig.getBlock(i, LamportKey.BLOCK_SIZE).printHex()
                this.key1.getBlock(i, LamportKey.BLOCK_SIZE).printHex()
                return false    //Failed authentication
            }
        }
        else {
            if (!(hashedSig.getBlock(i, LamportKey.BLOCK_SIZE) contentEquals
                        (this.key0.getBlock(i, LamportKey.BLOCK_SIZE)))  ) {

                return false    //failed authentication
            }
        }
    }

    return true;    //If every block was a match, authentication successful
}


fun LSecretKey.generateMessageFile(message : String, outputFile : String){
    File("$outputFile.sig").writeBytes(this.sign(message))
    File("$outputFile.txt").writeText(message)
}

fun LSecretKey.sign(message : String) : ByteArray{
    var sig = ByteArray(LamportKey.KEY_LENGTH)
    val msgHash = message.toByteArray().sha()
    for(i in 0 until (LamportKey.BLOCK_SIZE)*8) {
        if(msgHash.checkBit(i))
            sig.setBlock(i, LamportKey.BLOCK_SIZE, key1.getBlock(i, LamportKey.BLOCK_SIZE))
        else
            sig.setBlock(i, LamportKey.BLOCK_SIZE, key0.getBlock(i, LamportKey.BLOCK_SIZE))

    }
    return sig
}



        /*Lamport key operations*/

fun LamportKey.toFile(path : String){
    var file = File(path)
    file.writeBytes(key0)
    file.appendBytes(key1)
}

fun lamportFromFile(path : String) : LamportKey{
    var file = File(path)
    val full = file.readBytes()
    return LamportKey(full.copyOfRange(0, (256*32)), full.copyOfRange(256*32, full.size))
}

fun lSecretFromFile(path : String) : LSecretKey{
    var file = File(path)
    val full = file.readBytes()
    return LSecretKey(full.copyOfRange(0, (256*32)), full.copyOfRange(256*32, full.size))
}

fun lPublicFromFile(path : String) : LPublicKey{
    var file = File(path)
    val full = file.readBytes()
    return LPublicKey(full.copyOfRange(0, (256*32)), full.copyOfRange(256*32, full.size))
}

fun LamportKey.printHex(){
    println("key0")
    key0.printHex()
    println("\n\nkey1")
    key1.printHex()
}

//sends a copy of a 32 bytes bock.
//First block is block 0
fun LamportKey.getBlock(keyID: Int, blockNum: Int) : ByteArray{
    return if(keyID == 1)
        key1.copyOfRange(blockNum*LAMPORT_BLK_SIZE, (blockNum+1)*LAMPORT_BLK_SIZE -1)
    else
        key0.copyOfRange(blockNum*LAMPORT_BLK_SIZE, (blockNum+1)*LAMPORT_BLK_SIZE -1)
}

fun LamportKey.setBlock(keyID : Boolean, blockNum : Int, block : ByteArray){
    if (block.size != 32)
        return
    if(keyID)
        block.copyInto(key1, blockNum*LAMPORT_BLK_SIZE, blockNum*LAMPORT_BLK_SIZE, (blockNum+1)*LAMPORT_BLK_SIZE-1)
    else
        block.copyInto(key0, blockNum*LAMPORT_BLK_SIZE, blockNum*LAMPORT_BLK_SIZE, (blockNum+1)*LAMPORT_BLK_SIZE-1)
}

fun LamportKey.toByteArray() : ByteArray{
    return key0 + key1
}