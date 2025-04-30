const express = require('express');
const app = express();
const PORT = 3000;

app.use(express.json());

app.post('/api/update', (req, res) => {
  const { uuid, status, tps, ram, macro } = req.body;
  console.log(`[${uuid}] Status: ${status}, TPS: ${tps}, RAM: ${ram}, Macro: ${macro}`);
  res.sendStatus(200);
});

app.listen(PORT, () => {
  console.log(`Server listening on http://localhost:${PORT}`);
});
