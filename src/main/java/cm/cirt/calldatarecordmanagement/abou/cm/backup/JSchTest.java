/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.abou.cm.backup;

import cm.cirt.calldatarecordmanagement.metier.Variables;
/**
 *
 * @author antic
 */
import cm.cirt.calldatarecordmanagement.metier.Variables111;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
 
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
 
 
public class JSchTest {
    public static void main(String[] args){
        try{
            String command = "ls -la";
            String host = "197.159.9.34";
            String user = "root";
            String password = Variables111.NEXTTEL_PASSWORD;
             
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);;
            //Set Password
String privateKeyPath = Variables.getIdRsaPath(); // Replace with your actual path
jsch.addIdentity(privateKeyPath);
            //session.setPassword(password);
            session.connect();
             
            Channel channel = session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);
             
            InputStream input = channel.getInputStream();
            channel.connect();
             
            System.out.println("Channel Connected to machine " + host + " server with command: " + command ); 
             
            try{
                try (InputStreamReader inputReader = new InputStreamReader(input)) {
                    BufferedReader bufferedReader = new BufferedReader(inputReader);
                    String line = null;
                    
                    while((line = bufferedReader.readLine()) != null){
                        System.out.println(line);
                    }
                    bufferedReader.close();
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
             
            channel.disconnect();
            session.disconnect();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}