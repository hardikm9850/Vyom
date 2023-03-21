package com.hardik.vyom.cache

import android.graphics.Bitmap

class MemoryCache : ImageCache {
    private val lruCache = LRUCache(10)

    @Synchronized
    override fun get(url: String): Bitmap? {
        return lruCache.get(url)
    }

    @Synchronized
    override fun put(url: String, bitmap: Bitmap) {
        lruCache.put(url,bitmap)
    }

    override fun clear() {
        lruCache.clear()
    }

    private class LRUCache(private val capacity: Int) {
        private val mapOfNodes = HashMap<String, DoublyNode>(capacity)
        private var length = 0;
        private var dummyHead = DoublyNode()
        private var dummyTail = DoublyNode()

        init {
            initialise()
        }

        private fun initialise() {
            dummyHead.next = dummyTail
            dummyTail.prev = dummyHead
        }

        fun clear(){
            mapOfNodes.clear()
            length = 0;
            dummyHead = DoublyNode()
            dummyTail = DoublyNode()
            initialise()
        }

        fun get(key: String): Bitmap? {
            if (mapOfNodes[key] != null) {
                moveNodeToHead(mapOfNodes[key]!!)
                return mapOfNodes[key]?.bitmap
            }
            return null
        }

        fun put(key: String, bitmap: Bitmap) {
            if (mapOfNodes[key] != null) {
                //node is already present, move it to head
                val node = mapOfNodes[key]!!
                node.bitmap = bitmap
                moveNodeToHead(node)
            } else {
                //create and append the node at first
                val node = DoublyNode(key, bitmap)
                mapOfNodes[key] = node
                addFirst(node)
            }
        }

        private fun moveNodeToHead(node: DoublyNode) {
            removeNode(node)
            addFirst(node)
        }

        private fun addFirst(node: DoublyNode) {
            val next = dummyHead.next

            node.prev = dummyHead
            dummyHead.next = node

            node.next = next
            next?.prev = node

            if (++length > capacity) {
                //gotta remove the last node
                removeLastNode()
            }
        }

        private fun removeLastNode() {
            dummyTail.prev?.let {
                if (it == dummyHead) return
                val key = it.url
                mapOfNodes.remove(key)
                removeNode(it)
            }
        }

        private fun removeNode(node: DoublyNode) {
            val prev = node.prev
            val next = node.next
            prev?.next = next
            next?.prev = prev
            length--
        }
    }

    private class DoublyNode(
        val url: String? = null,
        var bitmap: Bitmap? = null,
        var prev: DoublyNode? = null,
        var next: DoublyNode? = null
    )


}