

class Client{
    constructor(name, socket){
        this.name = name;
        this.socket = socket;
    }

    addFriend(user){
        friends.push(user);
    }
}

exports.Client = Client;