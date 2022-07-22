# Getting Started

### Technical Details
Use IMDB Dataset in order to develop these functionalities using Java 8+ and expose proper RESTful HTTP APIs :
1. Import the dataset into the application
2. Return all the titles in which both director and writer are the same person and he/she
   is still alive
3. Get two actors and return all the titles in which both of them played at
4. Get a genre from the user and return best selling titles on each year for that genre
5. Count how many HTTP requests you received in this application since the last
   Startup.


### Considerations

1. You Don’t have to use any external database application but if you want to just use
them in in-memory mode, your application should run standalone
2. Do not implement any UI

You can find the dataset and its explanation here: https://www.imdb.com/interfaces Let us know if you have any questions and good luck!

### How To check:
After running the application, an embedded web container will launch, and we will have 3 endpoints:
1. http://localhost:8080/imdb/titles-with-same-alive-director-writer?page=1
2. http://localhost:8080/imdb/titles-with-actors?actor1=Robert%20De%20Niro&actor2=Al%20Pacino
3. http://localhost:8080/imdb/stat
4. Number 4 in Technical Section is not implemented