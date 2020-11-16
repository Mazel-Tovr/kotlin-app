# simple-kotlin-app


### Application description
##### It's a simple crud application. To interaction with it is carried out by this APIs
###### Shortcut: req -> request / resp -> response / desc -> description /db -> database/ws -> WebSocket
###### Main controllers paths are this {controller} = [user,product,productgroup]
###### Resp status if everything good -> 200 OK otherwise 400 Bad req

| API | http req method | desc |
|:----------------:|:-------:|:---------:|
| ```http://your-host/{controller}/all``` | GET | get all entities from db|
| ```http://your-host/{controller}/{id}``` | GET | get entity with this id |
| ```http://your-host/{controller}``` | POST | create new entity (entity should be in req body) |
| ```http://your-host/{controller}``` | PUT | edit entity (entity should be in req body)|
| ```http://your-host/{controller}/{id}``` | DELETE | delete entity from db|

##### Application also have ws (some kind chat with some extra features) to connect to ws use this path ```http://your-host/ws```
###### Extra features are: u can subscribe on the event that can happened in REST (like someone wants to get a user with id = 1 , u will be informed about this)

##### As a chat client u should send ur name(or session id (what ever u want)) as a first message and event u want to subscribe : ([read,create,delete,update]); Send "" to subscribe to all events as second message



### Technologies :

| Technology | Status |
|:----------------:|:---------:|
| Ktor | Basic knowledge (How to start server,config it)|
| Kotlin coroutines | Basic knowledge (need more practice) |
| Kotlin dsl | Basic knowledge (need more practice) |
| Swagger for ktor | Know how to add my rest to swagger ui |
| kotlinx-collections-immutable | Know what is it , how to user , rest to know how wright to use it , i mean why should i use it here and basic collection here |
| ktor-websockets | Basic knowledge |
