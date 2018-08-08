package hackernews4s.v0

import skinny.http._
import skinny.logging.Logging
import skinny.json4s.JSONStringOps._
import scala.util._

/**
 * HackerNews API client
 */
object HackerNews extends HackerNews

/**
 * HackerNews API client
 *
 * {{{
 * scala> import hackernews4s.v0._
 * scala> val hn = new HackerNews {}
 * scala> val item: Option[Item] = hn.getItem(ItemId(123))
 * }}}
 */
trait HackerNews extends Logging {
  import hackernews4s.v0.internal._

  val BASE_URL = "https://hacker-news.firebaseio.com/v0"

  private[this] val CONCURRENCY: Int = 10

  /**
   * Retrieves a HackerNews item from Items API.
   * Stories, comments, jobs, Ask HNs and even polls are just items.
   * They're identified by their ids, which are unique integers, and live under https://hacker-news.firebaseio.com/v0/item/.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> val item: Option[Item] = HackerNews.getItem(ItemId(123))
   * }}}
   */
  def getItem(itemId: ItemId): Option[Item] = {
    val response = HTTP.get(req(s"${BASE_URL}/item/${itemId.id}.json"))
    debugLogging("Items API", response)
    response.status match {
      case 200 =>
        fromJSONString[RawItem](response.textBody) match {
          case Success(rawItem) => Some(rawItem.toItem)
          case Failure(error) =>
            logger.info("Failed to parse response body", error)
            None
        }
      case 401 =>
        // HackerNews API sometimes returns (temporal?) 401
        logger.info(s"Failed to access an item (id: ${itemId}) - ${response.textBody}")
        None
      case s => throw new HackerNewsAPIException(s, response.textBody)
    }
  }

  /**
   * Retrieves a HackerNews user information from Users API.
   * Users are identified by case-sensitive ids, and live under https://hacker-news.firebaseio.com/v0/user/.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> val user: Option[User] = HackerNews.getUser(UserId("seratch"))
   * }}}
   */
  def getUser(userId: UserId): Option[User] = {
    val response = HTTP.get(req(s"${BASE_URL}/user/${userId.id}.json"))
    debugLogging("Users API", response)
    response.status match {
      case 200 =>
        fromJSONString[RawUser](response.textBody) match {
          case Success(rawUser) => Some(rawUser.toUser)
          case Failure(error) =>
            logger.info("Failed to parse response body", error)
            None
        }
      case 401 =>
        // HackerNews API sometimes returns (temporal?) 401
        logger.info(s"Failed to access an item (id: ${userId}) - ${response.textBody}")
        None
      case s => throw new HackerNewsAPIException(s, response.textBody)
    }
  }

  /**
   * Retrieves HackerNews top stories' item ids from Top Stories API.
   * The current top 400+ stories are at https://hacker-news.firebaseio.com/v0/topstories.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> val ids: Seq[ItemId] = HackerNews.getItemIdsForTopStories()
   * scala> ids.size >= 400
   * res0: Boolean = true
   * }}}
   */
  def getItemIdsForTopStories(): Seq[ItemId] = {
    val response = HTTP.get(req(s"${BASE_URL}/topstories.json"))
    debugLogging("Top Stories API", response)
    extractItemIds(response)
  }

  /**
   * Retrieves actual top stories.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> val items: Seq[Item] = HackerNews.getTopStories()
   * scala> items.size
   * res0: Int = 10
   * }}}
   */
  def getTopStories(limit: Int = 10): Seq[Item] = toItems(getItemIdsForTopStories().take(limit))

  /**
   * Retrieves HackerNews top stories' item ids from New Stories API.
   * The current new 400+ stories are at https://hacker-news.firebaseio.com/v0/newstories.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> val ids: Seq[ItemId] = HackerNews.getItemIdsForNewStories()
   * scala> ids.size >= 400
   * res0: Boolean = true
   * }}}
   */
  def getItemIdsForNewStories(): Seq[ItemId] = {
    val response = HTTP.get(req(s"${BASE_URL}/newstories.json"))
    debugLogging("New Stories API", response)
    extractItemIds(response)
  }

  /**
   * Retrieves actual new stories.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> val items: Seq[Item] = HackerNews.getNewStories()
   * scala> items.size
   * res0: Int = 10
   * }}}
   */
  def getNewStories(limit: Int = 10): Seq[Item] = toItems(getItemIdsForNewStories().take(limit))

  /**
   * Retrieves HackerNews stories' item ids from Ask Stories API.
   * The current new 400+ stories are at https://hacker-news.firebaseio.com/v0/askstories.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> val ids: Seq[ItemId] = HackerNews.getItemIdsForAskStories()
   * }}}
   */
  def getItemIdsForAskStories(): Seq[ItemId] = {
    val response = HTTP.get(req(s"${BASE_URL}/askstories.json"))
    debugLogging("Ask Stories API", response)
    extractItemIds(response)
  }

  /**
   * Retrieves actual ask stories.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> val items: Seq[Item] = HackerNews.getAskStories()
   * }}}
   */
  def getAskStories(limit: Int = 10): Seq[Item] = toItems(getItemIdsForAskStories().take(limit))

  /**
   * Retrieves HackerNews stories' item ids from Show Stories API.
   * The current new 400+ stories are at https://hacker-news.firebaseio.com/v0/showstories.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> val ids: Seq[ItemId] = HackerNews.getItemIdsForShowStories()
   * }}}
   */
  def getItemIdsForShowStories(): Seq[ItemId] = {
    val response = HTTP.get(req(s"${BASE_URL}/showstories.json"))
    debugLogging("Show Stories API", response)
    extractItemIds(response)
  }

  /**
   * Retrieves actual show stories.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> val items: Seq[Item] = HackerNews.getShowStories()
   * }}}
   */
  def getShowStories(limit: Int = 10): Seq[Item] = toItems(getItemIdsForShowStories().take(limit))

  /**
   * Retrieves HackerNews stories' item ids from Job Stories API.
   * The current new 400+ stories are at https://hacker-news.firebaseio.com/v0/jobstories.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> val ids: Seq[ItemId] = HackerNews.getItemIdsForJobStories()
   * }}}
   */
  def getItemIdsForJobStories(): Seq[ItemId] = {
    val response = HTTP.get(req(s"${BASE_URL}/jobstories.json"))
    debugLogging("Job Stories API", response)
    extractItemIds(response)
  }

  /**
   * Retrieves actual job stories.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> val items: Seq[Item] = HackerNews.getJobStories()
   * scala> items.size
   * res0: Int = 10
   * }}}
   */
  def getJobStories(limit: Int = 10): Seq[Item] = toItems(getItemIdsForJobStories().take(limit))

  /**
   * Max Item ID
   *
   * The current largest item id is at https://hacker-news.firebaseio.com/v0/maxitem.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> val itemId: ItemId = HackerNews.getMaxItemId()
   * scala> itemId.id > 8447116L
   * res0: Boolean = true
   * }}}
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

  /**
   * {{{
   * scala> import hackernews4s.v0._
   * scala> val itemId: ItemId = HackerNews.getCurrentLargestItemId()
   * }}}
   */
  def getCurrentLargestItemId(): ItemId = getMaxItemId()

  /**
   * Retrieves changed item ids and user ids from Changed Items and Profiles API.
   *
   * The item and profile changes are at https://hacker-news.firebaseio.com/v0/updates.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> val ChangedItemsAndProfiles(itemIds, userIds) = HackerNews.getIdsForChangedItemsAndProfiles()
   * }}}
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
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> HackerNews.getChangedItems().size > 0
   * res0: Boolean = true
   * }}}
   */
  def getChangedItems(): Seq[Item] = toItems(getIdsForChangedItemsAndProfiles().itemIds)

  /**
   * Retrieves actual changed user data for ids from "Changed Items and Profiles API".
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> HackerNews.getChangedProfiles().size >= 0
   * res0: Boolean = true
   * }}}
   */
  def getChangedProfiles(): Seq[User] = {
    getIdsForChangedItemsAndProfiles().userIds.grouped(CONCURRENCY).flatMap(ids =>
      ids.par.flatMap(userId => getUser(userId)).toIndexedSeq).toSeq
  }

  // creates skinny.http.Request for each API call
  private[this] def req(url: String): Request = {
    new Request(url)
      .connectTimeoutMillis(2000) // HackerNews API sometimes cannot accept new connection
  }

  private[this] def extractItemIds(response: Response): Seq[ItemId] = {
    response.status match {
      case 200 => fromJSONString[Seq[Long]](response.textBody).map(_.map(ItemId)).getOrElse(Nil)
      case s => throw new HackerNewsAPIException(s, response.textBody)
    }
  }

  private[this] def toItems(itemIds: Seq[ItemId]): Seq[Item] = {
    itemIds.grouped(CONCURRENCY).flatMap(ids =>
      ids.par.flatMap(id => getItem(id)).toIndexedSeq).toSeq
  }

  private[this] def debugLogging(api: String, response: Response): Unit = {
    logger.debug(s"${api} - (status: ${response.status}, body: ${response.textBody})")
  }

}
