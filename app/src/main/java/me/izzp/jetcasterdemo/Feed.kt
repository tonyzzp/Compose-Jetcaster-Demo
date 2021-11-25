package me.izzp.jetcasterdemo

import androidx.compose.runtime.Immutable

@Immutable
data class Post(
    val feedId: String,
    val postId: String,
    val title: String,
    val desc: String,
    val img: String?,
    val audio: Audio?
)

@Immutable
data class Audio(
    val url: String,
    val length: Long,
)

@Immutable
data class Feed(
    val feedId: String,
    val title: String,
    val desc: String,
    val img: String?,
    val posts: List<Post>
) {
    override fun toString(): String {
        return "title= $title, desc= $desc, img= $img, posts.size= ${posts.size}"
    }
}
