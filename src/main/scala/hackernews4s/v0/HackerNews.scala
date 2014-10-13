package hackernews4s.v0

import hackernews4s.v0.internal._
import skinny.http._
import skinny.logging.Logging
import skinny.util.JSONStringOps._

/**
 * HackerNews API client
 */
object HackerNews extends HackerNews

/**
 * HackerNews API client
 */
trait HackerNews extends Logging {

  val BASE_URL = "https://hacker-news.firebaseio.com/v0"

  private[this] val CONCURRENCY: Int = 3

  /**
   * Items
   *
   * Stories, comments, jobs, Ask HNs and even polls are just items.
   * They're identified by their ids, which are unique integers, and live under https://hacker-news.firebaseio.com/v0/item/.
   */
  def getItem(itemId: ItemId): Option[Item] = {
    val response = HTTP.get(req(s"${BASE_URL}/item/${itemId.id}.json"))
    debugLogging("Items API", response)
    response.status match {
      case 200 => fromJSONString[RawItem](response.textBody).map(_.toItem)
      case s => throw new HackerNewsAPIException(s, response.textBody)
    }
  }

  /**
   * Users
   *
   * Users are identified by case-sensitive ids, and live under https://hacker-news.firebaseio.com/v0/user/.
   */
  def getUser(userId: UserId): Option[User] = {
    val response = HTTP.get(req(s"${BASE_URL}/user/${userId.id}.json"))
    debugLogging("Users API", response)
    response.status match {
      case 200 => fromJSONString[RawUser](response.textBody).map(_.toUser)
      case s => throw new HackerNewsAPIException(s, response.textBody)
    }
  }

  /**
   * Top Stories
   *
   * The current top 100 stories are at https://hacker-news.firebaseio.com/v0/topstories.
   */
  def getItemIdsForTopStories(): Seq[ItemId] = {
    val response = HTTP.get(req(s"${BASE_URL}/topstories.json"))
    debugLogging("Top Stories API", response)
    response.status match {
      case 200 => fromJSONString[Seq[Long]](response.textBody).map(_.map(ItemId)).getOrElse(Nil)
      case s => throw new HackerNewsAPIException(s, response.textBody)
    }
  }

  /**
   * Retrieves actual top stories.
   */
  def getTopStories(limit: Int = 10): Seq[Item] = {
    getItemIdsForTopStories().take(limit).grouped(CONCURRENCY).flatMap(ids =>
      ids.par.flatMap(id => getItem(id)).toIndexedSeq
    ).toSeq
  }

  /**
   * Max Item ID
   *
   * The current largest item id is at https://hacker-news.firebaseio.com/v0/maxitem.
   */
  def getMaxItemId(): ItemId = {
    val response = HTTP.get(req(s"${BASE_URL}/maxitem.json"))
    debugLogging("Max Item ID API", response)
    response.status match {
      case 200 =>
        fromJSONString[Long](response.textBody).map(ItemId)
          .getOrElse(throw new HackerNewsAPIException(200, response.textBody))
      case s => throw new HackerNewsAPIException(s, response.textBody)
    }
  }

  def getCurrentLargestItemId(): ItemId = getMaxItemId()

  /**
   * Changed Items and Profiles
   *
   * The item and profile changes are at https://hacker-news.firebaseio.com/v0/updates.
   */
  def getIdsForChangedItemsAndProfiles(): ChangedItemsAndProfiles = {
    val response = HTTP.get(req(s"${BASE_URL}/updates.json"))
    debugLogging("Changed Items and Profiles API", response)
    response.status match {
      case 200 =>
        fromJSONString[RawChangedItemsAndProfiles](response.textBody).map(_.toChangedItemsAndProfiles)
          .getOrElse(throw new HackerNewsAPIException(200, response.textBody))
      case s => throw new HackerNewsAPIException(s, response.textBody)
    }
  }

  /**
   * Retrieves actual changed item data for ids from "Changed Items and Profiles API".
   */
  def getChangedItems(): Seq[Item] = {
    getIdsForChangedItemsAndProfiles().itemIds.grouped(CONCURRENCY).flatMap(ids =>
      ids.par.flatMap(itemId => getItem(itemId)).toIndexedSeq
    ).toSeq
  }

  /**
   * Retrieves actual changed user data for ids from "Changed Items and Profiles API".
   */
  def getChangedProfiles(): Seq[User] = {
    getIdsForChangedItemsAndProfiles().userIds.grouped(CONCURRENCY).flatMap(ids =>
      ids.par.flatMap(userId => getUser(userId)).toIndexedSeq
    ).toSeq
  }

  // creates skinny.http.Request for each API call
  private[this] def req(url: String): Request = {
    val req = new Request(url)
    req.connectTimeoutMillis(2000) // HackerNews API sometimes cannot accept new connection
    req
  }

  private[this] def debugLogging(api: String, response: Response): Unit = {
    logger.debug(s"${api} - (status: ${response.status}, body: ${response.textBody})")
  }

}
