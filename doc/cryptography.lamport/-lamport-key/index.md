[cryptography.lamport](../index.md) / [LamportKey](.)

# LamportKey

`open class LamportKey : Any`

Lamport signature scheme key. This contains a single key (public or private) stored as two [ByteArrays](#), one containing the '0' key, the other containing the '1' key. See https://en.wikipedia.org/wiki/Lamport_signature for more info.

### Constructors

| [&lt;init&gt;](-init-.md) | `LamportKey(key0: ByteArray, key1: ByteArray)`<br>Lamport signature scheme key. This contains a single key (public or private) stored as two [ByteArrays](#), one containing the '0' key, the other containing the '1' key. See https://en.wikipedia.org/wiki/Lamport_signature for more info. |

### Properties

| [key0](key0.md) | `val key0: ByteArray`<br>half of the key used to code for 0 |
| [key1](key1.md) | `val key1: ByteArray`<br>half of the key used to code for 1 |

### Functions

| [getBlock](get-block.md) | `fun getBlock(keyID: Int, blockIndex: Int): ByteArray`<br>Getter for a block of the LamportKey. A block is a set of bytes, usually of the size of the output of the hash function used. |
| [printHex](print-hex.md) | `fun printHex(): Unit`<br>Prints [this](#) in a similar manner as [ByteArray.printHex](#) |
| [setBlock](set-block.md) | `fun setBlock(keyID: Int, blockIndex: Int, block: ByteArray): Unit`<br>Setter for a block of the LamportKey. A block is a set of bytes, usually of the size of the output of the hash function used. |
| [toByteArray](to-byte-array.md) | `fun toByteArray(): ByteArray`<br>Simply concatenates both halves of the key together into a single [ByteArray](#) |
| [toFile](to-file.md) | `fun toFile(path: String): Unit`<br>Appends the content of [LamportKey.key1](key1.md) to that of [LamportKey.key0](key0.md) and writes the bytes to a given file. |

### Companion Object Properties

| [BLOCK_SIZE](-b-l-o-c-k_-s-i-z-e.md) | `const val BLOCK_SIZE: Int`<br>Size of the Lamport Key block. Should be equal to the output size of the hash function used (in this case, sha256, 32 bytes) |
| [KEY_LENGTH](-k-e-y_-l-e-n-g-t-h.md) | `const val KEY_LENGTH: Int`<br>Total size of a half-key in [Bytes](#). Should be a the hash-size-in-bits blocks long |

### Companion Object Functions

| [generateKeys](generate-keys.md) | `fun generateKeys(): `[`<ERROR CLASS>`](../../index.md)`<`[`LSecretKey`](../-l-secret-key/index.md)`, `[`LPublicKey`](../-l-public-key/index.md)`>`<br>Generates a [Secret](../-l-secret-key/index.md)/[Public](../-l-public-key/index.md) key pair |
| [lamportFromFile](lamport-from-file.md) | `fun lamportFromFile(path: String): LamportKey`<br>Loads a LamportKey stored in a file using the reversed scheme of [LamportKey.toFile](to-file.md) |

### Inheritors

| [LPublicKey](../-l-public-key/index.md) | `class LPublicKey : LamportKey`<br>Lamport Public Key. A regular Lamport key capable of [verifying](../-l-public-key/verify.md) messages. |
| [LSecretKey](../-l-secret-key/index.md) | `class LSecretKey : LamportKey`<br>Lamport Secret (private) Key. A regular Lamport key capable of [signing](../-l-secret-key/sign.md) messages. |

