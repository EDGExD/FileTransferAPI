package com.InterviewTask.FileTransferApi;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
public class FileTransferController {

    public final String localDir = "";

    @PostMapping("/request")
    public ResponseEntity postController(@RequestBody Request request) throws IOException {

        ResponseEntity response = validateRequestData(request);
        if(response.getStatusCode() == HttpStatus.BAD_REQUEST) return response;

        SSHClient sshSource = setupSshj(request.source.host, request.source.port, request.source.login,request.source.password);
        SFTPClient sftpSource = sshSource.newSFTPClient();

        String tempFileName = request.source.file_name;

        sftpSource.get(request.source.file_name, localDir + tempFileName);
        sftpSource.close();
        sshSource.disconnect();

        SSHClient sshTarget = setupSshj(request.target.host, request.target.port, request.target.login,request.target.password);
        SFTPClient sftpTarget = sshTarget.newSFTPClient();

        sftpTarget.put(localDir + tempFileName,  request.target.path + request.source.file_name);

        sftpTarget.close();
        sshTarget.disconnect();

        File tempFile = new File(tempFileName);
        if (!tempFile.delete()) {
            System.out.println("Failed to delete tempFile.");
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }


    private SSHClient setupSshj(String host,String port, String username, String password) throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new PromiscuousVerifier());
        client.connect(host, Integer.parseInt(port));
        client.authPassword(username, password);
        return client;
    }

    private ResponseEntity validateRequestData(Request request)
    {
        if(request == null || request.source==null || request.target==null) return ResponseEntity.badRequest().body("Either 'source' or 'target' must be set");
        if(request.source.file_name==null) return ResponseEntity.badRequest().body("'source.file_name' must be set");
        if(request.source.host == null ) return ResponseEntity.badRequest().body("'source.host' must be set");
        if( request.source.port == null) return ResponseEntity.badRequest().body("'source.port' must be set");
        if( request.source.login == null) return ResponseEntity.badRequest().body("'source.login' must be set");
        if( request.source.password == null) return ResponseEntity.badRequest().body("'source.password' must be set");

        if(request.target.path==null) return ResponseEntity.badRequest().body("'target.path' must be set");
        if(request.target.host == null ) return ResponseEntity.badRequest().body("'target.host' must be set");
        if( request.target.port == null) return ResponseEntity.badRequest().body("'target.port' must be set");
        if( request.target.login == null) return ResponseEntity.badRequest().body("'target.login' must be set");
        if( request.target.password == null) return ResponseEntity.badRequest().body("'target.password' must be set");

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
