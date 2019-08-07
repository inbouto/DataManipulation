[kotlin.ByteArray](index.md) / [checkBit](.)

# checkBit

`fun ByteArray.checkBit(index: Int): Boolean`

Checks whether a given bit of a [ByteArray](#) is a 1 or a 0.

### Parameters

`index` - the position of the bit to check (max value is 8 times [this.size](../index.md))

### Exceptions

`IndexOutOfBoundsException` - when [index](check-bit.md#$checkBit(kotlin.ByteArray, kotlin.Int)/index) is lesser than 0 or greater than 8 * [this.size](../index.md)

**See Also**

[Byte.checkBit](#)

**Return**
true if the bit is a 1, false if it's a 0

