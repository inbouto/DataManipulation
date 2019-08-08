[cryptography.lamport](../index.md) / [LSecretKey](.)

# LSecretKey

`class LSecretKey : `[`LamportKey`](../-lamport-key/index.md)

Lamport Secret (private) Key. A regular Lamport key capable of [signing](sign.md) messages.

**See Also**

[LamportKey](../-lamport-key/index.md)

### Constructors

| [&lt;init&gt;](-init-.md) | `LSecretKey(key0: ByteArray, key1: ByteArray)`<br>generates a secret [Lamport key](../-lamport-key/index.md) |

### Inherited Properties

| [key0](../-lamport-key/key0.md) | `val key0: ByteArray`<br>half of the key used to code for 0 |
| [key1](../-lamport-key/key1.md) | `val key1: ByteArray`<br>half of the key used to code for 1 |

### Functions

| [sign](sign.md) | `fun sign(message: ByteArray): ByteArray`<br>Signs a [message](sign.md#cryptography.lamport.LSecretKey$sign(kotlin.ByteArray)/message) using the secret key |

### Inherited Functions

| [getBlock](../-lamport-key/get-block.md) | `fun getBlock(keyID: Int, blockIndex: Int): ByteArray`<br>Getter for a block of the [LamportKey](../-lamport-key/index.md). A block is a set of bytes, usually of the size of the output of the hash function used. |
| [printHex](../-lamport-key/print-hex.md) | `fun printHex(): Unit`<br>Prints [this](#) in a similar manner as [ByteArray.printHex](#) |
| [setBlock](../-lamport-key/set-block.md) | `fun setBlock(keyID: Int, blockIndex: Int, block: ByteArray): Unit`<br>Setter for a block of the [LamportKey](../-lamport-key/index.md). A block is a set of bytes, usually of the size of the output of the hash function used. |
| [toByteArray](../-lamport-key/to-byte-array.md) | `fun toByteArray(): ByteArray`<br>Simply concatenates both halves of the key together into a single [ByteArray](#) |
| [toFile](../-lamport-key/to-file.md) | `fun toFile(path: String): Unit`<br>Appends the content of [LamportKey.key1](../-lamport-key/key1.md) to that of [LamportKey.key0](../-lamport-key/key0.md) and writes the bytes to a given file. |

### Companion Object Functions

| [lSecretFromFile](l-secret-from-file.md) | `fun lSecretFromFile(path: String): LSecretKey`<br>Loads a LSecretKey stored in a file using the reversed scheme of [LamportKey.toFile](../-lamport-key/to-file.md) |

