const WebSocket = require('ws');
const ws = new WebSocket('ws://localhost:8080');

ws.on('open', function open() {
  console.log('Connected');
  ws.send(JSON.stringify({ type: "status", id: "TestClient", status: "Running Macro" }));
});
