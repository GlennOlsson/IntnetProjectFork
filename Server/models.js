const Client = require('./models/Client');

let clients = [];

/**
 * A new client is connected
 */
exports.newClient = (socket) =>  {
    console.log("New client " + socket.username);
    clients = clients.filter(cli => {
        return cli.getName() != socket.username
    });
    clients.push(new Client(socket));
}

/**
 * A client is disconnected
 */
exports.removeClient = (username) => {
    for(clientIndex in clients){
        let client = clients[clientIndex];
        if(client.getName() == username){
            clients.splice(clientIndex, 1);
            console.log("Removing client " + username);
            break;
        }
    }
}

/**
 * The the client names in a chat of a specified chatid
 */
exports.getClientsOfChat = (chatid) => {
    let returnArray = [];
    for(client of clients){
        console.log("id", chatid, " ", client.getChat())
        if(client.getChat() == chatid){
            returnArray.push(client.getName());
        }
    }

    console.log("clients in: ", returnArray);

    return returnArray;
}

/**
 * Get the client object of a username
 */
exports.getClient = (username) => {
    for(client of clients){
        console.log(client.getName() + ", " + username);
        if(client.getName() == username){
            return client;
        }
    }
}

/**
 * Send notifications to a client
 */
exports.sendNotification = (message, username) => {
    let client = exports.getClient(username);
    let socket = client.getSocket();
    socket.emit("notification", {
        message
    });
}