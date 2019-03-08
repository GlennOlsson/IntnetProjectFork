const express = require('express');
const http = require('http');
const app = express();

const httpServer = http.Server(app);

const socketSetup = require('./controllers/socket.controller');
socketSetup(httpServer);

const router = require('./controllers/rest.controller');
app.use(express.json());

app.use("/", router);

httpServer.listen(8082, () => {
    console.log("Server running");
});

require('./models/ORM');

