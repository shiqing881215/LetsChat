Version 0
Three basic classes Client, Server, ServerThread.
Start the server first, then start client. Send the text to server and get automative reply from server as the same message
add some socket information.
ServerSocket extends thread to help server deal with multiple users.

Version 1
Add login page.
Use the same frame as client UI. Create two panel, change frame's content fram when login.
Add username for each login user.
LoginPage class is not used.

Version 2
Add user list component.
Each user can view all login user on the server.
The user list can update on the fly. 
You can set the update frequency at the ClientUpdateUserlistThread class. Default is 5 seconds.

Version 3
Complete the user list component.
Whenever new user login or login user logout, all clients user list will update correctly on the fly.
Complete login and register function. Auxiliary class is SecurityUtil. Using pwd.txt to hold all user name and pwd. Pwd uses hashcode. 
Tricks:
1. To delete the logout socket. Not know whether the socket in client and server seems to be the different sockets. 
   So to identify the socket, use the port attribute. In client, using socket.getLocalPort(); in server, using socket.getPort().
2. In client, frame add window listener. When close the window, close the socket and send the logout infor with port to server.
3. Protocol add situation to deal with logout, return 2.
4. Remove the add user when user login action from server to serverClient, which makes more sense. 
   When get logout infor, remove the user bound with specified port number.
5. Add user class, to hold the socket and user name infor together.
---------TO DO LIST:
1. User input validation.
2. Some situation, eg. same user login twice.
3. Add private chat function


Patch Version:
Spend too much time on the checking the logging user.
Try to use the singleton pattern, however, it involves a lot of issues about synchronization.
Cannot still set it down. 
ActiveUserPool and LoggingUserUtil is the file to implement this feature. Need to take a serious consideration of the multi-thread scenario.
Original checking update user list may also have some problem. Need to verify in the future. 
Skip this step now, may come back in the future.  04/01/2013


Version 4
Version 4.1
1.Add ProtocolEnum and SecurityEnum class. Avoid using any magic number.
2.Add PrivateChatFrame and PrivateChatThread
  PrivateChatFrame is used to show the person-to-person chat window.
  PrivateChatThread is used to run PrivateChatFrame.
  Not done yet, just some basic function. Need to improve.
Version 4.2
Finish basic private chat function. One user start the private chat window, the other user start the same window, so they can private chat.
Server handle the private chat message and forward to the target user.
---------TO DO LIST:
1. Add a private chat remind window, so no need to start a window by user himself and make more sense.
2. Change the original "stupid" server reply to the group chat function.
3. Bug --- enter some user which doesn't exist. 
   Seems that the problem is ServerThread can get the correct clientsPool but client can never get it.
   
   
 
Version 5
Version 5.1
Finish the group chatting function. 
When a user post something on the group chat box. All other users will see it.
Core method : notifyGroupMessage --- get all other users' PrintWriter
TODO:
Add remind box to show the private chat invitation
Version 5.2
Finish BASIC private chat remind box.
When a user click start private chat button. His own private chat window and also the target user private chat window will show at the same time.
Still remain some disgusting bugs. Most of them are validating bugs.
All basic function done here. The remaining work is focused on the bugs and the code refactoring.



Version 6
Bug fix and code refactor
1. Validate same user login twice
2. Validate private chat with a non-existing user
3. Cannot receive private chat message
4. Cannot start private chat window for the target user

1. Done --- Add protocol, check it from server side instead of client side.
2. Done --- Similar startegy as above, check whether the private chat target user is online

Leave 3.4 here. All basic function implemented. Congratulation to myself for this simple chat program!!!!!
Simple but also need some effort and time.
Prepare to put it on the Github. Yeah!!! Open source, though it's a little simple and defected compared with others.

If have time will come back for these:
1. Small bugs fixed
2. Code refactor (1) code structure 
                 (2) util (remove some code duplicate and improce code reuse, eg error panel)
                 (3) Code performance
3. Try some new (1) New UI instead of Swing (Needs too many lines)
                (2) New feature. Just compared to QQ (log, transfer files ....... need DB)
                (3) Start method --- most simple way jar start, issue is cannot get the labels, it seems the problem of reading text file













