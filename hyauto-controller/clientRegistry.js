//Track active accounts
const clients = new Map();  // Map<ws, clientData>

function registerClient(ws) {
  clients.set(ws, {
    id: null,        // Assigned when client identifies
    lastUpdate: Date.now(),
    status: "unknown"
  });
}

function removeClient(ws) {
  clients.delete(ws);
}

function updateClient(ws, data) {
  if (clients.has(ws)) {
    const info = clients.get(ws);
    info.lastUpdate = Date.now();
    info.status = data.status || info.status;
    info.id = data.id || info.id;
  }
}

function getAllClients() {
  return Array.from(clients.values());
}

module.exports = {
  registerClient,
  removeClient,
  updateClient,
  getAllClients
};
