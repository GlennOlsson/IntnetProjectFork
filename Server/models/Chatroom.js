
export default class Chatroom{
    constructor(id, name){
        this.id = id;
        this.name = name;
        this.users = [];
        this.messages = [];
    }

    addMessage(message){
        this.messages.push(message);
    }

    addUser(user){
        this.users.push(user);
    }

    removeUser(userObject){
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