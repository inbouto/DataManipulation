package cryptography.lamport

import data_manipulation.structures.MerkleTree
import indexedHash
import setBlock
import sha
import toByteArray
import kotlin.random.Random

/**
 * @author Inbouto
 * The Lamport scheme at [LamportKey] works only to generate single-time use key pairs.
 * We can improve upon that original idea by generating multiple public keys from a single private key, *
 * as well as reducing the private key size down to 32 bytes and using [Merkle trees][data_manipulation.structures.MerkleTree].
 */


/**
 * We will use an indexed hash twice : once to index a public key, once to index a block of a public key.
 * That's how a single 32 bytes private key can generate a virtually illimited amount of public keys.
 * @constructor
 * TODO
 *
 * @param amount
 */
class ImprovedLamportScheme(amount: Int){
    val pubKeys : MerkleTree
    val secret : LSecretRoot
    init{


//        println("generating secret key")

        secret = genLSecretRoot()

//        println("secret : $LSecretRootKey")
//        println("generating $amount public keys")

        pubKeys = MerkleTree({
            var res = ArrayList<MerkleTree.Container>(0)
            genPublicKeys(amount, secret).forEach {
                res.add(it)
            }
            res
        }()

        )
    }

    /**
     * @property BLOCK_SIZE Size of the Lamport Key block. Should be equal to the output size of the hash function used (in this case, sha256, 32 bytes)
     * @property KEY_LENGTH Total size of a half-key in [Bytes][Byte]. Should be a the hash-size-in-bits blocks long
     */
    companion object {
        const val BLOCK_SIZE = 32
        const val KEY_LENGTH = 256 * BLOCK_SIZE
    }






    private fun genLSecretRoot() : LSecretRoot{
        return LSecretRoot()
    }
    private fun genPublicKeys(amount : Int, secret : LSecretRoot) : ArrayList<StackableLPublic>{
        var res = ArrayList<StackableLPublic>(0)
        for(i in 0 until amount){

//            println("Generating key #$i")

            res.add(StackableLPublic(secret.getIndexedKey(i)))
        }
        return res
    }

    fun sign(message : ByteArray) : ByteArray{
        for(i in 0 until pubKeys.size){
            if(!(pubKeys[i] as StackableLPublic).used)
                return secret.signUsingKey(message, i)

        }
        throw NoMoreUsableKeysExceptioon()
    }











    class LSecretRoot {
        val key : ByteArray = Random.Default.nextBytes(ByteArray(BLOCK_SIZE))
        fun getIndexedKey(index: Int) : ByteArray = key + index.toUInt().toByteArray()

        fun signUsingKey(message: ByteArray, keyIndex : Int): ByteArray = getIndexedLSecret(keyIndex).sign(message)



        private fun getIndexedLSecret(index : Int) : LSecretKey{
            var key0 = ByteArray(KEY_LENGTH)
            var key1 = ByteArray(KEY_LENGTH)

            for(i in 0 until KEY_LENGTH/ BLOCK_SIZE){
                key0.setBlock(i, this.key.indexedHash(index).indexedHash(i))
                key1.setBlock(i, this.key.indexedHash(index + KEY_LENGTH/ BLOCK_SIZE).indexedHash(i))
            }

            return LSecretKey(key0, key1)
        }

    }

    class StackableLPublic(secret : ByteArray) : MerkleTree.Container(genContent(secret)){
        var used = false
        companion object{
            fun genContent(secret : ByteArray) : ByteArray{
                var key0 = ByteArray(KEY_LENGTH)
                var key1 = ByteArray(KEY_LENGTH)


                for(i in 0 until KEY_LENGTH/ BLOCK_SIZE) {
//                    println("block : $i   \t" +
//                            "| secret : ${secret.toHexFormat()} \t" +
//                            "| hash : $${secret.sha().toHexFormat()} \t" +
//                            "| indexed hash (private block) : ${secret.indexedHash(i).toHexFormat()} \t" +
//                            "| public block : ${secret.indexedHash(i).sha().toHexFormat()}")
                    key0.setBlock(i, secret.indexedHash(i).sha())
                    key1.setBlock(i, secret.indexedHash(i + KEY_LENGTH/ BLOCK_SIZE).sha())
                }
                return key0 + key1
            }
        }
    }

    override fun toString(): String {
        return pubKeys.toString()
    }
}
