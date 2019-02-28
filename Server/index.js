const express = require('express');
const http = require('http');
const app = express();

const httpServer = http.Server(app);

const router = require('./controllers/rest.controller');
app.use(express.json());

app.use("/", router);

const socketSetup = require('./controllers/socket.controller');
socketSetup(httpServer);

httpServer.listen(80, () => {
    console.log("Server running");
});

require('./models/ORM');

