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

