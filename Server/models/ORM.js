const Sequelize = require('sequelize');

console.log("SEQ");

const sequelize = new Sequelize({
    host: 'localhost',
    username: 'intnet',
    password: 'inget',
	database: 'intnetproj',
    dialect: 'postgres',
    logging: false
});

const ChatRoom = sequelize.define("chatrooms", {
    id: {
        type: Sequelize.INTEGER,
        autoIncrement: true,
        primaryKey: true
    },
    name: Sequelize.STRING
});

const Account = sequelize.define("accounts", {
    username: {
        type: Sequelize.STRING,
        primaryKey: true
    },
    passhash: Sequelize.STRING
});

const Messages = sequelize.define('messages', {
    chatid: {
        type: Sequelize.INTEGER,
        references: {
            model: ChatRoom,
            key: 'id',            
        }
    },
    userid: {
        type: Sequelize.STRING,
        references: {
            model: Account,
            key: 'username',            
        }
    },
    message: Sequelize.STRING,
    date: Sequelize.STRING
});

const ProfileComment = sequelize.define('profilecomments', {
    userto: {
        type: Sequelize.STRING,
        references: {
            model: Account,
            key: 'username',            
        }
    },
    userfrom: {
        type: Sequelize.STRING,
        references: {
            model: Account,
            key: 'username',            
        }
    },
    comment: Sequelize.STRING,
    date: Sequelize.STRING
});

const Profile = sequelize.define('profiles', {
    user: {
        type: Sequelize.STRING,
        references: {
            model: Account,
            key: 'username',            
        }
    },
    profilepic: Sequelize.TEXT,
    bio: Sequelize.STRING,
    date_created: Sequelize.STRING
});

const Friends = sequelize.define('friends', {
    user1: {
        type: Sequelize.STRING,
        references: {
            model: Account,
            key: 'username',            
        }
    },
    user2: {
        type: Sequelize.STRING,
        references: {
            model: Account,
            key: 'username',            
        }
    },
});

const AccountTokens = sequelize.define('accounttokens', {
    user: {
        type: Sequelize.STRING,
        references: {
            model: Account,
            key: 'username'
        }
    },
    token: Sequelize.STRING
})

Account.hasMany(Messages, {foreignKey: "userid"});
Messages.belongsTo(Account, {foreignKey: "userid"});

ChatRoom.hasMany(Messages, {foreignKey: "chatid"});
Messages.belongsTo(ChatRoom, {foreignKey: "chatid"});

Account.hasMany(ProfileComment, {
    foreignKey: "userto",
    as: 'uto'
});

ProfileComment.belongsTo(Account, {
    foreignKey: "userto",
    as:'uto'
});
Account.hasMany(ProfileComment, {
    foreignKey: "userfrom",
    as:'ufrom'
});
ProfileComment.belongsTo(Account, {
    foreignKey: "userfrom",
    as:'ufrom'
});

Account.hasOne(Profile, {foreignKey: "user"});
Profile.belongsTo(Account, {foreignKey: "user"});

Account.hasMany(Friends, {
    as: 'u1',
    foreignKey: "user1"
});
Friends.belongsTo(Account, {
    as: 'u1',
    foreignKey: "user1"
});
Account.hasMany(Friends, {
    as: 'u2',
    foreignKey: "user2"
});
Friends.belongsTo(Account, {
    as:'u2',
    foreignKey: "user2"
});

Account.hasMany(AccountTokens, {foreignKey: "user"});
AccountTokens.belongsTo(Account, {foreignKey: "user"});

sequelize.sync({force: true}).then(() => createSample());

function sampleAccount(){
    //Passwords are hunter2
    return Account.bulkCreate([
        {
            username: "glennol",
            passhash: "$2b$10$Vks4DREylBTTeXPNmm8XjeyimmEOeCsnr.fTUSsLJ7SgsvBpmyjuC"
        },
        {
            username: "oscarekh",
            passhash: "$2b$10$BvgF4QiOoW8Nt4QOcqHv4uuaRg8VNRd4PV0ZP8gfhEoHQT4xIzmHO"
        },
        {
            username: "linusri",
            passhash: "$2b$10$2GsI4zTnJ.2jvpqY21oQ9uGsl4JK6JEnigdCHME5O6ZkPB1K4cQLe"
        },
        {
            username: "frnorlin",
            passhash: "$2b$10$627nicg0Zi3Fr0YpdSf9s.ceJTN8b2/YpyF7TOMmrP.lXealDtrsC"
        },
    ]);
}

function sampleChatRooms(){
    return ChatRoom.bulkCreate([
        {
            name: "Tugget",
        },
        {
            name: "Snacket"
        },
        {
            name: "Fredagsmys"
        }
    ]);
}

function sampleRest(){
    Messages.bulkCreate([
        {
            chatid: 1,
            userid: 'glennol',
            message: "Hejsan!",
            date: "2019-01-02;12:08",
        },
        {
            chatid: 1,
            userid: "oscarekh",
            message: "Hej!",
            date: "2019-01-02;12:09",
        },
        {
            chatid: 1,
            userid: "glennol",
            message: "Hur gammal är du?",
            date: "2019-01-02;12:10",
        },
        {
            chatid: 1,
            userid: "oscarekh",
            message: "Jag är 200 år gammal",
            date: "2019-01-02;12:15",
        },
        {
            chatid: 1,
            userid: "glennol",
            message: "Ojdå det var gammalt",
            date: "2019-01-02;12:16",
        }
    ])

    Friends.bulkCreate([
        {
            user1: "glennol",
            user2: "oscarekh"
        },
        {
            user1: "oscarekh",
            user2: "linusri"
        },
        {
            user1: "frnorlin",
            user2: "glennol"
        },
        {
            user1: "frnorlin",
            user2: "oscarekh"
        }        
    ])

    Profile.bulkCreate([
        {
            user: "glennol",
            bio: "The best Javascript and NodeJS developer the world has ever seen. Bit of a douche though",
            date_created: "2019-01-01;13:37",
            profilepic: "image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAIAAAACCAYAAABytg0kAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAABWWlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iWE1QIENvcmUgNS40LjAiPgogICA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPgogICAgICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgICAgICAgICB4bWxuczp0aWZmPSJodHRwOi8vbnMuYWRvYmUuY29tL3RpZmYvMS4wLyI+CiAgICAgICAgIDx0aWZmOk9yaWVudGF0aW9uPjE8L3RpZmY6T3JpZW50YXRpb24+CiAgICAgIDwvcmRmOkRlc2NyaXB0aW9uPgogICA8L3JkZjpSREY+CjwveDp4bXBtZXRhPgpMwidZAAAAFElEQVQIHWP4DwQMDAz/mYAEGAAAR9wE/rOv0uoAAAAASUVORK5CYII="
        },
        {
            user: "oscarekh",
            bio: "A master of Android development",
            date_created: "2019-01-01;13:38",
            profilepic: "iVBORw0KGgoAAAANSUhEUgAAAAIAAAACCAYAAABytg0kAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAABWWlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iWE1QIENvcmUgNS40LjAiPgogICA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPgogICAgICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgICAgICAgICB4bWxuczp0aWZmPSJodHRwOi8vbnMuYWRvYmUuY29tL3RpZmYvMS4wLyI+CiAgICAgICAgIDx0aWZmOk9yaWVudGF0aW9uPjE8L3RpZmY6T3JpZW50YXRpb24+CiAgICAgIDwvcmRmOkRlc2NyaXB0aW9uPgogICA8L3JkZjpSREY+CjwveDp4bXBtZXRhPgpMwidZAAAAE0lEQVQIHWNkYGD4DwQMTCACBABG7wb9gUd90AAAAABJRU5ErkJggg=="
        },
        {
            user: "linusri",
            bio: "Pleb",
            date_created: "2019-02-22;12:08",
            profilepic: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAIAAAACCAYAAABytg0kAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAABWWlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iWE1QIENvcmUgNS40LjAiPgogICA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPgogICAgICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgICAgICAgICB4bWxuczp0aWZmPSJodHRwOi8vbnMuYWRvYmUuY29tL3RpZmYvMS4wLyI+CiAgICAgICAgIDx0aWZmOk9yaWVudGF0aW9uPjE8L3RpZmY6T3JpZW50YXRpb24+CiAgICAgIDwvcmRmOkRlc2NyaXB0aW9uPgogICA8L3JkZjpSREY+CjwveDp4bXBtZXRhPgpMwidZAAAAFUlEQVQIHWP4DwQMDAz/mYAEA4gNAFDTB/ueUw4AAAAAAElFTkSuQmCC"
        },
        {
            user: "frnorlin",
            bio: "From Norrland",
            date_created: "2019-02-22;04:30",
            profilepic: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAIAAAACCAYAAABytg0kAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAABWWlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iWE1QIENvcmUgNS40LjAiPgogICA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPgogICAgICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgICAgICAgICB4bWxuczp0aWZmPSJodHRwOi8vbnMuYWRvYmUuY29tL3RpZmYvMS4wLyI+CiAgICAgICAgIDx0aWZmOk9yaWVudGF0aW9uPjE8L3RpZmY6T3JpZW50YXRpb24+CiAgICAgIDwvcmRmOkRlc2NyaXB0aW9uPgogICA8L3JkZjpSREY+CjwveDp4bXBtZXRhPgpMwidZAAAAFUlEQVQIHWNkYGD4D8QMLP//g2kGACMlBAIW3cmQAAAAAElFTkSuQmCC"
        }
    ]);

    ProfileComment.bulkCreate([
        {
            userto: "glennol",
            userfrom: "linusri",
            comment: "I wish I was just like you",
            date: "2019-02-21;23:59"
        },
        {
            userto: "glennol",
            userfrom: "frnorlin",
            comment: "This project is so much cooler than ours",
            date: "2019-02-22;12:22"
        },
        {
            userto: "oscarekh",
            userfrom: "frnorlin",
            comment: "Wow this app sure is something! Well done",
            date: "2019-02-21;13:02"
        },
        {
            userto: "frnorlin",
            userfrom: "linusri",
            comment: "Maybe we should just wash our app down the drain?",
            date: "2019-02-22;08:46"
        },
        {
            userto: "linusri",
            userfrom: "linusri",
            comment: "You are the only one more dumb than me",
            date: "2019-02-24;08:18"
        },
    ]);

    AccountTokens.create({
        user: 'glennol',
        token: "testtoken"
    })
}


function createSample(){
    sampleAccount()
        .then(() => 
            sampleChatRooms()
            .then(() => sampleRest()));
    

    console.log("Created");
}


function padd(num){
    return num.toString().length == 1 ? "0" + num : num;
}

function getCurrentTime(){
    let now = new Date();
    let YY = padd(now.getFullYear())
    let MM = padd(now.getMonth());
    let DD = padd(now.getDay());
    let HH = padd(now.getHours());
    let mm = padd(now.getMinutes());
    let date = YY + "-" + MM + "-" + DD  + ";" + HH + ":" + mm;

    return date;
}

exports.sequelize = sequelize;
exports.Account = Account;
exports.ChatRoom = ChatRoom;
exports.Messages = Messages;
exports.Friends = Friends;
exports.Profile = Profile;
exports.ProfileComment = ProfileComment;
exports.AccountTokens = AccountTokens;
exports.getCurrentTime = getCurrentTime;
