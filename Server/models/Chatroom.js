
class Chatroom{
    constructor(id){
        this.id = id;
        this.users = [];

        this.addUser = (username) => {
            this.users.push(username);
        }

        this.removeUser = (username) => {
            
        }
    }
}

module.exports = Chatroom