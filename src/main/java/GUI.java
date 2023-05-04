import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GUI {
    private static final String DEFAULT_PROTOCOL = "imaps";
    private static final String DEFAULT_HOST = "outlook.com";

    public GUI() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Email to HTML");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        Container container = frame.getContentPane();
        container.setLayout(new GridLayout(7, 2));

        JLabel welcomeLabel = new JLabel("HTML EMAIL ARCHIVAL SYSTEM!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        container.add(welcomeLabel);
        container.add(new JLabel());

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField outputFolderField = new JTextField();
        JTextField emailsPerPageField = new JTextField();

        container.add(new JLabel("Email:"));
        container.add(emailField);
        container.add(new JLabel("Password:"));
        container.add(passwordField);
        container.add(new JLabel("Output Folder:"));
        container.add(outputFolderField);
        container.add(new JLabel("Emails per Page:"));
        container.add(emailsPerPageField);

        JButton browseButton = new JButton("Browse");
        container.add(browseButton);
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFolder = fileChooser.getSelectedFile();
                    outputFolderField.setText(selectedFolder.getAbsolutePath());
                }
            }
        });

        JButton submitButton = new JButton("Submit");
        container.add(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String outputFolder = outputFolderField.getText();
                int emailsPerPage = Integer.parseInt(emailsPerPageField.getText());

                File folder = new File(outputFolder, "HTML-Archive");
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                try {
                    EmailClient client = new EmailClient(DEFAULT_PROTOCOL, DEFAULT_HOST, email, password);
                    int totalEmails = client.getEmailCount();
                    int totalPages = (int) Math.ceil((double) totalEmails / emailsPerPage);

                    for (int i = 1; i <= totalPages; i++) {
                        int startIndex = (i - 1) * emailsPerPage + 1;
                        int endIndex = Math.min(i * emailsPerPage, totalEmails);
                        List<Email> emails = client.getEmails(startIndex, endIndex);


                        FileBuilder.generateHome(folder.getAbsolutePath() + "/index_page_" + i + ".html", emails, totalPages, i);

                        for (Email emailObj : emails) {
                            FileBuilder.generateFiles(folder.getAbsolutePath() + "/mail" + emailObj.hashCode() + ".html", emailObj);
                        }
                    }

                    client.close();
                    FileBuilder.closeIndexWriter();

                    JOptionPane.showMessageDialog(frame, "Thank you for using the HTML EMAIL ARCHIVAL SYSTEM! Your archive will be in your directory of choice, in a folder named: HTML-Archive.");

                    // Clear the entry fields
                    emailField.setText("");
                    passwordField.setText("");
                    outputFolderField.setText("");
                    emailsPerPageField.setText("");

                } catch (IOException | MessagingException c) {
                    c.printStackTrace();
                }
            }
        });

        frame.setVisible(true);
    }
}

