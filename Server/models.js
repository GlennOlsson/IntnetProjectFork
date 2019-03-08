const Client = require('./models/Client');

let clients = [];

exports.newClient = (socket) =>  {
    clients.push(new Client(socket));
}

exports.removeClient = (username) => {
    for(clientIndex in clients){
        let client = clients[clientIndex];
        if(client.getUsername() == username){
            clients.splice(clientIndex, 1);
            break;
        }
    }
}

exports.getClientsOfChat = (chatid) => {
    let returnArray = [];
    for(client of clients){
        if(client.getChat() == chatid){
            returnArray.push(client.getUsername);
        }
    }

    return returnArray;
}

exports.getClient = (username) => {
    for(client of clients){
        if(client.getUsername() == username){
            return client;
        }
    }
}

