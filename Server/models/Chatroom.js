
class Chatroom{
    constructor(id, name){
        this.id = id;
        this.name = name;
        this.users = [];
        this.messages = [];

        this.addMessage = function(message){
            this.messages.push(message);
        }

        this.addUser = function(user){
            this.users.push(user);
        }

        this.removeUser = function(userObject){
            index = 0;
            for(user of users){
                if(user.id == userObject.id){
                    users.splice(index, 1);
                    break;
                }
                index++;
            }
        }
    }
}

module.exports = Chatroom