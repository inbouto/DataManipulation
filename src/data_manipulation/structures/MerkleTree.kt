package data_manipulation.structures

import checkBit
import sha
import toByteArray
import toHexFormat
import java.lang.Exception
import kotlin.math.log2

/**
 * @author Inbouto
 */




/**
 * Merkle tree structure. It implements [List] to let us read from it with ease.
 * @see [List]]
 *
 * @property root starting point of the binary tree structure
 * @property depth amount of "floors" to the binary tree. Each extra floor means twice as many leaves.
 * @constructor Will sort the given contents from smallest to largest before creating the tree starting from a [Root].
 * @throws InvalidDataSetException when the data set's size is not a power of 2.
 *
 *
 *
 * @param contents the total data to store.
 */
class MerkleTree(contents: ArrayList<Container>){

    val size: Int
    private val root: Root
    private val depth: Int

    init {
        if((log2(contents.size.toFloat()) - log2(contents.size.toFloat()).toInt() >0))  //If the pubKeys array doesnt contain a power of 2 amount of keys...
            throw InvalidDataSetException()
        size = contents.size
        depth = log2(size.toFloat()).toInt()




        //This entire section was made to sort contents. However this proved impractical (the improved Lamport scheme already generates keys in a specific order that needs to be kept. No re-sorting)

//        while(!complete){
//            complete = true
//            for(i in 0 until contents.size-1){
//                if(contents[i].content > contents[i+1].content){
//                    val tmp = contents[i]
//                    contents[i] = contents[i+1]
//                    contents[i+1] = tmp
//                    complete = false
//                }
//            }
//        }

        root = Root(contents)
    }


    /**
     * Retrieves the chain of hashes that will allow someone to generate the public key hash from a single [Container]
     *
     * @param index index of the [Container]
     * @return the chain of hashes ; first hash is that of the neighbour container, last hash is the penultimate hash before the public key hash
     */
    fun getHashChain(index : Int) : ArrayList<ByteArray> = root.fetchHashedChain(index)




    override fun toString() : String{
        var res = ""
        val array = root.recursivePrint()
        array.forEach {
            res += "$it\n"
        }
        return res
    }

    /**
     * Standard "get" method to retrieve a [Container] using its index.
     * @see [Child.get]
     * @see [Leaf.get]
     * @see [Parent.fetch]
     * @see [Node.fetch]
     *
     * @param index the index of the [Container] to retrieve
     * @return the [Container] at the given [index]
     */
    fun get(index: Int): Container = root.fetch(index)


    /**
     * Basic node of our tree. Has 2 children and a parent.
     * Each [Node] has a [hash][HashContainer.hash]
     * Should not be accessed from outside the [MerkleTree]
     *
     * @property parent reference to the parent (either another [Node] or the [Root])
     * @constructor creates a [Node] that will itself try to create the proper amount of children
     *
     *
     * @param contents to be split between all final [Leaves][Leaf]
     */
    private class Node(contents : List<Container>, override val parent: Parent) : Child, Parent(contents){
        override fun getHashedChain(index: Int): ArrayList<ByteArray> = fetchHashedChain(index)

        /**
         * [get] is inherited from [Child] ; [fetch] is inherited from [Parent]
         * When a [Parent] asks its [Child] for a [Container], it sees it only as a [Child] and can only access the [Child.get] method.
         * A [Node], being both a [Parent] and a [Child], must manually do this translation : "My parent is asking for a container. I dont have the container, but one of my children do".
         * a [Node] is asked as a [Child], but must behave as a [Parent] and ask its own [Children][Child] for the [Container]
         *
         * @param index index of the
         * @return
         */
        override fun get(index: Int): Container = fetch(index)


        /**
         * Used to display the [Node] and its children
         * @see [Parent.recursivePrint]
         * @return [Parent.recursivePrint]
         */
        override fun print(): ArrayList<String> = recursivePrint()
    }

    /**
     * Leaf class ; final level of the Merkle tree. Contains a reference to the given contents
     *
     * @property parent the parent [Node] or [Root]
     * @property container reference to the [Container] containing the contents given
     * @property hash sha256 hash of the given content
     * @constructor
     *
     *
     * @param content Data to be stored in the [container]
     */
    private class Leaf(val container: Container, override val parent: Parent) : Child {
        override fun getHashedChain(index: Int): ArrayList<ByteArray> = arrayListOf(hash)

        /**
         * Get the container. Inherited and overridden from [Child.get]
         *
         * @param index index of the container (should not matter at this point, because we know we're in a leaf)
         * @return the container
         */
        override fun get(index: Int): Container = container


        override val hash: ByteArray = container.content.sha()
        /**
         * prints the leaf in a single line containing the [hash] of the contents stored in [container]
         * @see [ByteArray.toHexFormat]
         * @return a single-lined [ArrayList] containing [hash] in hexadecimal format
         */
        override fun print(): ArrayList<String> = arrayListOf("HASH :\t${hash.toHexFormat()}", container.print())
    }


    /**
     * Root element of the Merkle tree. Contains the root hash of the tree.
     * @see [Parent]
     * @constructor will recursively generate [Nodes][Node] until the next level will have enough [Leaves][Leaf] to store all the contents
     *
     *
     * @param contents a list of all the contents to store
     */
    private class Root(contents : List<Container>) : Parent(contents)


    /**
     * Parent class ; a [Parent] always has two [Children][Child]. Will handle recursive generation of [Nodes][Node]
     * @see [Root]
     * @see [Node]
     *
     * @property child0 first [Child]
     * @property child1 second [Child]
     * @constructor
     *
     *
     * @param contents data to store among [Children][Child]
     */
    private open abstract class Parent(contents : List<Container>) : HashContainer{
        override val hash : ByteArray
        private val child0 : Child
        private val child1 : Child
        val depth : Int

        init{
            if((log2(contents.size.toFloat()) - log2(contents.size.toFloat()).toInt() >0))  //If the pubKeys array doesnt contain a power of 2 amount of keys...
                throw Exception()

            if(contents.size == 2){
                child0 = Leaf(contents[0], this)
                child1 = Leaf(contents[1], this)
                depth = 1
            }
            else{
                val tmp = Node(contents.subList(0, (contents.size/2).toInt()).toList(), this)   //We use a temporary val to also retrieve the depth. Once it's converted to a child, its depth is no longer accessible, so we can't do child0.depth
                depth = tmp.depth+1
                child0 = tmp
                child1 = Node(contents.subList((contents.size/2).toInt(), contents.size).toList(), this)
            }
            hash = (child0.hash + child1.hash).sha()
        }


        /**
         * Allows us to print or display the [MerkleTree] by retrieving informations from each [Node] and [Children][Child] as well as the [Root] and indenting them appropriately
         * @see [MerkleTree.toString]
         * @return an [ArrayList] with each cell of the array containing a line to print.
         */
        open fun recursivePrint() : ArrayList<String>{
            val res : ArrayList<String> = ArrayList<String>(0)
            res.add("HASH :\t${this.hash.toHexFormat()}")
            res.add("CHILD 0 >")
            res.addAll({            //takes the print of the son and prepends a tab to it
                val res = ArrayList<String>(0)
                child0.print().forEach {
                    res.add("\t| $it")
                }
                res
            }())
            res.add("CHILD 1 >")
            res.addAll({            //takes the print of the son and prepends a tab to it
                val res = ArrayList<String>(0)
                child1.print().forEach {
                    res.add("\t| $it")
                }
                res
            }())
            return res
        }

        /**
         * Fetches a container
         *
         * @param index index of the container
         * @return the container at the given index
         */
        open fun fetch(index : Int) : Container{
            if(index.toUInt().toByteArray().checkBit(32 - this.depth))
                return child1.get(index)
            else
                return child0.get(index)
        }

        /**
         * Fetches the hash chain for a given container index, then appends its own hash to the result.
         *
         * [fetch] is used for [Parent] methods, not to confuse with [get] methods which are used for [Child] methods.
         * @see [Child.get]
         *
         * @param index index of the container
         * @return the hash chain leading to the container. First hash if that of the container, last hash is that of the final parent
         */
        fun fetchHashedChain(index: Int): ArrayList<ByteArray>{
            val res = ArrayList<ByteArray>(0)
            if(index.toUInt().toByteArray().checkBit(32 - this.depth)) {
                var tmp = child1.getHashedChain(index)
                tmp.add(hash)
                return tmp
            }
            else {
                var tmp = child0.getHashedChain(index)
                tmp.add(hash)
                return tmp
            }
        }

    }

    /**
     * interface to handle [Child] behavior
     * @see [Node]
     * @see [Leaf]
     */
    private open interface Child : HashContainer{

        fun get(index : Int) : Container

        fun print() : ArrayList<String>
        fun getHashedChain(index: Int): ArrayList<ByteArray>

        val parent : Parent
    }

    /**
     * Handles classes that contain hashes
     * @see [Parent]
     * @see [Child]
     */
    private open interface HashContainer{
        val hash : ByteArray
    }

    /**
     * Handles content under a [ByteArray] format.
     * @see [Leaf]
     *
     * @property content data to store
     */
    open class Container(val content : ByteArray) {
        open fun print(): String = content.toHexFormat()
    }





}



