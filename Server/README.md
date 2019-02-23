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
Comment on a users profile\

#### Request body
```json
{
    "token": "COMMENTER TOKEN (STRING)",
    "name": "COMMENTER USERNAME (STRING)",
    "comment": "COMMENT CONTENT (STRING)"
}
```

### /friend/:username
Befriend someone\

#### Request body
```json
{
    "token": "COMMENTER TOKEN (STRING)",
    "friend": "USERNAME OF FRIEND (STRING)"
}
```