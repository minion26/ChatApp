# Chat Application

1. The ``Main.java`` handles the options that a user that wants to use the *Chat Application* have. It can :
   1. initiate a conversation
   2. participate in a conversation that has already started
      1. if they insert a PORT that never exists, they can try again
      2. when a 3rd (4th, etc.) client connects, his appearance will be announced by a message to the other clients that participate in that conversation
   3. exit the program
2. The ``Server.java`` handles the server side of the application. It can :
   1. accept multiple clients
   2. has an inner class ``ClientHandler`` that handles the communication between the server and the clients
      1. sends messages to the clients
      2. receives messages from the clients
      3. closes the connection with the clients when they disconnect
3. The ``Client.java`` handles the client side of the application. It can :
    1. send messages to the server
    2. receives messages from the server
    3. closes the connection with the server
