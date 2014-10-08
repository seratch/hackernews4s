import org.scalatest._
import hackernews4s.v0._

class UsageSpec extends FunSpec with Matchers {

  describe("HackerNews") {

    it("retrieves items") {
      val item = HackerNews.getItem(ItemId(8863))
      item.isDefined should be(true)
      item.map(_.by.id) should equal(Some("dhouston"))
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
      items.size should equal(100)
    }

  }

}
