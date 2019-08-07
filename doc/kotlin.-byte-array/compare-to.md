[kotlin.ByteArray](index.md) / [compareTo](.)

# compareTo

`operator fun ByteArray.compareTo(bytes: ByteArray): Int`

Enables us to directly compare two [ByteArrays](#) (with &gt; or &lt; or == or !=).

### Parameters

`bytes` - [ByteArray](#) against which to compare [this](#)

### Exceptions

`DifferentArraySizesException` - if [bytes](compare-to.md#$compareTo(kotlin.ByteArray, kotlin.ByteArray)/bytes) and [this](#) have different sizes

**See Also**

[UByte.compareTo](#)

**Return**
-1 if [this](#) &lt; [bytes](compare-to.md#$compareTo(kotlin.ByteArray, kotlin.ByteArray)/bytes), 1 if [this](#) &gt; [bytes](compare-to.md#$compareTo(kotlin.ByteArray, kotlin.ByteArray)/bytes), 0 if [this](#) == [bytes](compare-to.md#$compareTo(kotlin.ByteArray, kotlin.ByteArray)/bytes)

