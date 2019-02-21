

export default class Message {
    constructor(chatid, userid, content, date){
        this.chat = chatid;
        this.user = userid;
        this.content = content;
        this.date = date;
    }
}