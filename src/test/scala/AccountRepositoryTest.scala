import Task.{Account, AccountRepositoryImpl, BoomerAccountFactory, OpportunistAccountFactory, SocialNetwork, ZoomerAccountFactory}
import org.scalatest._
import flatspec._
import matchers._

import java.util.UUID

class AccountRepositoryTest extends AnyFlatSpec with should.Matchers {
    "Account repository" should "bulk load should add all elements from seq" in {
        val repository = new AccountRepositoryImpl()

        val initCount = repository.count

        val accounts = Seq(
            new ZoomerAccountFactory().make(),
            new BoomerAccountFactory().make(),
            new OpportunistAccountFactory().make()
        )

        repository.bulkLoad(accounts)

        repository.count - initCount shouldBe accounts.length
    }

    "Account repository" should "put method should add only unique accounts(by id)" in {
        val repository = new AccountRepositoryImpl()

        val account = new ZoomerAccountFactory().make()

        repository.put(account).isEmpty shouldBe true
        repository.put(account).isEmpty shouldBe false
    }

    "Account repository" should "be able to return added values with ::getById method" in {
        val repository = new AccountRepositoryImpl()

        val account = new ZoomerAccountFactory().make()

        repository.put(account)

        repository.getById(account.id).nonEmpty shouldBe true
    }
}