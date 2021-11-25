package me.izzp.jetcasterdemo

import org.w3c.dom.Element
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.math.floor

private fun Element.firstElement(name: String): Element? {
    val list = getElementsByTagName(name)
    return if (list.length == 0) {
        null
    } else {
        list.item(0) as Element
    }
}

private fun Element.elements(name: String): List<Element> {
    val rtn = mutableListOf<Element>()
    val list = getElementsByTagName(name)
    for (i in 0 until list.length) {
        val item = list.item(i)
        if (item is Element) {
            rtn += item
        }
    }
    return rtn
}

object FeedResolver {
    fun resolve(content: String): Feed? {
        val doc = try {
            DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(content.byteInputStream())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } ?: return null
        val root = doc.documentElement
        val channel = root.firstElement("channel") ?: return null
        val link = channel.firstElement("link")?.textContent ?: "random_${UUID.randomUUID()}"
        val title = channel.firstElement("title")?.textContent ?: ""
        val desc = channel.firstElement("description")?.textContent ?: ""
        val image = channel.firstElement("image")?.firstElement("url")?.textContent
        val posts = channel.elements("item").map { item ->
            val guid = item.firstElement("guid")?.textContent ?: "random_${UUID.randomUUID()}"
            val title = item.firstElement("title")?.textContent ?: ""
            val desc = item.firstElement("description")?.textContent ?: ""
            val enclosure = item.firstElement("enclosure")
            val url = enclosure?.getAttribute("url")
            val length = enclosure?.getAttribute("length")?.toLongOrNull() ?: 0L
            Post(
                feedId = link,
                postId = guid,
                title = title,
                desc = desc,
                img = image,
                audio = if (url != null) {
                    Audio(url, floor(length / 1000f).toLong())
                } else {
                    null
                }
            )
        }
        return Feed(link, title, desc, image, posts)
    }
}