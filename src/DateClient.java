import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import  java.awt.BorderLayout;
import javax.swing.*;
public class DateClient {
    String serverAddress;
    Scanner in;
    PrintWriter out;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(50);
    JTextArea messageArea = new JTextArea(16,50);

    public DateClient(String serverAddress){
        this.serverAddress = serverAddress;
        textField.setEditable(false);
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(messageArea),BorderLayout.CENTER);
        frame.pack();
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }

private String getName(){
        return JOptionPane.showInputDialog(frame,"Schoose a screen name:","Screen name seletion"
        , JOptionPane.PLAIN_MESSAGE);
}
    private void run() throws IOException{
        try{
            var socket = new Socket(serverAddress,59001);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(),true);
            while (in.hasNextLine()){
                var line = in.nextLine();
                if (line.startsWith("SUBMINAME")){
                    out.println(getName());
                }else if (line.startsWith("NAMEACCEPTED")){
                    this.frame.setTitle("Chatter-"+line.substring(13));
                    textField.setEditable(true);
                }else if(line.startsWith("MESSAGE")){
                    messageArea.append(line.substring(8)+"\n");
                }

            }
        }finally {
            frame.setVisible(false);
            frame.dispose();

        }
    }
    public static void main(String[] args) throws IOException {

            var client = new DateClient("127.0.0.1");
            client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.frame.setVisible(true);
            client.run();
        }
    }

