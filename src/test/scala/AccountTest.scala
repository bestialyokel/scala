import Task.{Account, SocialNetwork, ZoomerAccountFactory}
import org.scalatest._
import flatspec._
import matchers._

import java.util.UUID

class AccountTest extends AnyFlatSpec with should.Matchers {
    "Account post method" should "should post only if account uses SocialNetwork" in {
        val account = new ZoomerAccountFactory().make()
        account.post(SocialNetwork.Instagram).isFailure shouldBe true
        account.post(SocialNetwork.TikTok).isSuccess shouldBe true
    }
}