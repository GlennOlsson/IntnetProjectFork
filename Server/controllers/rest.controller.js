const express = require('express');
const http = require('http');

const router = express.Router();
exports.router = router

router.get("/rooms", (res, req) => {
    //TODO
    req.json({
        hej: "bajs"
    })
})

router.get("/room/:id", (res, req) => {
    //TODO
})

router.get("/profile/:username", (res, req) => {
    //TODO
})

router.post("/login", (res, req) => {
    //TODO
})

router.post("/comment/:username", (res, req) => {
    //TODO
})

const app = express(); 
app.use(express.json());
app.use("/", router);

const httpServer = http.Server(app);

httpServer.listen(80, () => {
    console.log("Server running");
})