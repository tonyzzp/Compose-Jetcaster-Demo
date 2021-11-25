package me.izzp.jetcasterdemo

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit

val FEED_URLS = listOf(
    "https://www.omnycontent.com/d/playlist/aaea4e69-af51-495e-afc9-a9760146922b/dc5b55ca-5f00-4063-b47f-ab870163d2b7/ca63aa52-ef7b-43ee-8ba5-ab8701645231/podcast.rss",
    "https://fragmentedpodcast.com/feed/",
    "https://feeds.megaphone.fm/replyall",
    "https://feeds.npr.org/510289/podcast.xml",
    "https://feeds.99percentinvisible.org/99percentinvisible",
    "https://www.thenakedscientists.com/naked_scientists_podcast.xml",
    "https://rss.art19.com/the-daily",
    "https://rss.art19.com/lisk",
    "https://omny.fm/shows/silence-is-not-an-option/playlists/podcast.rss",
    "https://feeds.simplecast.com/7PvD7RPL",
    "https://feeds.megaphone.fm/HSW9992617712"
)

class FeedRepository(context: Context) {

    private var cache: List<Feed>? = null

    private val okHttpClient by lazy {
        val cacheDir = File(context.cacheDir, "feed_cache")
        cacheDir.mkdirs()
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .cache(Cache(cacheDir, 10 * 1024 * 1024L))
            .build()
    }

    private suspend fun fetchContent(url: String) = withContext(Dispatchers.IO) {
        runCatching {
            val request = Request.Builder()
                .url(url)
                .get()
                .cacheControl(
                    CacheControl
                        .Builder()
                        .maxAge(1, TimeUnit.DAYS)
                        .build()
                )
                .build()
            okHttpClient
                .newCall(request)
                .execute()
                .body
                ?.string()
        }.getOrNull()
    }

    private suspend fun fetchFeed(url: String) = withContext(Dispatchers.IO) {
        val content = fetchContent(url) ?: return@withContext null
        val feed = FeedResolver.resolve(content)
        feed
    }

    suspend fun fetch(force: Boolean = false) = withContext(Dispatchers.IO) {
        if (!force && cache != null) {
            return@withContext cache!!
        }
        val feeds = FEED_URLS.map { url ->
            async {
                fetchFeed(url)
            }
        }.map {
            it.await()
        }
        println("feeds")
        feeds.forEach {
            println(it)
        }
        val list = feeds.filterNotNull()
        cache = list
        list
    }

    fun getPost(postId: String): Post? {
        cache?.forEach { feed ->
            feed.posts.forEach { post ->
                if (post.postId == postId) {
                    return post
                }
            }
        }
        return null
    }

}