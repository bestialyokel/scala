package Task

import scala.collection.mutable

abstract class AccountRepository {
    def getById(id: String): Option[Account]
    def put(account: Account): Option[Account]
    def bulkLoad(seq: Seq[Account]): Seq[Option[Account]] = seq.map( put )

    def values: Seq[Account]
}

class AccountRepositoryImpl extends AccountRepository {
    var hashMap: mutable.HashMap[String, Account] = new mutable.HashMap()

    def getById(id: String): Option[Account] = hashMap.get(id)

    def put(account: Account): Option[Account] = hashMap.get(account.id) match {
        case Some(account) => Some(account)
        case None => hashMap.put(account.id, account)
    }

    def values = hashMap.values.toSeq
}