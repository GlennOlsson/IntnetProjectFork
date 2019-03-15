
/**
 * A client object holding a socket. The socket contains informaiton about the client
 */
class Client{
    constructor(socket){
        this.socket = socket;

        this.getSocket = () => {
            return this.socket;
        }

        this.getName = () => {
            return this.socket.username;
        }

        this.getChat = () => {
            return this.socket.chatid;
        }
    }
}

module.exports = Client;