[cryptography.lamport](../index.md) / [LamportKey](index.md) / [getBlock](.)

# getBlock

`fun getBlock(keyID: Int, blockIndex: Int): ByteArray`

Getter for a block of the [LamportKey](index.md). A block is a set of bytes, usually of the size of the output of the hash function used.

### Parameters

`keyID` - which key to fetch the block from. Should be either 0 or 1.

`blockIndex` - index of the block to fetch.

### Exceptions

`InvalidKeyIDException` - if [keyID](get-block.md#cryptography.lamport.LamportKey$getBlock(kotlin.Int, kotlin.Int)/keyID) is neither 0 nor 1

**See Also**

[LamportKey.BLOCK_SIZE](-b-l-o-c-k_-s-i-z-e.md)

[LamportKey.setBlock](set-block.md)

**Return**
a [ByteArray](#) of [LamportKey.BLOCK_SIZE](-b-l-o-c-k_-s-i-z-e.md) containing the block.

