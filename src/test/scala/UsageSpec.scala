import org.scalatest._
import hackernews4s.v0._

class UsageSpec extends FunSpec with Matchers {

  describe("HackerNews") {

    it("retrieves items") {
      val item = HackerNews.getItem(ItemId(8863))
      item.isDefined should be(true)
      item.get.by.map(_.id) should equal(Some("dhouston"))
    }

    it("retrieves users") {
      val user = HackerNews.getUser(UserId("jl"))
      user.isDefined should be(true)
      user.map(_.about) should equal(Some(Some("This is a test")))
    }

    it("retrieves ids for top stories") {
      val itemIds = HackerNews.getItemIdsForTopStories()
      itemIds.size should equal(500)
    }

    it("retrieves top stories") {
      val items = HackerNews.getTopStories()
      items.size should equal(10)
    }

    it("retrieves top stories with limit value") {
      val items = HackerNews.getTopStories(5)
      items.size should equal(5)
    }

    it("retrieves ids for new stories") {
      val itemIds = HackerNews.getItemIdsForNewStories()
      itemIds.size should equal(500)
    }

    it("retrieves new stories") {
      val items = HackerNews.getNewStories()
      items.size should equal(10)
    }

    it("retrieves new stories with limit value") {
      val items = HackerNews.getNewStories(5)
      items.size should equal(5)
    }

    it("retrieves ids for ask stories") {
      val itemIds = HackerNews.getItemIdsForAskStories()
      itemIds.size should be > (10)
    }

    it("retrieves ask stories") {
      val items = HackerNews.getAskStories()
      items.size should equal(10)
    }

    it("retrieves ask stories with limit value") {
      val items = HackerNews.getAskStories(5)
      items.size should equal(5)
    }

    it("retrieves ids for job stories") {
      val itemIds = HackerNews.getItemIdsForJobStories()
      itemIds.size should be > (10)
    }

    it("retrieves job stories") {
      val items = HackerNews.getJobStories()
      items.size should equal(10)
    }

    it("retrieves job stories with limit value") {
      val items = HackerNews.getJobStories(5)
      items.size should equal(5)
    }

    it("retrieves the current largest item id via #getMaxItemId") {
      val itemId: ItemId = HackerNews.getMaxItemId()
      itemId.id should be > (8447116L)
    }

    it("retrieves the current largest item id via #getCurrentLargestItemId") {
      val itemId: ItemId = HackerNews.getCurrentLargestItemId()
      itemId.id should be > (8447116L)
    }

    it("retrieves ids for changed items and profiles") {
      val response: ChangedItemsAndProfiles = HackerNews.getIdsForChangedItemsAndProfiles()
      response.itemIds.size should be > (0)
      response.userIds.size should be > (0)
    }

    it("retrieves changed items") {
      val items: Seq[Item] = HackerNews.getChangedItems()
      items.size should be > (0)
    }

    it("retrieves changed profiles") {
      val users: Seq[User] = HackerNews.getChangedProfiles()
      users.size should be > (0)
    }

  }

}
