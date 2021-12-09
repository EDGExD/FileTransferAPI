
This API transfer files from SFTP do SFTP. For e.g. it can transfer files between two users in one server,
 between different path of one user or between two different servers.

This is the required request body format:
 {
     "source":{
         "host":"...",
         "port": "...",
         "login": "...",
         "password": "...",
         "file_name": "..."
     },
     "target":{
         "host": "...",
         "port":"...",
         "login": "...",
         "password": "...",
         "path": "..."
     }
 }
