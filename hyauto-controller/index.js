const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");

const app = express();
const PORT = 3000;

app.use(cors());
app.use(bodyParser.json());

// In-memory status storage
const clients = {};

// Endpoint to receive metrics from Minecraft clients
app.post("/api/update", (req, res) => {
    const { uuid, status, tps, ram, macro } = req.body;

    if (!uuid) return res.status(400).send("Missing UUID");

    clients[uuid] = {
        status,
        tps,
        ram,
        macro,
        lastUpdate: Date.now()
    };

    res.send("OK");
});

// Endpoint to retrieve all client statuses
app.get("/api/status", (req, res) => {
    res.json(clients);
});

app.listen(PORT, () => {
    console.log(`Server running at http://localhost:${PORT}`);
});
