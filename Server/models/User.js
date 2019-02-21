

export default class User{
    constructor(name, bio, dateCreated, profilePic){
        this.name = name;
        this.bio = bio;
        this.dateCreated = dateCreated;
        this.profilePic = profilePic;
        this.friends = [];
    }
}