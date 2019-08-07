[kotlin.ByteArray](index.md) / [getBlock](.)

# getBlock

`fun ByteArray.getBlock(blockIndex: Int, blockSize: Int): ByteArray`

Retrieves a "block" of data of any blockSize for a [ByteArray](#)

### Parameters

`blockIndex` - which block to retrieve. The block will start at index [blockIndex](get-block.md#$getBlock(kotlin.ByteArray, kotlin.Int, kotlin.Int)/blockIndex) * [blockSize](get-block.md#$getBlock(kotlin.ByteArray, kotlin.Int, kotlin.Int)/blockSize)

`blockSize` - size of blocks to use

### Exceptions

`IncorrectBlockSizeException` - if the size of the [ByteArray](#) is not a multiple of [blockSize](get-block.md#$getBlock(kotlin.ByteArray, kotlin.Int, kotlin.Int)/blockSize)

**See Also**

[setBlock](set-block.md)

**Return**

