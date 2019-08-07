[kotlin.Byte](.)

### Extensions for kotlin.Byte

| [BinaryCheck](-binary-check.md) | `fun Byte.BinaryCheck(checkValue: Int): Boolean`<br>Checks whether certain bits of a byte are at 1 or not.
For example, if checkValue is 128, will check for the first bit (10000000). If at 192 (11000000), will check for first and second bits, etc...
Useful when used through [Byte.checkBit](#). |
| [checkBit](check-bit.md) | `fun Byte.checkBit(position: Int): Boolean`<br>Checks whether a certain bit at a given position in the byte is a 1 or a 0.
Useful for sequential checks of bits in a byte. |

