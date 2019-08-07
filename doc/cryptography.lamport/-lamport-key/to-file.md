[cryptography.lamport](../index.md) / [LamportKey](index.md) / [toFile](.)

# toFile

`fun toFile(path: String): Unit`

Appends the content of [LamportKey.key1](key1.md) to that of [LamportKey.key0](key0.md) and writes the bytes to a given file.

### Parameters

`path` - the path to the file to write the key to. If the file previously existed, will erase its content.