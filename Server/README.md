Url to endpoint is
https://glennolsson.se/intnet/

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
    "reason": "DESCRIBE UNSUCCESS (STRING, UNDEFINED IF SUCCESS = TRUE)"
}
```

____
# SocketIO-API
Endpoint is /socket.io
## Socket emits
These are the requests sent to the server. Specified as socket.emit(...) by client

### init
Init client connection with username. **This must be done before any other socket call**

#### Body
```json
{
    "username": "USERNAME (STRING)"
}
```

### join
A user joins a new chatroom
#### Body
```json
{
    "chatid": "CHAT ID (INTEGER)"
}
```

### leave
A user leaves a chatroom on own behalf. No body required

### message
A new message is sent in a chat

#### Body
```json
{
    "content": "MESSAGE CONTENT (STRING)"
}
```

## IO emits
Emits sent by server. Specified as socket.on(...) by client

### join
A user joins a new chatroom
#### Body
```json
{
    "username": "USERNAME (STRING)"
}
```

### leave
A user is timed-out

#### Body
```json
{
    "username": "USERNAME (STRING)",
    "reason": "REASON TO LEAVE (STRING)"
}
```

### message
A new message is sent in a chat

#### Body
```json
{
    "username": "SENT USERNAME (STRING)",
    "content": "MESSAGE CONTENT (STRING)",
}
```

### notification
Notification about an event sent to user. An event can be that a user befriended them or a new profile comment on their profile.

#### Body
```json
{
    "message": "NOTIFICATION MESSAGE (STRING)"
}
```