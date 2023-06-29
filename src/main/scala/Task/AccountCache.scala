package Task

import scala.collection.mutable

abstract class AccountCache(val cacheEntryTTL: Long) {
    case class CacheEntry(account: Account, lastUpdateTs: Long)

    val repository: AccountRepository

    def getById(id: String): Option[Account]

    def postInstagram(): Unit

    protected def entryOverdue(entry: CacheEntry): Boolean

    protected def updateEntry(entry: CacheEntry): Option[CacheEntry]

    protected def put(account: Account): Option[CacheEntry]
}

class AccountCacheImpl(val repository: AccountRepository, override val cacheEntryTTL: Long) extends AccountCache(cacheEntryTTL) {
    val hashMap = new mutable.HashMap[String, CacheEntry]()

    override def getById(id: String): Option[Account] = {
        hashMap.get(id) match {
            case Some(entry) => if (entryOverdue(entry)) updateEntry(entry).map(_.account) else Some(entry.account)
            case None => repository.getById(id) match {
                case Some(account) => put(account).map(_.account)
            }
        }
    }

    def getCurrentTs() = System.currentTimeMillis()

    override def put(account: Account) = {
        hashMap.get(account.id) match {
            case Some(entry) => Some(entry)
            case None => hashMap.put(account.id, CacheEntry(account, getCurrentTs()))
        }
    }

    override def entryOverdue(entry: CacheEntry): Boolean = getCurrentTs() - entry.lastUpdateTs > cacheEntryTTL

    override def postInstagram(): Unit = {
        hashMap.values
          .filter(entry => entry.account.socialNetworks.contains(SocialNetwork.Instagram))
          .map(entry => if (entryOverdue(entry)) Some(entry) else updateEntry(entry))
          .filter(_.nonEmpty)
          .map(_.get)
          .foreach(entry => entry.account.post(SocialNetwork.Instagram))
    }

    override def updateEntry(cacheEntry: CacheEntry): Option[CacheEntry] = repository.getById(cacheEntry.account.id) match {
        case Some(account) => hashMap.put(account.id, CacheEntry(account, getCurrentTs()))
        case None => None
    }
}