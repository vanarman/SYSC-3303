STUDENT NAME:	
STUDENT NUMBER:	

ASSIGNMENT 1

RUN INSTRUCTIONS:

	1. RUN -> Server
	2. RUN -> Host
	3. RUN -> Client
	!NOTE: You can run files in any sequence because client contain a delay before send first packet.


CLASS DESCRIPTION:

	Client.java
		- Forms packet:
			* byte[0] 			= 0
			* byte[1] 			= 1 or 2 	// 1 - read; 2 - write
			* byte[2..n]		= message	// string in bytes
			* byte[n+1]			= 0 		// separator
			* byte[n+2..m]	 	= mode 		// string in bytes
			* byte[m+1]			= 0			// separator 
		- Send packet to Server through Host
		- Wait for response from Server

	Host.java
		- Wait for the packet from Client
		- Receive packet form Client
		- Send packet to Server
		- Wait for response from Server
		- Receive response from Server
		- Send response to Client

	Server.java
		- Wait for the packet from Host
		- Validate packet that has to follow next pattern:
			* byte[0] 			= 0
			* byte[1] 			= 1 or 2 	// 1 - read; 2 - write
			* byte[2..n]		= message	// string in bytes
			* byte[n+1]			= 0 		// separator
			* byte[n+2..m]	 	= mode 		// string in bytes
			* byte[m+1]			= 0			// separator 
		- If packet is valid:
			* Generate response:
				# [0 3 0 1] for read request	// byte[2] = 1
				# [0 4 0 0] for write request	// byte[2] = 2
			* Send response to the Host
		- If packet is NOT valid:
			* Throw an Exception
			* Exit the execution with error // System.exit(1);

	PacketSocketHelper.java
		- Unified print packet method
		- Unified received packet method
		- Unified send packet method