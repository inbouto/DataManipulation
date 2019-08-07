[cryptography.lamport](../index.md) / [LPublicKey](index.md) / [verify](.)

# verify

`fun verify(message: ByteArray, sig: ByteArray): Boolean`

Verifies the validity of a message-signature-public key set. The public key to use should be that which the sender published prior to sending you a message with signature. And invalid signature means the authentication has failed. In other terms, we aren't sure who generated that signature or if the message was altered.

### Parameters

`message` - the received message

`sig` - the received signature

**Return**
true if the signature is valid, false if it is not.

