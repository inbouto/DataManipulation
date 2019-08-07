[MerkleTree](.)

# MerkleTree

`class MerkleTree : List<ByteArray>`

Merkle tree structure. It implements [List](#) to let us read from it with ease.

**See Also**

[List](#)

### Constructors

| [&lt;init&gt;](-init-.md) | `MerkleTree(contents: `[`<ERROR CLASS>`](../index.md)`<ByteArray>)`<br>Will sort the given contents from smallest to largest before creating the tree starting from a [Root](#). |

### Properties

| [size](size.md) | `val size: Int` |

### Functions

| [contains](contains.md) | `fun contains(element: ByteArray): Boolean` |
| [containsAll](contains-all.md) | `fun containsAll(elements: Collection<ByteArray>): Boolean` |
| [get](get.md) | `fun get(index: Int): ByteArray` |
| [indexOf](index-of.md) | `fun indexOf(element: ByteArray): Int` |
| [isEmpty](is-empty.md) | `fun isEmpty(): Boolean` |
| [iterator](iterator.md) | `fun iterator(): Iterator<ByteArray>` |
| [lastIndexOf](last-index-of.md) | `fun lastIndexOf(element: ByteArray): Int` |
| [listIterator](list-iterator.md) | `fun listIterator(): ListIterator<ByteArray>`<br>`fun listIterator(index: Int): ListIterator<ByteArray>` |
| [subList](sub-list.md) | `fun subList(fromIndex: Int, toIndex: Int): List<ByteArray>` |
| [toString](to-string.md) | `fun toString(): String` |

