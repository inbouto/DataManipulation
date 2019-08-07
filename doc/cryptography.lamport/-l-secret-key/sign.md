[cryptography.lamport](../index.md) / [LSecretKey](index.md) / [sign](.)

# sign

`fun sign(message: ByteArray): ByteArray`

Signs a [message](sign.md#cryptography.lamport.LSecretKey$sign(kotlin.ByteArray)/message) using the secret key

### Parameters

`message` - message to sign

**Return**
a signature for the given message. We recommend appending that signature with the message when sending it to the recipient using [ByteArray.toHexFormat](#)

