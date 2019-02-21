const Sequelize = require('sequelize');

const sequelize = new Sequelize({
    host: 'localhost',
	username: 'glenn',
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
    profilepic: Sequelize.STRING,
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

Account.hasMany(Messages, {foreignKey: "userid"});
Messages.belongsTo(Account, {foreignKey: "userid"});

ChatRoom.hasMany(Messages, {foreignKey: "chatid"});
Messages.belongsTo(ChatRoom, {foreignKey: "chatid"});

Account.hasMany(ProfileComment, {foreignKey: "userto"});
ProfileComment.belongsTo(Account, {foreignKey: "userto"});
Account.hasMany(ProfileComment, {foreignKey: "userfrom"});
ProfileComment.belongsTo(Account, {foreignKey: "userfrom"});

Account.hasOne(Profile, {foreignKey: "user"});
Profile.belongsTo(Account, {foreignKey: "user"});

Account.hasMany(Friends, {foreignKey: "user1"});
Friends.belongsTo(Account, {foreignKey: "user1"});
Account.hasMany(Friends, {foreignKey: "user2"});
Friends.belongsTo(Account, {foreignKey: "user2"});

sequelize.sync({force: true}).then(() => createSample());

function sampleAccount(){
    return Account.bulkCreate([
        {
            username: "glennol",
            passhash: "hunter2"
        },
        {
            username: "oscarekh",
            passhash: "hunter2"
        },
        {
            username: "linusri",
            passhash: "hunter2"
        },
        {
            username: "frnorlin",
            passhash: "hunter2"
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
            profilepic: "",
        },
        {
            user: "oscarekh",
            bio: "A master of Android development",
            date_created: "2019-01-01;13:38",
            profilepic: ""
        },
        {
            user: "linusri",
            bio: "Pleb",
            date_created: "2019-02-22;12:08",
            profilepic: ""
        },
        {
            user: "frnorlin",
            bio: "From Norrland",
            date_created: "2019-02-22;04:30",
            profilepic: ""
        }
    ]);

    ProfileComment.bulkCreate([
        {
            useto: "glennol",
            userfrom: "linusri",
            comment: "I wish I was just like you",
            date: "2019-02-21;23:59"
        },
        {
            useto: "glennol",
            userfrom: "frnorlin",
            comment: "This project is so much cooler than ours",
            date: "2019-02-22;12:22"
        },
        {
            useto: "oscarekh",
            userfrom: "frnorlin",
            comment: "Wow this app sure is something! Well done",
            date: "2019-02-21;13:02"
        },
        {
            usetor: "frnorlin",
            userfrom: "linusri",
            comment: "Maybe we should just wash our app down the drain?",
            date: "2019-02-22;08:46"
        },
        {
            useto: "linusri",
            userfrom: "linusri",
            comment: "You are the only one more dumb than me",
            date: "2019-02-24;08:18"
        },
    ]);
}


function createSample(){
    sampleAccount()
        .then(() => 
            sampleChatRooms()
            .then(() => sampleRest()));
    

    console.log("Created");
}
