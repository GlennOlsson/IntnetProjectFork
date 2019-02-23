const express = require('express');
const http = require('http');
const ORMModels = require('../models/ORM');
const Chatroom = require('../models/Chatroom');
const Message = require('../models/Message');

const router = express.Router();
exports.router = router

router.get("/rooms", (req, res) => {
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
        res.json(returnRooms);
    })
})

router.get("/room/:id", (req, res) => {
    let returnMessages = []
    let chatID = req.params.id;
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
        res.json({
            messages: returnMessages
        })
    } )
})

router.get("/profile/:username", (req, res) => {
    let name = req.params.username;
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
        res.json({
            created,
            picture,
            bio,
            friends,
            comments
        })
    }).catch(er => console.log(er))
})

router.post("/login", (req, res) => {
    let json = req.body;
    let name = json.name;
    let pass = json.password;
    let hashedPass = hash(pass);
    ORMModels.Account.findOne({
        where:{
            username: name,
            passhash: hashedPass
        }
    }).then(acc => {
        if(!acc){
            res.json({success: false});
            return;
        }
        
        let token = generateToken();
        ORMModels.AccountTokens.create({
            user: name,
            token
        }).then(() => {
            res.json({
                success: true,
                token
            })
        })
    })
})

router.post("/comment/:username", (req, res) => {
    //TODO
})

//TODO: HASH PASSWORD
function hash(pass) {
    return pass;
}

const abc = "abcdefghijklmnopqrstuvwxyz1234567890"

function randomNumb(min, max){
    return Math.floor((Math.random()*max) + min);
}

function generateToken(){
    //Random length between 50 and 100 chars
    let length = randomNumb(50, 100);
    let token = "";
    for(let i = 0; i < length; i++){
        //Pick a random letter of abc
        let index = randomNumb(0, abc.length);
        let appender = abc[index];
        //50% should be uppercase (avg)
        if(randomNumb(1,2) == 1){
            appender = appender.toUpperCase();
        }
        token += appender;
    }
    return token;
}

const app = express();
app.use(express.json());
app.use("/", router);

const httpServer = http.Server(app);

httpServer.listen(80, () => {
    console.log("Server running");
})