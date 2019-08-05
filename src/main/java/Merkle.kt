import java.lang.Exception
import java.math.BigInteger
import kotlin.math.log2
import kotlin.math.pow


class MerkleTree{

    private val size: Int
    private val root: Root
    private val depth: Int

    constructor(pubKeys : ArrayList<ByteArray>){
        if((log2(pubKeys.size.toFloat()) - log2(pubKeys.size.toFloat()).toInt() >0))  //If the pubKeys array doesnt contain a power of 2 amount of keys...
            throw Exception()

        size = pubKeys.size
        depth = log2(size.toFloat()).toInt()
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
    class Leaf(private val content: ByteArray, override val parent: Parent) : Child {
        override val hash: ByteArray = content.sha()
        override fun print(): ArrayList<String> = arrayListOf("HASH :\t${hash.toHexFormat()}")
    }

    class Root(contents : List<ByteArray>) : Parent(contents){
        override fun recursivePrint() : ArrayList<String> {
            val res = super.recursivePrint()
            return res
        }
    }



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

}


