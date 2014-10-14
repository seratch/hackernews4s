package hackernews4s.v0

import skinny.http._
import skinny.logging.Logging
import skinny.util.JSONStringOps._

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
 * scala> hn.getItem(ItemId(123))
 * res0: Option[Item] = Some(Item(ItemId(123),false,Story,Some(UserId(beau)),2007-02-20T07:21:13.000+09:00,,false,None,List(ItemId(33749), ItemId(454556)),Some(http://design.caltech.edu/erik/Misc/design_quotes.html),8,Some(Design Quotations: &#34;And if in fact you do know the exact cost and the exact schedule, chances are that the technology is obsolete.&#34;),List()))
 * }}}
 */
trait HackerNews extends Logging {
  import hackernews4s.v0.internal._

  val BASE_URL = "https://hacker-news.firebaseio.com/v0"

  private[this] val CONCURRENCY: Int = 10

  /**
   * Retrieves a HackerNews item from Items API.
   *
   * Stories, comments, jobs, Ask HNs and even polls are just items.
   * They're identified by their ids, which are unique integers, and live under https://hacker-news.firebaseio.com/v0/item/.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> HackerNews.getItem(ItemId(123))
   * res0: Option[Item] = Some(Item(ItemId(123),false,Story,Some(UserId(beau)),2007-02-20T07:21:13.000+09:00,,false,None,List(ItemId(33749), ItemId(454556)),Some(http://design.caltech.edu/erik/Misc/design_quotes.html),8,Some(Design Quotations: &#34;And if in fact you do know the exact cost and the exact schedule, chances are that the technology is obsolete.&#34;),List()))
   * }}}
   */
  def getItem(itemId: ItemId): Option[Item] = {
    val response = HTTP.get(req(s"${BASE_URL}/item/${itemId.id}.json"))
    debugLogging("Items API", response)
    response.status match {
      case 200 => fromJSONString[RawItem](response.textBody).map(_.toItem)
      case 401 =>
        // HackerNews API sometimes returns (temporal?) 401
        logger.info(s"Failed to access an item (id: ${itemId}) - ${response.textBody}")
        None
      case s => throw new HackerNewsAPIException(s, response.textBody)
    }
  }

  /**
   * Retrieves a HackerNews user information from Users API.
   *
   * Users are identified by case-sensitive ids, and live under https://hacker-news.firebaseio.com/v0/user/.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> HackerNews.getUser(UserId("seratch")).map(_.createdAt)
   * res0: Option[org.joda.time.DateTime] = Some(2012-02-12T12:10:02.000+09:00)
   * }}}
   */
  def getUser(userId: UserId): Option[User] = {
    val response = HTTP.get(req(s"${BASE_URL}/user/${userId.id}.json"))
    debugLogging("Users API", response)
    response.status match {
      case 200 => fromJSONString[RawUser](response.textBody).map(_.toUser)
      case 401 =>
        // HackerNews API sometimes returns (temporal?) 401
        logger.info(s"Failed to access an item (id: ${userId}) - ${response.textBody}")
        None
      case s => throw new HackerNewsAPIException(s, response.textBody)
    }
  }

  /**
   * Retrieves a HackerNews top stories' item ids from Top Stories API.
   *
   * The current top 100 stories are at https://hacker-news.firebaseio.com/v0/topstories.
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> HackerNews.getItemIdsForTopStories().size
   * res0: Int = 100
   * }}}
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
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> HackerNews.getTopStories().size
   * res0: Int = 10
   * }}}
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
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> HackerNews.getMaxItemId().id > 8447116L
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
   * scala> HackerNews.getCurrentLargestItemId().id > 8447116L
   * res0: Boolean = true
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
  def getChangedItems(): Seq[Item] = {
    getIdsForChangedItemsAndProfiles().itemIds.grouped(CONCURRENCY).flatMap(ids =>
      ids.par.flatMap(itemId => getItem(itemId)).toIndexedSeq
    ).toSeq
  }

  /**
   * Retrieves actual changed user data for ids from "Changed Items and Profiles API".
   *
   * {{{
   * scala> import hackernews4s.v0._
   * scala> HackerNews.getChangedProfiles().size > 0
   * res0: Boolean = true
   * }}}
   */
  def getChangedProfiles(): Seq[User] = {
    getIdsForChangedItemsAndProfiles().userIds.grouped(CONCURRENCY).flatMap(ids =>
      ids.par.flatMap(userId => getUser(userId)).toIndexedSeq
    ).toSeq
  }

  // creates skinny.http.Request for each API call
  private[this] def req(url: String): Request = {
    new Request(url)
      .connectTimeoutMillis(2000) // HackerNews API sometimes cannot accept new connection
  }

  private[this] def debugLogging(api: String, response: Response): Unit = {
    logger.debug(s"${api} - (status: ${response.status}, body: ${response.textBody})")
  }

}
