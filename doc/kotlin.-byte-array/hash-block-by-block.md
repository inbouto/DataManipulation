[kotlin.ByteArray](index.md) / [hashBlockByBlock](.)

# hashBlockByBlock

`fun ByteArray.hashBlockByBlock(blk_size: Int): ByteArray`

Hashes [this](#) block by block and puts each hash one after another in a [ByteArray](#)

### Parameters

`blk_size` - size of blocks to use. Probably should be 32

**Return**
the [ByteArray](#) with all hashes concatenated in the right order

