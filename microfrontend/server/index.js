const express = require("express");
const proxy = require('express-http-proxy');
const url = require('url');
const app = express();
const port = 8080;


app.use('/pmo', proxy('http://localhost:3001', {
  proxyReqPathResolver: function (req) {
    return "/pmo/" + url.parse(req.url).path;
  }
}));

app.use('/client', proxy('http://localhost:3002', {
  proxyReqPathResolver: function (req) {
    return "/client/" + url.parse(req.url).path;
  }
}));

app.use('/', proxy('http://localhost:3000'));
app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`);
});