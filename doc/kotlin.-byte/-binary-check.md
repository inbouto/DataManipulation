[kotlin.Byte](index.md) / [BinaryCheck](.)

# BinaryCheck

`fun Byte.BinaryCheck(checkValue: Int): Boolean`

Checks whether certain bits of a byte are at 1 or not.
For example, if checkValue is 128, will check for the first bit (10000000). If at 192 (11000000), will check for first and second bits, etc...
Useful when used through [Byte.checkBit](#).

### Parameters

`checkValue` - unsigned decimal value of the binary mask that will decide which bits are to be checked for 1

**Return**
true if all checked bits are 1s, false if at least one bit is a 0. will always return true if [checkValue](-binary-check.md#$BinaryCheck(kotlin.Byte, kotlin.Int)/checkValue) is 0

**Trows**
[InvalidByteValueException](../data_manipulation.basics/-invalid-byte-value-exception/index.md) if given [checkValue](-binary-check.md#$BinaryCheck(kotlin.Byte, kotlin.Int)/checkValue) is lesser than 0 or greater than 255

