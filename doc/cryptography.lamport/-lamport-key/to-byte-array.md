[cryptography.lamport](../index.md) / [LamportKey](index.md) / [toByteArray](.)

# toByteArray

`fun toByteArray(): ByteArray`

Simply concatenates both halves of the key together into a single [ByteArray](#)

**Return**
a [ByteArray](#) containing the content of [LamportKey.key0](key0.md) followed by the content of [LamportKey.key1](key1.md) concatenated together

