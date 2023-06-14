# food-ordering-system

## Note
- The main target of all these architectures is to isolate the domain layer to develop, test, manage and deploy it separately.
- SAGA is used to create long running distributed transactions across services.
- Input ports are implemented in the domain layer but is not the adapter itself, whilst output ports are implemented with secondary adapters, with infrastructure modules.
- DIP(dependency inversion principle) leads to loosely coupled systems and an independent domain layer, by inverting any runtime dependency.
- Aggregate: Group of entities that are logically related. 
- Aggregate root: Root entity to keep the aggregate in a consistent state. 
- Entity: Core domain objects. 
- Value object: Used to bring context to the value. 
- Domain Event: Used to decouple the different domains. It will notify the other domains based on result of business logic.
- Value objects are immutable and only holds data, so identifier is not important for them. That means 2 value objects with same data, but different id’s considered to be the same value object.
- Entity classes do not have to be immutable. In fact they have state changes methods to run during business logic
- Application service should be the first contact point to outside of domain, and it will forward the call to the domain service and entities to complete a business logic.
- TransactionalEventListener is a spring annotation that listens an event that is fired from a transactional method. And it only process the event if the transactional operation is completed successfully.
- Kafka provides a replication factor. It replicates the data on different brokers on different servers. So even if one of the brokers is down, the data will still be available on other nodes.