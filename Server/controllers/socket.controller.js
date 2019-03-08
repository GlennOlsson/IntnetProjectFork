const ORMModels = require('../models/ORM');
const models = require('../models');

function setup(httpServer){

    const io = require('socket.io').listen(httpServer);
    io.on('connection', socket => {
        socketController(socket, io);
    });
    console.log("Setup socket")
}

function socketController(socket, io){
    console.log("New connection");

    socket.on("init", req => {
        console.log("Init: ", req);
        let json = JSON.parse(req);
        let user = json.username;
        models.newClient(socket);

        socket.username = user;
    });

    socket.on("message", req => {
        console.log("Message: ", req);
        let json = JSON.parse(req);
        let chat = socket.chatid;
        let user = socket.username;
        let content = json.content;
        io.to(chat).emit("message", json);

        let date = ORMModels.getCurrentTime();

        ORMModels.Messages.create({
            chatid: chat,
            userid: user,
            message: content,
            date: date
        });        
    });

    socket.on("join", req => {
        let json = JSON.parse(req);
        let chatid = json.chatid;
        let user = socket.username;
        console.log("Joined room ", user, chatid);
        socket.chatid = chatid;
        socket.join(chatid, () => {
            io.to(chatid).emit("join", {username: user});
        });
    });

    socket.on("leave", req => {
        console.log("Leave", req);
        let user = socket.username;
        let chatid = socket.chatid;
        socket.chatid = undefined;
        socket.leave(chatid, () => {
            io.to(chatid).emit("leave", {
                username: user,
                reason: "User left"
            });
        });
    });

    socket.on("disconnect", req => {
        console.log("Disconnect", req);
        let user = socket.username;
        let chatid = socket.chatid;
        io.to(chatid).emit("leave", {
            username: user,
            reason: "Timed out"
		});
		models.removeClient(user);
    });
}

module.exports = setup;