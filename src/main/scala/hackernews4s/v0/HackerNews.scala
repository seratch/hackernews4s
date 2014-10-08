package hackernews4s.v0

import hackernews4s.v0.internal._
import skinny.http._
import skinny.logging.Logging
import skinny.util.JSONStringOps._

object HackerNews extends HackerNews

trait HackerNews extends Logging {

  val BASE_URL = "https://hacker-news.firebaseio.com/v0"

  /**
   * Items
   * Stories, comments, jobs, Ask HNs and even polls are just items.
   * They're identified by their ids, which are unique integers, and live under https://hacker-news.firebaseio.com/v0/item/.
   */
  def getItem(itemId: ItemId): Option[Item] = {
    val response = HTTP.get(s"${BASE_URL}/item/${itemId.id}.json")
    debugLogging("Items API", response)
    response.status match {
      case 200 => fromJSONString[RawItem](response.textBody).map(_.toItem)
      case s => throw new HackerNewsAPIException(s, response.textBody)
    }
  }

  /**
   * Users
   * Users are identified by case-sensitive ids, and live under https://hacker-news.firebaseio.com/v0/user/.
   */
  def getUser(userId: UserId): Option[User] = {
    val response = HTTP.get(s"${BASE_URL}/user/${userId.id}.json")
    debugLogging("Users API", response)
    response.status match {
      case 200 => fromJSONString[RawUser](response.textBody).map(_.toUser)
      case s => throw new HackerNewsAPIException(s, response.textBody)
    }
  }

  /**
   * Top Stories
   * The current top 100 stories are at https://hacker-news.firebaseio.com/v0/topstories.
   */
  def getItemIdsForTopStories(): Seq[ItemId] = {
    val response = HTTP.get(s"${BASE_URL}/topstories.json")
    debugLogging("Top Stories API", response)
    response.status match {
      case 200 => fromJSONString[Seq[Long]](response.textBody).map(_.map(ItemId)).getOrElse(Nil)
      case s => throw new HackerNewsAPIException(s, response.textBody)
    }
  }

  /**
   * Top Stories (Actual Items)
   */
  def getTopStories(limit: Int = 10): Seq[Item] = {
    getItemIdsForTopStories().take(limit)
      .par.flatMap(itemId => getItem(itemId))
      .toIndexedSeq
  }

  private[this] def debugLogging(api: String, response: Response): Unit = {
    logger.debug(s"${api} - (status: ${response.status}, body: ${response.textBody})")
  }

}
