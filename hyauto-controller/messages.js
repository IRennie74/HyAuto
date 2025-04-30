//Message routing
const { updateClient } = require('./clientRegistry');

function handleMessage(ws, data) {
  switch (data.type) {
    case "status":
      updateClient(ws, data);
      console.log(`[STATUS] ${data.id}: ${data.status}`);
      break;

    case "metrics":
      console.log(`[METRICS] ${data.id} - RAM: ${data.ramMB}MB | TPS: ${data.tps}`);
      break;

    default:
      console.warn(`[UNKNOWN TYPE]`, data);
  }
}

module.exports = { handleMessage };
