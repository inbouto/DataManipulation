[cryptography.lamport](.)

## Package cryptography.lamport

### Types

| [LPublicKey](-l-public-key/index.md) | `class LPublicKey : `[`LamportKey`](-lamport-key/index.md)<br>Lamport Public Key. A regular Lamport key capable of [verifying](-l-public-key/verify.md) messages. |
| [LSecretKey](-l-secret-key/index.md) | `class LSecretKey : `[`LamportKey`](-lamport-key/index.md)<br>Lamport Secret (private) Key. A regular Lamport key capable of [signing](-l-secret-key/sign.md) messages. |
| [LamportKey](-lamport-key/index.md) | `open class LamportKey : Any`<br>Lamport signature scheme key. This contains a single key (public or private) stored as two [ByteArrays](#), one containing the '0' key, the other containing the '1' key. See https://en.wikipedia.org/wiki/Lamport_signature for more info. |

### Exceptions

| [InvalidBlockSizeException](-invalid-block-size-exception/index.md) | `class InvalidBlockSizeException : Throwable` |
| [InvalidKeyIDException](-invalid-key-i-d-exception/index.md) | `class InvalidKeyIDException : Throwable` |

