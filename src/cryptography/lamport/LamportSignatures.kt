package cryptography.lamport
import LAMPORT_BLK_SIZE
import checkBit
import getBlock
import hashBlockByBlock
import printHex
import setBlock
import sha
import java.io.File
import kotlin.random.Random

/**
 * @author Inbouto
 * Lamport key based signature scheme
 * For more information on how Lamport signatures work, watch this : https://www.youtube.com/watch?v=IJquEYhiq_U&list=PLUl4u3cNGP61KHzhg3JIJdK08JLSlcLId&index=1
 */



/**
 * Lamport signature scheme key. This contains a single key (public or private) stored as two [ByteArrays][ByteArray], one containing the '0' key, the other containing the '1' key. See https://en.wikipedia.org/wiki/Lamport_signature for more info.
 *
 * @property key0 half of the key used to code for 0
 * @property key1 half of the key used to code for 1
 */
open class LamportKey(val key0 : ByteArray, val key1 : ByteArray){

    /**
     * @property BLOCK_SIZE Size of the Lamport Key block. Should be equal to the output size of the hash function used (in this case, sha256, 32 bytes)
     * @property KEY_LENGTH Total size of a half-key in [Bytes][Byte].
     */
    companion object {
        const val BLOCK_SIZE = 32
        const val KEY_LENGTH = 256* BLOCK_SIZE
        /**
         * Generates a [Secret][LSecretKey]/[Public][LPublicKey] key pair
         *
         * @return a [Pair] with a [LSecretKey] as [Pair.first] and the corresponding [LPublicKey] as [Pair.second]
         */
        fun generateKeys(): Pair<LSecretKey, LPublicKey> {
            //Keys are comprised of 256 blocks of 32 bytes.
            //the private key (secret) is fully randomized.
            //the public key is generated as follow : each block is the private key's corresponding block's sha256 hash
            val secret1= Random.Default.nextBytes(ByteArray(KEY_LENGTH))
            val secret0= Random.Default.nextBytes(ByteArray(KEY_LENGTH))
            val public1 = secret1.hashBlockByBlock(BLOCK_SIZE)
            val public0 = secret0.hashBlockByBlock(BLOCK_SIZE)

            return Pair(LSecretKey(secret0, secret1), LPublicKey(public0, public1))
        }


        /**
         * Loads a [LamportKey] stored in a file using the reversed scheme of [LamportKey.toFile]
         *
         * @param path the path to the file containing the key
         * @return the key stored in the file
         */
        fun lamportFromFile(path : String) : LamportKey {
            var file = File(path)
            val full = file.readBytes()
            return LamportKey(full.copyOfRange(0, (256 * 32)), full.copyOfRange(256 * 32, full.size))
        }

    }


    /**
     * Appends the content of [LamportKey.key1] to that of [LamportKey.key0] and writes the bytes to a given file.
     *
     * @param path the path to the file to write the key to. If the file previously existed, will erase its content.
     */
    fun toFile(path : String){
        var file = File(path)
        file.writeBytes(key0)
        file.appendBytes(key1)
    }


    /**
     * Prints [this] in a similar manner as [ByteArray.printHex]
     */
    fun printHex(){
        println("key 0")
        key0.printHex()
        println("\n\nkey 1")
        key1.printHex()
    }

    /**
     * Getter for a block of the [LamportKey]. A block is a set of bytes, usually of the size of the output of the hash function used.
     *
     * @see LamportKey.BLOCK_SIZE
     * @see LamportKey.setBlock
     *
     * @param keyID which key to fetch the block from. Should be either 0 or 1.
     * @param blockIndex index of the block to fetch.
     * @return a [ByteArray] of [LamportKey.BLOCK_SIZE] containing the block.
     * @throws InvalidKeyIDException if [keyID] is neither 0 nor 1
     */
    fun getBlock(keyID: Int, blockIndex: Int) : ByteArray{
        if(keyID != 1 && keyID != 0)
            throw InvalidKeyIDException()

        return if(keyID == 1)
            key1.copyOfRange(blockIndex* LAMPORT_BLK_SIZE, (blockIndex+1)* LAMPORT_BLK_SIZE -1)
        else
            key0.copyOfRange(blockIndex* LAMPORT_BLK_SIZE, (blockIndex+1)* LAMPORT_BLK_SIZE -1)
    }


    /**
     * Setter for a block of the [LamportKey]. A block is a set of bytes, usually of the size of the output of the hash function used.
     *
     * @see LamportKey.BLOCK_SIZE
     * @see LamportKey.getBlock
     *
     * @param keyID which key to fetch the block from. Should be either 0 or 1.
     * @param blockIndex index of the block to fetch.
     * @param block block to replace the selected [LamportKey] block with
     * @return a [ByteArray] of [LamportKey.BLOCK_SIZE] containing the block.
     * @throws InvalidKeyIDException if [keyID] is neither 0 nor 1
     */
    fun setBlock(keyID : Int, blockIndex : Int, block : ByteArray){
        if (block.size != 32)
            throw InvalidBlockSizeException()
        if(keyID == 1)
            block.copyInto(key1, blockIndex* LAMPORT_BLK_SIZE, blockIndex* LAMPORT_BLK_SIZE, (blockIndex+1)* LAMPORT_BLK_SIZE -1)
        else
            block.copyInto(key0, blockIndex* LAMPORT_BLK_SIZE, blockIndex* LAMPORT_BLK_SIZE, (blockIndex+1)* LAMPORT_BLK_SIZE -1)
    }

    /**
     * Simply concatenates both halves of the key together into a single [ByteArray]
     *
     * @return a [ByteArray] containing the content of [LamportKey.key0] followed by the content of [LamportKey.key1] concatenated together
     */
    fun toByteArray() : ByteArray{
        return key0 + key1
    }


}

/**
 * Lamport Secret (private) Key. A regular Lamport key capable of [signing][sign] messages.
 * @see [LamportKey]
 * @constructor generates a secret [Lamport key][LamportKey]
 *
 *
 * @param key0 same as [LamportKey.key0]
 * @param key1 same as [LamportKey.key1]
 */
class LSecretKey(key0 : ByteArray, key1 : ByteArray) : LamportKey(key0, key1){


    companion object{
        /**
         * Loads a [LSecretKey] stored in a file using the reversed scheme of [LamportKey.toFile]
         * @see [LamportKey.lamportFromFile]
         * @param path the path to the file containing the key
         * @return the key stored in the file
         */
        fun lSecretFromFile(path : String) : LSecretKey {
            var file = File(path)
            val full = file.readBytes()
            return LSecretKey(full.copyOfRange(0, (256 * 32)), full.copyOfRange(256 * 32, full.size))
        }
    }


    /**
     * Signs a [message] using the secret key
     *
     * @param message message to sign
     * @return a signature for the given message. We recommend appending that signature with the message when sending it to the recipient using [ByteArray.toHexFormat]
     */
    fun sign(message : String) : ByteArray{
        var sig = ByteArray(KEY_LENGTH)
        val msgHash = message.toByteArray().sha()
        for(i in 0 until (BLOCK_SIZE)*8) {
            if(msgHash.checkBit(i))
                sig.setBlock(i,
                    BLOCK_SIZE, key1.getBlock(i,
                        BLOCK_SIZE
                    ))
            else
                sig.setBlock(i,
                    BLOCK_SIZE, key0.getBlock(i,
                        BLOCK_SIZE
                    ))
        }
        return sig
    }
}
/**
 * Lamport Public Key. A regular Lamport key capable of [verifying][verify] messages.
 * @see [LamportKey]
 * @constructor generates a secret [Lamport key][LamportKey]
 *
 *
 * @param key0 same as [LamportKey.key0]
 * @param key1 same as [LamportKey.key1]
 */
class LPublicKey(key0 : ByteArray, key1 : ByteArray) : LamportKey(key0, key1){


    companion object{
        /**
         * Loads a [LPublicKey] stored in a file using the reversed scheme of [LamportKey.toFile]
         * @see [LamportKey.lamportFromFile]
         *
         * @param path the path to the file containing the key
         * @return the key stored in the file
         */
        fun lPublicFromFile(path : String) : LPublicKey {
            var file = File(path)
            val full = file.readBytes()
            return LPublicKey(full.copyOfRange(0, (256 * 32)), full.copyOfRange(256 * 32, full.size))
        }
    }



    /**
     * Verifies the validity of a message-signature-public key set. The public key to use should be that which the sender published prior to sending you a message with signature. And invalid signature means the authentication has failed. In other terms, we aren't sure who generated that signature or if the message was altered.
     *
     * @param message the received message
     * @param sig the received signature
     * @return true if the signature is valid, false if it is not.
     */
    fun verify(message : String, sig : ByteArray) : Boolean{

        //Step one : generating hashed blocks from the signature :

        val hashedSig = sig.hashBlockByBlock(BLOCK_SIZE)



        //Step two : generating a hash of the message :
        val hashedMsg =
            message.toByteArray()
                .sha()
        //Step three : comparing the hashed signature to key0 and key1 of the public key
        for(i in 0 until (HASH_SIZE)*8){        //For each bit of the sha256 hash of the message...
            if (hashedMsg.checkBit(i)) {  //If we find '1'...
                if (!(hashedSig.getBlock(i, BLOCK_SIZE) contentEquals
                            (this.key1.getBlock(i, BLOCK_SIZE)))  ) {    //If the hash of the signature doesnt match the same block of the public key...
                    sig.getBlock(i, BLOCK_SIZE).printHex()
                    hashedSig.getBlock(i, BLOCK_SIZE).printHex()
                    this.key1.getBlock(i, BLOCK_SIZE).printHex()
                    return false    //Failed authentication
                }
            }
            else {
                if (!(hashedSig.getBlock(i, BLOCK_SIZE) contentEquals
                            (this.key0.getBlock(i, BLOCK_SIZE)))  ) {

                    return false    //failed authentication
                }
            }
        }

        return true;    //If every block was a match, authentication successful
    }
}






