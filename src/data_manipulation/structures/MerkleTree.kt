package data_manipulation.structures

import compareTo
import sha
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
class MerkleTree(contents: ArrayList<Container>) : List<MerkleTree.Container>{

    override val size: Int
    private val root: Root
    private val depth: Int

    init {
        if((log2(contents.size.toFloat()) - log2(contents.size.toFloat()).toInt() >0))  //If the pubKeys array doesnt contain a power of 2 amount of keys...
            throw InvalidDataSetException()
        size = contents.size
        depth = log2(size.toFloat()).toInt()

        var complete = false


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






    override fun toString() : String{
        var res = ""
        val array = root.recursivePrint()
        array.forEach {
            res += "$it\n"
        }
        return res
    }


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


        init{
            if((log2(contents.size.toFloat()) - log2(contents.size.toFloat()).toInt() >0))  //If the pubKeys array doesnt contain a power of 2 amount of keys...
                throw Exception()

            if(contents.size == 2){
                child0 = Leaf(contents[0], this)
                child1 = Leaf(contents[1], this)
            }
            else{
                child0 = Node(contents.subList(0, (contents.size/2).toInt()).toList(), this)
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



    }

    /**
     * interface to handle [Child] behavior
     * @see [Node]
     * @see [Leaf]
     */
    private open interface Child : HashContainer{
        fun print() : ArrayList<String>
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
        fun print(): String = content.toHexFormat()
    }






    /**
     * @see [List.get]
     */
    override fun get(index: Int): Container {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /**
     * @see [List.indexOf]
     */
    override fun indexOf(element: Container): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /**
     * @see [List.lastIndexOf]
     */
    override fun lastIndexOf(element: Container): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /**
     * @see [List.listIterator]
     */
    override fun listIterator(): ListIterator<Container> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /**
     * @see [List.listIterator]
     */
    override fun listIterator(index: Int): ListIterator<Container> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /**
     * @see [List.subList]
     */
    override fun subList(fromIndex: Int, toIndex: Int): List<Container> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /**
     * @see [List.contains]
     */
    override fun contains(element: Container): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /**
     * @see [List.containsAll]
     */
    override fun containsAll(elements: Collection<Container>): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    /**
     * @see [List.get]
     */
    override fun isEmpty(): Boolean = false
    /**
     * @see [List.get]
     */
    override fun iterator(): Iterator<Container> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}



