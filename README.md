# CentenarGO

Aici o sa punem descrierea sau ceva :))

## API server

To run the server:
```
cd back
npm install
node server.js
```

Postgres configs can be found in `config.js`.

Testing the API:
* all `/api` pages return status code 200 on success
* all `/api` pages except `/api/login` and `/api/signup` expect an authorization token and will return 403 otherwise
* `/api/login` expects `email` and `password`
* `/api/signup` expects `username`, `email` and `password`, returns 400 if the email is already in use