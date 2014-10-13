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
      itemIds.size should equal(100)
    }

    it("retrieves top stories") {
      val items = HackerNews.getTopStories()
      items.size should equal(10)
    }

    it("retrieves top stories with limit value") {
      val items = HackerNews.getTopStories(5)
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
