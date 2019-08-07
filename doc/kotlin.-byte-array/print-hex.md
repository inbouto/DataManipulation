[kotlin.ByteArray](index.md) / [printHex](.)

# printHex

`fun ByteArray.printHex(basic: Boolean = false): Unit`

Prints a user-friendly display of [this](#).
Each line is 32 bytes long, cut into 4 8-bytes long blocks.

### Parameters

`basic` - if true, will only print [this](#) in a single, unformatted hexadecimal string. false by default