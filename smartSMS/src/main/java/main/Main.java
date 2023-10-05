package main.java.main;

import main.java.ui.LoginPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Smart SMS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);

        LoginPanel loginPanel = new LoginPanel();
        frame.add(loginPanel);

        frame.setVisible(true);
    }
}







/*"C:\Program Files\Java\jdk-1.8\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2023.2\lib\idea_rt.jar=56421:C:\Program Files\JetBrains\IntelliJ IDEA 2023.2\bin"
-Dfile.encoding=UTF-8 -classpath "C:\Program Files\Java\jdk-1.8\jre\lib\charsets.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\deploy.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\ext\access-bridge-64.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\ext\cldrdata.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\ext\dnsns.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\ext\jaccess.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\ext\jfxrt.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\ext\localedata.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\ext\nashorn.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\ext\sunec.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\ext\sunjce_provider.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\ext\sunmscapi.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\ext\sunpkcs11.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\ext\zipfs.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\javaws.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\jce.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\jfr.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\jfxswt.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\jsse.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\management-agent.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\plugin.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\resources.jar;
C:\Program Files\Java\jdk-1.8\jre\lib\rt.jar;
C:\Users\SRI VISHNU JSB\Documents\git\CSE380-Project\smartSMS\out\production\smartSMS;
C:\Users\SRI VISHNU JSB\.m2\repository\junit\junit\4.13.1\junit-4.13.1.jar;
C:\Users\SRI VISHNU JSB\.m2\repository\org\hamcrest\hamcrest-core\1.3\hamcrest-core-1.3.jar" main.java.main.Main
*/