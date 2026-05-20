var express = require('express');
var app = express();
var path=require('path');

app.use(express.static(__dirname + '/src/main/resources'))

app.get( '/', ( req, res ) => {
    res.sendFile( path.join( __dirname, 'index.html' ));
  });

app.listen(8080, () => console.log('server started at http://localhost:8080'))