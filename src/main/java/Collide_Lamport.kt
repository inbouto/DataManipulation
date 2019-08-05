import java.io.File
import java.lang.Exception
import java.math.BigInteger
import kotlin.math.pow

const val HASH_SIZE = 32

fun forge(pubKey : LPublicKey, signatures : ArrayList<ByteArray>, messages : ArrayList<String>, message : String, outputFiles : String){

    if(signatures.size != messages.size)
        throw Exception()

    //First we generate the hashes from the messages and gather the signatures

    val hashes = getHashes(messages)

    //Then we generate a wildcard mask & "prefix" of the hashes

    val mask = getMask(hashes)
    val prefix = mask.inv().and(hashes[0])  //Take any hash, run it through the (inverted) mask, you get a prefix of constrained bits

    //Now we have constraint bits as well as a wildcard mask.
    //We need to generate the partial private key from the signatures by looking at all the hashes we have

    val partialPriv = genPartialKey(hashes, signatures)


    //Finally, for a given message, we keep generating hashes (with salt) until we find a hash that fits our prefix+mask

    val finalMessage = bruteForce(message, prefix, mask)

    println("Forgery complete !")
    println("complete message : \"$finalMessage\"")

    //With that message and our partial private key, we can now generate our forged signature :

    partialPriv.generateMessageFile(finalMessage, outputFiles)


    //final check ; verifying the message with signature and public key


    println("Checking forgery...")
    if(pubKey.checkMessage(outputFiles))
        println("Forgery successful !")
    else
        println("Forgery failed !")
}

fun bruteForce(message: String, prefix: ByteArray, mask: ByteArray): String {
    var salt = BigInteger.valueOf(0)
    val attempts = 2.0.pow((256 - mask.amountOfOnes()).toDouble()).toInt()
    println("average attempts : $attempts")
    while(!"$message $salt".toByteArray().sha().checkMatch(prefix, mask)) {   //While our message + salt does not generate a suitable hash
        if(salt.mod((attempts/100).toBigInteger()) == 0.toBigInteger())
            println("${100.toBigInteger()*salt/attempts.toBigInteger()}% complete...")
        salt++      //try another salt
    }
    return "$message $salt"
}

fun genPartialKey(hashes: ArrayList<ByteArray>, signatures: ArrayList<ByteArray>): LSecretKey {

    val key1 = ByteArray(LamportKey.BLOCK_SIZE * 256)
    val key0 = ByteArray(LamportKey.BLOCK_SIZE * 256)

    for(i in 0 until hashes.size)
        for(j in 0 until 256)
            if(hashes[i].checkBit(j))
                key1.setBlock(j, LamportKey.BLOCK_SIZE, signatures[i].getBlock(j, LamportKey.BLOCK_SIZE))
            else
                key0.setBlock(j, LamportKey.BLOCK_SIZE, signatures[i].getBlock(j, LamportKey.BLOCK_SIZE))
    return LSecretKey(key0, key1)
}

fun getMask(hashes: ArrayList<ByteArray>): ByteArray{
    val mask = ByteArray(HASH_SIZE)
    for(i in 1 until hashes.size)
        (hashes[i].xor(hashes[i-1]))
            .or(mask)
            .copyInto(mask)
    println("${mask.amountOfOnes()} positive bits in the mask.\nThat's ${100*mask.amountOfOnes().toDouble()/(mask.size*8).toDouble()}% of the mask")
    return mask
}

fun getHashes(messages: ArrayList<String>): ArrayList<ByteArray> {
    val hashes = ArrayList<ByteArray>()

    messages.forEach{
        hashes.add(it.toByteArray().sha())
    }
    return hashes
}



