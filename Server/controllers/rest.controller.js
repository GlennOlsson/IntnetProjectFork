const express = require('express');
const http = require('http');
const ORMModels = require('../models/ORM');
const Chatroom = require('../models/Chatroom');
const Message = require('../models/Message');

const router = express.Router();
exports.router = router

router.get("/rooms", (res, req) => {
    let returnRooms = []
    ORMModels.ChatRoom.findAll().then((rooms) => {
        rooms.forEach(room => {
            let id = room.id;
            let name = room.name;
            let thisRoom = {
                name: name,
                usercount: -1, //TODO, probalby replace with list of users
                id: id
            }
            returnRooms.push(thisRoom);
        });
        req.json(returnRooms);
    })
})

router.get("/room/:id", (res, req) => {
    let returnMessages = []
    let chatID = res.params.id;
    ORMModels.Messages.findAll({
        where: {
            chatid: chatID
        }
    }).then((messages) => {
        messages.forEach(message => {
            let uID = message.userid;
            let content = message.message;
            let date = message.date;
            let thisMessage = {
                message: content, 
                sentby: uID,
                sent: date
            }
            returnMessages.push(thisMessage);
        })
        req.json({
            messages: returnMessages
        })
    } )
})

router.get("/profile/:username", (res, req) => {
    let name = res.params.username;
    console.log(name);
    ORMModels.Account.findOne({
        where: {
            username: name
        },
        include: [
            {
                model: ORMModels.Friends,
                as: 'u1'
            },
            {
                model: ORMModels.Friends,
                as: 'u2'
            },
            {
                model: ORMModels.ProfileComment,
                as: 'uto'
            },
            ORMModels.Profile
        ]
    }).then(account => {
        let profile = account.profile;
        console.log(Object.keys(account))
        let created = profile.date_created;
        let picture = profile.profilepic;
        let bio = profile.bio;

        //Only bring the friend id
        let u1 = account.u1.filter(fs => fs.user2 != name).map(fs => fs.user2);
        let u2 = account.u2.filter(fs => fs.user1 != name).map(fs => fs.user1);
        let friends = u1.concat(u2);
        let comments = account.uto.map(comment => {
            return {
                by: comment.userfrom,
                comment: comment.comment,
                date: comment.date
            }
        });
        req.json({
            created,
            picture,
            bio,
            friends,
            comments
        })
    }).catch(er => console.log(er))
})

router.post("/login", (res, req) => {
    //TODO
})

router.post("/comment/:username", (res, req) => {
    //TODO
})

const app = express();
app.use(express.json());
app.use("/", router);

const httpServer = http.Server(app);

httpServer.listen(80, () => {
    console.log("Server running");
})