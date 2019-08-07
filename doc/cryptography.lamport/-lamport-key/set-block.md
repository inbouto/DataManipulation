[cryptography.lamport](../index.md) / [LamportKey](index.md) / [setBlock](.)

# setBlock

`fun setBlock(keyID: Int, blockIndex: Int, block: ByteArray): Unit`

Setter for a block of the [LamportKey](index.md). A block is a set of bytes, usually of the size of the output of the hash function used.

### Parameters

`keyID` - which key to fetch the block from. Should be either 0 or 1.

`blockIndex` - index of the block to fetch.

`block` - block to replace the selected [LamportKey](index.md) block with

### Exceptions

`InvalidKeyIDException` - if [keyID](set-block.md#cryptography.lamport.LamportKey$setBlock(kotlin.Int, kotlin.Int, kotlin.ByteArray)/keyID) is neither 0 nor 1

**See Also**

[LamportKey.BLOCK_SIZE](-b-l-o-c-k_-s-i-z-e.md)

[LamportKey.getBlock](get-block.md)

**Return**
a [ByteArray](#) of [LamportKey.BLOCK_SIZE](-b-l-o-c-k_-s-i-z-e.md) containing the block.

