[kotlin.Byte](index.md) / [checkBit](.)

# checkBit

`fun Byte.checkBit(position: Int): Boolean`

Checks whether a certain bit at a given position in the byte is a 1 or a 0.
Useful for sequential checks of bits in a byte.

### Parameters

`position` - the position of the bit to check between 1 and 8 (heaviest bit is bit 1, lightest bit is bit 8)

### Exceptions

`InvalidBitPositionException` - when [position](check-bit.md#$checkBit(kotlin.Byte, kotlin.Int)/position) is lesser than 1 or greater than 8

**See Also**

[Byte.BinaryCheck](#)

**Return**
true if bit is a 1, false if bit is a 0

