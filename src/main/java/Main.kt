import java.io.File

const val DIR = "C:\\Users\\Learneo\\IdeaProjects\\Crypto\\resources"

const val KEY_AMOUNT = 64

fun main() {
    val pubKeys = ArrayList<LPublicKey>(0)
    val secKey = ArrayList<LSecretKey>(0)
    for(i in 0 until KEY_AMOUNT){
        val keys = generateKeys()
        pubKeys.add(keys.second)
        secKey.add(keys.first)


    }
    println(MerkleTree({
        val result = ArrayList<ByteArray>(0)
        pubKeys.forEach {
            result.add(it.toByteArray())
        }
        result
    }()
    ))



}

