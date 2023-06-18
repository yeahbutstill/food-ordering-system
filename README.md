# food-ordering-system

![1](img/project-overview-section-1.png)

## Run Postgres
```shell
docker run --rm \
--name food-ordering-system \
-e POSTGRES_DB=food-ordering-systemdb \
-e POSTGRES_USER=foodorderingsystem \
-e POSTGRES_PASSWORD=PNSJkxXvVNDAhePMuExTBuRR \
-e PGDATA=/var/lib/postgresql/data/pgdata \
-v "$PWD/food-ordering-systemdb-data:/var/lib/postgresql/data" \
-p 5432:5432 \
postgres:15
```

## Create schema and Insert Data
```postgres-sql
DROP SCHEMA IF EXISTS restaurant CASCADE;

CREATE SCHEMA restaurant;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS restaurant.restaurants CASCADE;

CREATE TABLE restaurant.restaurants
(
    id uuid NOT NULL,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    active boolean NOT NULL,
    CONSTRAINT restaurants_pkey PRIMARY KEY (id)
);

DROP TYPE IF EXISTS approval_status;

CREATE TYPE approval_status AS ENUM ('APPROVED', 'REJECTED');

DROP TABLE IF EXISTS restaurant.order_approval CASCADE;

CREATE TABLE restaurant.order_approval
(
    id uuid NOT NULL,
    restaurant_id uuid NOT NULL,
    order_id uuid NOT NULL,
    status approval_status NOT NULL,
    CONSTRAINT order_approval_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS restaurant.products CASCADE;

CREATE TABLE restaurant.products
(
    id uuid NOT NULL,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    price numeric(10,2) NOT NULL,
    available boolean NOT NULL,
    CONSTRAINT products_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS restaurant.restaurant_products CASCADE;

CREATE TABLE restaurant.restaurant_products
(
    id uuid NOT NULL,
    restaurant_id uuid NOT NULL,
    product_id uuid NOT NULL,
    CONSTRAINT restaurant_products_pkey PRIMARY KEY (id)
);

ALTER TABLE restaurant.restaurant_products
    ADD CONSTRAINT "FK_RESTAURANT_ID" FOREIGN KEY (restaurant_id)
    REFERENCES restaurant.restaurants (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE RESTRICT
    NOT VALID;

ALTER TABLE restaurant.restaurant_products
    ADD CONSTRAINT "FK_PRODUCT_ID" FOREIGN KEY (product_id)
    REFERENCES restaurant.products (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE RESTRICT
    NOT VALID;

DROP MATERIALIZED VIEW IF EXISTS restaurant.order_restaurant_m_view;

CREATE MATERIALIZED VIEW restaurant.order_restaurant_m_view
TABLESPACE pg_default
AS
 SELECT r.id AS restaurant_id,
    r.name AS restaurant_name,
    r.active AS restaurant_active,
    p.id AS product_id,
    p.name AS product_name,
    p.price AS product_price,
    p.available AS product_available
   FROM restaurant.restaurants r,
    restaurant.products p,
    restaurant.restaurant_products rp
  WHERE r.id = rp.restaurant_id AND p.id = rp.product_id
WITH DATA;

refresh materialized VIEW restaurant.order_restaurant_m_view;

DROP function IF EXISTS restaurant.refresh_order_restaurant_m_view;

CREATE OR replace function restaurant.refresh_order_restaurant_m_view()
returns trigger
AS '
BEGIN
    refresh materialized VIEW restaurant.order_restaurant_m_view;
    return null;
END;
'  LANGUAGE plpgsql;

DROP trigger IF EXISTS refresh_order_restaurant_m_view ON restaurant.restaurant_products;

CREATE trigger refresh_order_restaurant_m_view
after INSERT OR UPDATE OR DELETE OR truncate
ON restaurant.restaurant_products FOR each statement
EXECUTE PROCEDURE restaurant.refresh_order_restaurant_m_view();

INSERT INTO restaurant.restaurants(id, name, active)
	VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb45', 'restaurant_1', TRUE);
INSERT INTO restaurant.restaurants(id, name, active)
	VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb46', 'restaurant_2', FALSE);

INSERT INTO restaurant.products(id, name, price, available)
	VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb47', 'product_1', 25.00, FALSE);
INSERT INTO restaurant.products(id, name, price, available)
	VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb48', 'product_2', 50.00, TRUE);
INSERT INTO restaurant.products(id, name, price, available)
	VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb49', 'product_3', 20.00, FALSE);
INSERT INTO restaurant.products(id, name, price, available)
	VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb50', 'product_4', 40.00, TRUE);

INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
	VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb51', 'd215b5f8-0249-4dc5-89a3-51fd148cfb45', 'd215b5f8-0249-4dc5-89a3-51fd148cfb47');
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
	VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb52', 'd215b5f8-0249-4dc5-89a3-51fd148cfb45', 'd215b5f8-0249-4dc5-89a3-51fd148cfb48');
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
	VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb53', 'd215b5f8-0249-4dc5-89a3-51fd148cfb46', 'd215b5f8-0249-4dc5-89a3-51fd148cfb49');
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
	VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb54', 'd215b5f8-0249-4dc5-89a3-51fd148cfb46', 'd215b5f8-0249-4dc5-89a3-51fd148cfb50');
```

## Run Zookeper
```shell
# goto directory
cd infrastructure/docker-compose
# run
docker compose -f common.yml -f zookeeper.yml up
# open new terminal test
echo ruok | nc localhost 2181
```
https://zookeeper.apache.org/doc/r3.1.2/zookeeperAdmin.html#sc_zkCommands

## Run Kafka Cluster
```shell
# cd infrastructure/docker-compose
docker compose -f common.yml -f kafka_cluster.yml up
# open new terminal and run this for create topic
docker compose -f common.yml -f init_kafka.yml up
```

## Let's confirm this by opening the kafka manager user interface
```http request
localhost:9000
```

## Add Cluster
![add_cluster](img/img.png)
And save. Now, if I list the clusters, I will see three brokers and a topic created using init-kafka.yml docker compose file
![cluster_information](img/img_1.png)

##### ingat saat anda menggunakan pemetaan volume, anda harus terlebih dahulu memulai Zookeeper dan kemudian memulai Cluster Kafka, karena kafka cluster memeriksa kesehatan Zookeeper saat startup dan gagal jika tidak sehat
kedepannya saya akan coba menganti docker compose dengan cp-helm-charts dari kubernetes untuk menjalankan Cluster Kafka

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
- KafkaTemplate is a spring component that wraps a Kafka Producer and provides method to easily produce data on kafka. 
- To create a Kafka Consumer with spring, using a KafkaListener annotation on a simple method is enough. 
- If my kafka topic has n partitions, I cannot use more than n threads to consume the data on topics. 
- Kafka Consumer has a max poll records property that limits the number of records returned for a single poll.
- ControllerAdvice: Used to catch exceptions application wide. 
- RestController: Used to create a rest controller with path mapping, and accept type. 
- RequestBody: Used to mark a java class as a request body in the rest request. Spring will map the json body into this class by setting the values automatically. 
- PostMapping: Used to create a method that will serve to a http post request.