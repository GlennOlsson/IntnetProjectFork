# REST-API
## GETs
### /rooms
Fetch all rooms possible to join

#### Response body
```json
[
    {
        "name": "ROOM NAME (STRING)",
        "userscount": "COUNT OF USERS IN (INTEGER)",
        "id": "ROOM ID"
    }
]
```

### /room/:id
Get information about a chatroom

#### Response body
```json
{
    "messages": [
        {
            "message": "MESSAGE CONTENT (STRING)",
            "sentby": "SENDER USERNAME (STRING)",
            "sent": "SENT DATE (STRING, YYYY-MM-DD;HH:mm)"
        }
    ]
}
```

### /profile/:username
Get general profile information about a user

#### Response body
```json
{
    "created": "CREATED DATE (STRING, YYYY-MM-DD;HH:mm)",
    "picture": "IMAGE (STRING, BASE64)",
    "bio": "USER BIOGRAPHY (STRING)",
    "friends": [
        "FRIEND USERNAME (STRING)"
    ],
    "comments": [
        {
            "by": "BY USERNAME (STRING)",
            "comment": "COMMENT CONENT (STRING)",
            "date": "COMMENT DATE (STRING, YYYY-MM-DD;HH:mm)"
        }
    ]
}
```
______
## POSTs
### /login
Login to app with username and password

#### Request body
```json
{
    "name": "USERNAME (STRING)",
    "password": "PASSWORD (STRING)"
}
```

#### Response body
```json
{
    "success": "LOGIN SUCCESSFULL (BOOLEAN)",
    "token": "TOKEN (STRING, NULL IF NOT SUCCESS)"
}
```

### /comment/:username
Comment on a users profile. The user to comment on is :username

#### Request body
```json
{
    "token": "COMMENTER TOKEN (STRING)",
    "name": "COMMENTER USERNAME (STRING)",
    "comment": "COMMENT CONTENT (STRING)"
}
```

### /friend/:username
Befriend someone. The user to befriend is :username

#### Request body
```json
{
    "token": "BEFRIENDER TOKEN(STRING)",
    "nane": "BEFRIENDER USERNMAE (STRING)"
}
```

## POSTs
### /newaccount
Create a new account with username and password

#### Request body
```json
{
    "name": "USERNAME (STRING)",
    "password": "PASSWORD (STRING)"
}
```

#### Response body
```json
{
    "success": "SUCCESS (BOOLEAN)",
    "reason": "DESCRIBE UNSUCCESS (STRING, UNDEFINED IF SUCCESS = TRUE"
}
```

# SocketIO-API
## IO emits (client)
Client emits content to io (all other clients and server), as in
```javascript
    let chats = io.of("/chat"); //Chat is the namespace
    chats.to(":chatid").emit(...); //:chatid is the group
```

This means that a client can both send and recieve these so it must implement functionallity for both

### join
A user joins a new chatroom
#### Body
```json
{
    "username": "USERNAME (STRING)"
}
```

### leave
A user leaves a chatroom on own behalf
#### Body
```json
{
    "username": "USERNAME (STRING)",
    "reason": "Disconnected (STATIC)"
}
```

### message
A new message is sent in a chat
```json
{
    "username": "SENT USERNAME (STRING)",
    "content": "MESSAGE CONTNET (STRING)",
    
}
```

## IO emits (server)
### leave
A user is timed-out

#### Body
```json
{
    "username": "USERNAME (STRING)",
    "reason": "Timed out (STATIC)"
}
```