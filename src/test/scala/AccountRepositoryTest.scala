import Task.{Account, AccountRepositoryImpl, BoomerAccountFactory, OpportunistAccountFactory, SocialNetwork, ZoomerAccountFactory}
import org.scalatest._
import flatspec._
import matchers._

import java.util.UUID

class AccountRepositoryTest extends AnyFlatSpec with should.Matchers {
    "Account repository" should "bulk load should add all elements from seq" in {
        val repository = new AccountRepositoryImpl()

        val accounts = Seq(
            new ZoomerAccountFactory().make(),
            new BoomerAccountFactory().make(),
            new OpportunistAccountFactory().make()
        )

        repository.bulkLoad( accounts )




    }
}