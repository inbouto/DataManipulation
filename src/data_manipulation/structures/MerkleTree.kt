import java.lang.Exception
import kotlin.math.log2

/**
 * @author Inbouto
 */

class MerkleTree : List<MerkleTree.Container>{
    override fun get(index: Int): Container {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun indexOf(element: Container): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun lastIndexOf(element: Container): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listIterator(): ListIterator<Container> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listIterator(index: Int): ListIterator<Container> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<Container> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun contains(element: Container): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun containsAll(elements: Collection<Container>): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmpty(): Boolean = false

    override fun iterator(): Iterator<Container> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val size: Int
    private val root: Root
    private val depth: Int

    constructor(pubKeys : ArrayList<ByteArray>){
        if((log2(pubKeys.size.toFloat()) - log2(pubKeys.size.toFloat()).toInt() >0))  //If the pubKeys array doesnt contain a power of 2 amount of keys...
            throw Exception()

        size = pubKeys.size
        depth = log2(size.toFloat()).toInt()


        var complete = false
        while(!complete){
            complete = true
            for(i in 0 until pubKeys.size-1){
                if(pubKeys[i] > pubKeys[i+1]){
                    val tmp = pubKeys[i]
                    pubKeys[i] = pubKeys[i+1]
                    pubKeys[i+1] = tmp
                    complete = false
                }
            }
        }
        root = Root(pubKeys)
    }






    override fun toString() : String{
        var res = ""
        val array = root.recursivePrint()
        array.forEach {
            res += "$it\n"
        }
        return res
    }







    class Node(contents : List<ByteArray>, override val parent: Parent) : Child, Parent(contents){
        override fun print(): ArrayList<String> = recursivePrint()
    }
    class Leaf(content: ByteArray, override val parent: Parent) : Child {
        val container : Container = Container(content)
        override val hash: ByteArray = content.sha()
        override fun print(): ArrayList<String> = arrayListOf("HASH :\t${hash.toHexFormat()}", container.print())
    }

    class Root(contents : List<ByteArray>) : Parent(contents)



    open abstract class Parent(contents : List<ByteArray>) : HashContainer{
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
    open interface Child : HashContainer{
        fun print() : ArrayList<String>
        val parent : Parent
    }
    open interface HashContainer{
        val hash : ByteArray
    }
    open class Container(val content : ByteArray) {
        fun print(): String = content.toHexFormat()
    }

}




