[cryptography.lamport](../index.md) / [LPublicKey](.)

# LPublicKey

`class LPublicKey : `[`LamportKey`](../-lamport-key/index.md)

Lamport Public Key. A regular Lamport key capable of [verifying](verify.md) messages.

**See Also**

[LamportKey](../-lamport-key/index.md)

### Constructors

| [&lt;init&gt;](-init-.md) | `LPublicKey(key0: ByteArray, key1: ByteArray)`<br>generates a LSecretRoot [Lamport key](../-lamport-key/index.md) |

### Inherited Properties

| [key0](../-lamport-key/key0.md) | `val key0: ByteArray`<br>half of the key used to code for 0 |
| [key1](../-lamport-key/key1.md) | `val key1: ByteArray`<br>half of the key used to code for 1 |

### Functions

| [verify](verify.md) | `fun verify(message: ByteArray, sig: ByteArray): Boolean`<br>Verifies the validity of a message-signature-public key set. The public key to use should be that which the sender published prior to sending you a message with signature. And invalid signature means the authentication has failed. In other terms, we aren't sure who generated that signature or if the message was altered. |

### Inherited Functions

| [getBlock](../-lamport-key/get-block.md) | `fun getBlock(keyID: Int, blockIndex: Int): ByteArray`<br>Getter for a block of the [LamportKey](../-lamport-key/index.md). A block is a set of bytes, usually of the size of the output of the hash function used. |
| [printHex](../-lamport-key/print-hex.md) | `fun printHex(): Unit`<br>Prints [this](#) in a similar manner as [ByteArray.printHex](#) |
| [setBlock](../-lamport-key/set-block.md) | `fun setBlock(keyID: Int, blockIndex: Int, block: ByteArray): Unit`<br>Setter for a block of the [LamportKey](../-lamport-key/index.md). A block is a set of bytes, usually of the size of the output of the hash function used. |
| [toByteArray](../-lamport-key/to-byte-array.md) | `fun toByteArray(): ByteArray`<br>Simply concatenates both halves of the key together into a single [ByteArray](#) |
| [toFile](../-lamport-key/to-file.md) | `fun toFile(path: String): Unit`<br>Appends the content of [LamportKey.key1](../-lamport-key/key1.md) to that of [LamportKey.key0](../-lamport-key/key0.md) and writes the bytes to a given file. |

### Companion Object Functions

| [lPublicFromFile](l-public-from-file.md) | `fun lPublicFromFile(path: String): LPublicKey`<br>Loads a LPublicKey stored in a file using the reversed scheme of [LamportKey.toFile](../-lamport-key/to-file.md) |

