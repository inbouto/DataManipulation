[kotlin.ByteArray](index.md) / [checkMatch](.)

# checkMatch

`fun ByteArray.checkMatch(prefix: ByteArray, wcMask: ByteArray): Boolean`

Checks if [this](#) matches a condition based on an [wildcard mask](check-match.md#$checkMatch(kotlin.ByteArray, kotlin.ByteArray, kotlin.ByteArray)/wcMask) and a prefix.
Note : while the concept of wildcard mask comes from network engineering, it is perfectly applicable for any array of bytes.
For more information, see https://en.wikipedia.org/wiki/Wildcard_mask

### Parameters

`prefix` - The reference array against which to test the match

`wcMask` - will determine which bits to test (will test a given bit of [this](#) against the one on the same position in [prefix](check-match.md#$checkMatch(kotlin.ByteArray, kotlin.ByteArray, kotlin.ByteArray)/prefix) if at 0, will not test if at 1)

### Exceptions

`DifferentArraySizesException` - if [this](#), [prefix](check-match.md#$checkMatch(kotlin.ByteArray, kotlin.ByteArray, kotlin.ByteArray)/prefix) and [wcMask](check-match.md#$checkMatch(kotlin.ByteArray, kotlin.ByteArray, kotlin.ByteArray)/wcMask) have a different size

**Return**
true if the bits selected by [wcMask](check-match.md#$checkMatch(kotlin.ByteArray, kotlin.ByteArray, kotlin.ByteArray)/wcMask) in [this](#) and [prefix](check-match.md#$checkMatch(kotlin.ByteArray, kotlin.ByteArray, kotlin.ByteArray)/prefix) are the same, false if there is at least a single difference

