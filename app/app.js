'use strict';
const express = require('express');
const bodyParser = require('body-parser');
const http = require('request');

const PORT = 9091;
const HOST = '0.0.0.0';
const API_BASE = 'http://localhost:9090';

const app = express();
app.set('view engine', 'pug')
app.use(bodyParser.urlencoded({ extended: true }));

app.get('/', (request, response) => {
  response.redirect('/endpoint');
})

app.get('/endpoint', (req, res) => {
  res.render('endpoint', {
    username: '',
    error: false
  });
});

app.post('/endpoint', (req, res) => {
  const username = req.body.username || 'guest';
  const number = req.body.weight || 1;

  http.get({
    url: `${API_BASE}/endpoint/${number}`,
    headers: {
      'X-Auth-User': username
    }
  }, function(e, r, body) {
    res.render('endpoint', {
      error: r.statusCode !== 200,
      response: r.body,
      username,
      number
    });
  });
});

app.listen(PORT, HOST, () => {
  console.log(`Running on http://${HOST}:${PORT}`);
});