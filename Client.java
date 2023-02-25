import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client {
    static JFrame chatWindow = new JFrame("Chat Application");
    static JTextArea chatArea = new JTextArea(22,40);
    static JTextField textField = new JTextField(40);
    static JLabel blankLabel = new JLabel("     ");
    static JButton sendButton = new JButton("Send");
    static  BufferedReader in;
    static  PrintWriter out;

    Client()
    {
        chatWindow.setLayout(new FlowLayout());
        chatWindow.add(new JScrollPane(chatArea));
        chatWindow.add(blankLabel);
        chatWindow.add(textField);
        chatWindow.add(sendButton);

        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow.setSize(475, 500);
        chatWindow.setVisible(true);

        textField.setEditable(false);
        chatArea.setEditable(false);

        sendButton.addActionListener(new Listener());
        textField.addActionListener(new Listener());
    }
    void startChat() throws Exception{
        String ipAddress = JOptionPane.showInputDialog(
                chatWindow,
                "Enter IP Address",
                "IP Address required",
                JOptionPane.PLAIN_MESSAGE
        );
        Socket soc = new Socket(ipAddress, 9806);
        in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        out = new PrintWriter(soc.getOutputStream(), true);

        while (true){
            String str = in.readLine();
            if(str.equals("NAMEREQUIRED")){
                String name = JOptionPane.showInputDialog(
                        chatWindow,
                        "Enter an unique name",
                        "Name required",
                        JOptionPane.PLAIN_MESSAGE
                );
                out.println(name);
            } else if(str.equals("NAMEALREADYEXISTS")){
                String name = JOptionPane.showInputDialog(
                        chatWindow,
                        "Enter another name",
                        "Name already exists",
                        JOptionPane.WARNING_MESSAGE
                );
                out.println(name);
            } else if(str.equals("NAMEACCEPTED")){
                textField.setEditable(true);
            }else{
                chatArea.append(str + '\n');
            }

        }
    }
    public static void main(String[] args) throws Exception{
        Client client = new Client();
        client.startChat();
    }
}

class Listener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
        Client.out.println(Client.textField.getText());
        Client.textField.setText("");
    }
}