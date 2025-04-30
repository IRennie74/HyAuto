const WebSocket = require('ws');
const { handleMessage } = require('./messages');
const { registerClient, removeClient } = require('./clientRegistry');

const wss = new WebSocket.Server({ port: 8080 });

wss.on('connection', function connection(ws, req) {
  const ip = req.socket.remoteAddress;
  console.log(`[CONNECT] ${ip}`);

  registerClient(ws);

  ws.on('message', function incoming(message) {
    try {
      const data = JSON.parse(message);
      handleMessage(ws, data);
    } catch (e) {
      console.error(`[ERROR] Invalid JSON from ${ip}:`, message);
    }
  });

  ws.on('close', () => {
    console.log(`[DISCONNECT] ${ip}`);
    removeClient(ws);
  });
});

console.log("[HyAuto Server] Listening on ws://localhost:8080");
