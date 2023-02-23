# [WIP] Distributify
This plugin exposes API that mocks default Java structures hiding underneath remote storage (like Redis).

**NOT READY FOR PRODUCTION USE. REPORT ANY ISSUES TO THIS GITHUB**
****
## Getting started

## Add to plugin


Is it "thread-safe"?
---
Depends on what do you mean by "thread-safe". Usually this depends on underlying driver/storage.

People tend to call "thread-safe" a few rather different things like safety, concurrency and persistence. so here is a little FAQ for Redis to figure out your answer.


### **Can we call methods of the structures from different threads?**

Yes. There is no java synchronization code to make sure it's safe though. API allows Redis to figure out safety on it's own. 

But if you are **NOT** in full async or "almost" async environment (aka Velocity):

> You just need to be sure that your structure was created before you call its methods.
By the design of the API it should be rather difficult to replicate such behaviour.

### **Can we operate on structures that use same key concurrently and safely?**

**TL;DR** Concurrently? Yes. Safely? Maybe not.

**Long answer:**

As answered in first question - we can delegate handling of concurrency and safety to underlying storage, so there is no Java synchronization code in the implementation for Redis.
Some implementations instead of Redis might use it though. Better check the javadocs for each implementation.

In Redis, depending on implementation we might or might not use [Transactions](https://redis.io/docs/manual/transactions/).

For example `DistributedHashMap` does not guarantee safety, opposing to `DistributedSafeHashMap`, and so it does not make use of Redis Transactions.
Essentially this means that there **IS** possibility of race conditions and data corruption in the Redis when 
working with same keys at the same time.
Usage of Redis Transaction introduces a bit of overhead, which might not be noticeable on low loads, 
but in some use-cases it's either not likely to happen, or just a better tradeoff. 

Like if you're synchronizing list of players on your Velocity instances (like RedisBungee did) to avoid players (with same names) joining different Velocity instances at the same time.

Usually normal players does not tend to join same server with same nickname, so it's better to use `DistributedHashMap`.

In case of cache that might be modified often it might be better to use safe variation of HashMap.

#### A good rule of thumb:
> If you are fine with using `ConcurrentHashMap` go for `DistributedHashMap`, in other cases go with `DistributedSafeHashMap`.

But still consider the use case. `DistributedHashMap` creates new Redis key for each entry. If you won't modify the entry very much, you can still get away with using unsafe implementation for better perfomance.

In case `DistributedLongCounter` that uses same key to contain Long value that might be changed by multitude of instances we use Transactions.
This means there is NO possibility that commands that grouped in Transaction would be executed while executing other transaction.

### **Is it persistent?**
This is not handled by the API and you should consult with Redis documentation on [Persistence](https://redis.io/docs/management/persistence/)




Current Status
---
**API**
- [x] HashMap
- [x] SafeHashMap
- [x] Counter
- [ ] Message broking
- [ ] ... more ..?

**Drivers (NoSQL)**
- [x] Redis
- [ ] ... more ..?

**Drivers (Message Brokers)**
- [ ] Redis Pub/Sub
- [ ] RabbitMQ
- [ ] ... more ..?

**Misc**
- [ ] Runtime drivers loading 
- [ ] Sane driver specific configuration
- [ ] Proper Gradle setup