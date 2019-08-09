package cryptography.lamport


import data_manipulation.structures.MerkleTree
import hashBlockByBlock
import indexedHash
import setBlock
import toByteArray
import kotlin.random.Random

/**
 * @author Inbouto
 * The Lamport scheme at [LamportKey] works only to generate single-use key pairs.
 * We can improve upon that original idea by generating multiple public keys from a single private key,
 * as well as reducing the private key size down to 32 bytes and using [Merkle trees][data_manipulation.structures.MerkleTree].
 * This improved Lamport scheme uses the old one to sign and verify content, but generates public and private keys differently.
 *
 * We want to generate multiple public and private keys at once and commit all of them with a single hash. We generate a global secret hash, then append to it 4 bytes of indexation that'll select a key and hash that. That'll be the private key hash.
 * then we can index that private key hash again using the same method to generate each of the 512 blocks of the private key.
 * From there we have a usable, single-use private key that can be used with the unoptimized scheme.
 *
 * To store and commit public keys, we will use a Merkle tree and commit the public key hash.
 * to check the validity of a given public key (calculate back the public key hash) we also need a series of hashes that I'll refer to as a hash chain.
 * We simply recursively hash an input with the next hash. The first input is the public key we received in the signature.
 */


/**
 * We will use an indexed hash twice : once to index a public key, once to index a block of a public key.
 * That's how a single 32 bytes private key can generate a virtually unlimited amount of public keys.
 * @constructor generates a global secret root, then derives a series of public keys from it using indexed hashes.
 *
 *
 * @param amount of public keys to generate and commit
 *
 *
 * @property pubKeys [MerkleTree] containing the public keys
 * @property secret the global secret root
 *
 */
class ImprovedLamportScheme(amount: Int){
    private val pubKeys : MerkleTree
    private val secret : LSecretRoot
    init{

        secret = genLSecretRoot()


        pubKeys = MerkleTree({              //Not sure why, but we have to cast the StackableLPublic into Containers.
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


    /**
     * Generates a random global secret root
     *
     * @return a 32 bytes global secret root
     */
    private fun genLSecretRoot() : LSecretRoot = LSecretRoot()


    /**
     * Generates public keys based on a global secret root
     *
     * @param amount how many keys we want to generate
     * @param secret the global secret root we want to derive the public keys from
     * @return an [ArrayList] containing all the generated
     */
    private fun genPublicKeys(amount : Int, secret : LSecretRoot) : ArrayList<LPublicContainer>{
        var res = ArrayList<LPublicContainer>(0)
        for(i in 0 until amount){
            res.add(LPublicContainer(secret.getIndexedLSecret(i)))
        }
        return res
    }


    /**
     * Signs a message using the first available key.
     * @see [LSecretKey.sign]
     *
     * @param message the message to sign
     * @return a 24KB signature, containing the signature itself, the public key used and a hash chain.
     */
    fun sign(message : ByteArray) : ImprovedLSignature{
        for(i in 0 until pubKeys.size){
            if(!(pubKeys.get(i) as LPublicContainer).used){
                (pubKeys.get(i) as LPublicContainer).used = true
                return ImprovedLSignature(secret.signUsingKey(message, i), LPublicKey(pubKeys.get(i).content), pubKeys.getHashChain(i))
            }
        }
        throw NoMoreUsableKeysExceptioon()
    }


    /**
     * Global secret root
     * @property key the actual 32 bytes key
     */
    class LSecretRoot {
        val key : ByteArray = Random.Default.nextBytes(ByteArray(BLOCK_SIZE))

        /**
         * generates an indexed key. simply concatenates the index to the key.
         *
         * @param index the index to which to index the key
         * @return the global secret root key + index
         */
        fun getIndexedKey(index: Int) : ByteArray = key + index.toUInt().toByteArray()

        /**
         * Signs a message using a specific (indexed) key.
         * @see [ImprovedLamportScheme.sign]
         *
         * @param message the message to sign
         * @param keyIndex the index of the key to use
         * @return the 24KB signature
         */
        fun signUsingKey(message: ByteArray, keyIndex : Int): ByteArray = getIndexedLSecret(keyIndex).sign(message)


        /**
         * generates a complete [LSecretKey] that can be used to [LSecretKey.sign] a message
         *
         * @param index index of the key to use
         * @return a [LSecretKey] capable of [signing][LSecretKey.sign]
         */
        fun getIndexedLSecret(index : Int) : LSecretKey{
            var key0 = ByteArray(KEY_LENGTH)
            var key1 = ByteArray(KEY_LENGTH)

            for(i in 0 until KEY_LENGTH/ BLOCK_SIZE){
                key0.setBlock(i, this.key.indexedHash(index).indexedHash(i))
                key1.setBlock(i, this.key.indexedHash(index + KEY_LENGTH/ BLOCK_SIZE).indexedHash(i))
            }

            return LSecretKey(key0, key1)
        }

    }

    /**
     * Single public key generated from a [LSecretKey]
     *
     * @constructor generates a [LPublicKey] from the given [LSecretKey] and puts its contents in the [MerkleTree.Container]
     *
     * @param secret the secret from which to generate a public key
     */
    class LPublicContainer(secret : LSecretKey) : MerkleTree.Container(
        LPublicKey(secret.key0.hashBlockByBlock(BLOCK_SIZE), secret.key0.hashBlockByBlock(BLOCK_SIZE))
            .toByteArray()
    )
    {
        var used = false
    }


    class ImprovedLSignature(val signature : ByteArray, val pubKey : LPublicKey, val hashChain : ArrayList<ByteArray>)


    override fun toString(): String {
        return pubKeys.toString()
    }
}
