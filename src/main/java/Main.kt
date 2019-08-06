import cryptography.lamport.*
import java.util.ArrayList


/**
 * @suppress
 */
const val DIR = "C:\\Users\\Learneo\\IdeaProjects\\Crypto\\resources"
/**
 * @suppress
 */
const val KEY_AMOUNT = 8
/**
 * @suppress
 *
 */
fun main() {

    val pubKeys = ArrayList<LPublicKey>(0)
    val secKey = ArrayList<LSecretKey>(0)
    for(i in 0 until KEY_AMOUNT){
        val keys = LamportKey.generateKeys()
        pubKeys.add(keys.second)
        secKey.add(keys.first)


    }
    val tree = MerkleTree({
        val result = ArrayList<ByteArray>(0)
        pubKeys.forEach {
            result.add(it.toByteArray())
        }
        result
    }()
    )


    println(tree)



}


