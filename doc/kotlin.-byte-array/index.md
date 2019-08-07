[kotlin.ByteArray](.)

### Extensions for kotlin.ByteArray

| [amountOfOnes](amount-of-ones.md) | `fun ByteArray.amountOfOnes(): Int` |
| [and](and.md) | `fun ByteArray.and(comp: ByteArray): ByteArray`<br>Does a bitwise AND comparison between two [ByteArrays](#).
Both [ByteArray](#)s have to be the same size. |
| [checkBit](check-bit.md) | `fun ByteArray.checkBit(index: Int): Boolean`<br>Checks whether a given bit of a [ByteArray](#) is a 1 or a 0. |
| [checkMatch](check-match.md) | `fun ByteArray.checkMatch(prefix: ByteArray, wcMask: ByteArray): Boolean`<br>Checks if [this](#) matches a condition based on an [wildcard mask](check-match.md#$checkMatch(kotlin.ByteArray, kotlin.ByteArray, kotlin.ByteArray)/wcMask) and a prefix.
Note : while the concept of wildcard mask comes from network engineering, it is perfectly applicable for any array of bytes.
For more information, see https://en.wikipedia.org/wiki/Wildcard_mask |
| [compareTo](compare-to.md) | `operator fun ByteArray.compareTo(bytes: ByteArray): Int`<br>Enables us to directly compare two [ByteArrays](#) (with &gt; or &lt; or == or !=). |
| [getBlock](get-block.md) | `fun ByteArray.getBlock(blockIndex: Int, blockSize: Int): ByteArray`<br>Retrieves a "block" of data of any blockSize for a [ByteArray](#) |
| [hashBlockByBlock](hash-block-by-block.md) | `fun ByteArray.hashBlockByBlock(blk_size: Int): ByteArray`<br>Hashes [this](#) block by block and puts each hash one after another in a [ByteArray](#) |
| [inv](inv.md) | `fun ByteArray.inv(): ByteArray`<br>Does a bitwise inversion (NOT operation) of [this](#). |
| [isMinValue](is-min-value.md) | `fun ByteArray.isMinValue(): Boolean`<br>Checks whether [this](#) contains only bits at 0. |
| [or](or.md) | `fun ByteArray.or(comp: ByteArray): ByteArray`<br>Does a bitwise OR comparison between two [ByteArrays](#).
Both [ByteArray](#)s have to be the same size. |
| [printHex](print-hex.md) | `fun ByteArray.printHex(basic: Boolean = false): Unit`<br>Prints a user-friendly display of [this](#).
Each line is 32 bytes long, cut into 4 8-bytes long blocks. |
| [setBlock](set-block.md) | `fun ByteArray.setBlock(position: Int, block_from: ByteArray): Unit`<br>Sets a "block" of data of any blockSize in a [ByteArray](#) |
| [sha](sha.md) | `fun ByteArray.sha(): ByteArray`<br>Generates a sha-256 hash of a [ByteArray](#). |
| [toFile](to-file.md) | `fun ByteArray.toFile(path: String): Unit`<br>Saves a [ByteArray](#) into a file. Will erase the file if it exists. |
| [toHexFormat](to-hex-format.md) | `fun ByteArray.toHexFormat(): String`<br>Converts a [ByteArray](#) into a readable hexadecimal [String](#). |
| [xor](xor.md) | `fun ByteArray.xor(comp: ByteArray): ByteArray`<br>Does a bitwise XOR (exclusive OR) comparison between two [ByteArrays](#).
Both [ByteArray](#)s have to be the same size. |

