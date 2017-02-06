## Synopsis

Web application (play framework based) for Akka actors and websockets features demonstration.
  
## Motivation  

The goal is to show the basic Scala technologies for future game project. In akka actors based application all is actor and application is set of actors that send messages to each other. And we can also have the actor that sends messages using  websockets channel. The advantages of using actors are well-known. Application is multi-threading by design (one actor = one thread), easily scales, reactive (non blocking), etc.

## Running

To start this web app do commands as follows:

$ sbt    
    
[game] $ run

Then go to http://localhost:9000 in browser

 
