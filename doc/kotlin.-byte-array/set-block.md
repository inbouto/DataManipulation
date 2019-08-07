[kotlin.ByteArray](index.md) / [setBlock](.)

# setBlock

`fun ByteArray.setBlock(position: Int, block_from: ByteArray): Unit`

Sets a "block" of data of any blockSize in a [ByteArray](#)

### Parameters

`blockIndex` - which block to update. The block will start at index [blockIndex](#) * [blockSize](#)

`blockSize` - blockSize of blocks to use

`block_from` - block to copy from

### Exceptions

`IncorrectBlockSizeException` - if the blockSize of the [ByteArray](#) is not a multiple of the size of [block_from](set-block.md#$setBlock(kotlin.ByteArray, kotlin.Int, kotlin.ByteArray)/block_from)

**See Also**

[getBlock](get-block.md)

**Return**

