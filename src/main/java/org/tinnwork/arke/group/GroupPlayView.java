package org.tinnwork.arke.group;

import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.tinnwork.arke.MainUI;

import static com.vaadin.ui.UI.getCurrent;

/**
 * Created by fran on 16/06/17.
 */
public class GroupPlayView extends VerticalLayout {

	private SubjectListForm subjectListForm = new SubjectListForm();
    private PlayersForm playersForm = new PlayersForm();
    private TestForm testForm = new TestForm();
    private ServerSocket serverSocket;
    private ArrayList<Players> playersList = new ArrayList();
    private int pin;
    Label portLabel;
    Button backButton;
    Label countDown = new Label("");
    Label numberOfQuestion = new Label("");
    int count;
    Timer timer= new Timer();
    TimerTask timerTask;

    private String info, infoip, msg;
    private String message = "";

    public GroupPlayView() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        MainUI ui = (MainUI) getCurrent();

        Random random = new Random();
        pin = random.nextInt(900) + 100;

        infoip = getIpAddress();
        playersForm.setWidth("100%");
        subjectListForm.setWidth("100%");
        ui.setPollInterval(900);

        subjectListForm.subjects.addItemClickListener(event -> subjectListForm.selectSubject(event.getItem()));
        subjectListForm.lessons.addItemClickListener(event -> subjectListForm.selectLesson(event.getItem()));
        subjectListForm.startButton.addClickListener(event -> startTest());


        count=Integer.valueOf(subjectListForm.time.getValue());
        countDown.setValue(subjectListForm.time.getValue());
        numberOfQuestion.setValue(testForm.questionId+1+"/"+subjectListForm.numberOfQuestions.getValue());

        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }




    /*****************************************/
    private void buildLayout() {
        setSizeFull();
        addStyleName("backColorBlue");

        Label ip = new Label(infoip);
        ip.setStyleName(ValoTheme.LABEL_NO_MARGIN);

        Component topBar = buildTopBar();
        addComponent(topBar);
        setComponentAlignment(topBar,Alignment.TOP_CENTER);
        setExpandRatio(topBar,1.3f);

        HorizontalLayout mainLayout = new HorizontalLayout(playersForm, subjectListForm,testForm);
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        addComponent(mainLayout);

        testForm.setVisible(false);
        backButton.setVisible(false);
        countDown.setVisible(false);
        numberOfQuestion.setVisible(false);

        setExpandRatio(mainLayout, 15.0f);
        addComponents(ip);
    }

    private Component buildTopBar() {
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setSizeFull();
        Component leftItems = buildLeftItems();
        topBar.addComponent(leftItems);
        topBar.setComponentAlignment(leftItems,Alignment.MIDDLE_LEFT);
        topBar.setExpandRatio(leftItems,1.0f);


        portLabel = new Label("PIN:"+String.valueOf(pin));
        portLabel.addStyleName("pin");
        portLabel.addStyleName(ValoTheme.LABEL_HUGE);
        topBar.addComponent(portLabel);
        topBar.setComponentAlignment(portLabel,Alignment.MIDDLE_RIGHT);
        topBar.setExpandRatio(portLabel,10.0f);

        countDown.addStyleName("time");
        topBar.addComponent(countDown);
        topBar.setComponentAlignment(countDown,Alignment.MIDDLE_RIGHT);
        topBar.setExpandRatio(countDown,0.08f);

        numberOfQuestion.addStyleName("numberofquestion");
        topBar.addComponent(numberOfQuestion);
        topBar.setComponentAlignment(numberOfQuestion,Alignment.MIDDLE_RIGHT);
        topBar.setExpandRatio(numberOfQuestion,0.08f);

        backButton = new Button("back", this::onBackToSettings);
        backButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        backButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        backButton.setIcon(new ThemeResource("img/back2.png"));
        backButton.setWidth(85,Unit.PIXELS);
        topBar.addComponent(backButton);
        topBar.setComponentAlignment(backButton,Alignment.MIDDLE_RIGHT);
        topBar.setExpandRatio(backButton,0.08f);

        return topBar;
    }

    private Component buildLeftItems() {
        HorizontalLayout leftItems = new HorizontalLayout();
        Component mainButton = buildMainButton();
        leftItems.addComponents(mainButton);
        return leftItems;
    }

    private Component buildMainButton() {
        final Button mainButton = new Button("Menú", this::onBackToMenu);
        mainButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        mainButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        mainButton.setIcon(new ThemeResource("img/logo-small-blue.png"));
        mainButton.setWidth(115,Unit.PIXELS);
        return mainButton;
    }

    private void onBackToMenu(Button.ClickEvent event){
        MainUI ui = (MainUI) UI.getCurrent();
        ui.showMainMenu();
    }

    private void onBackToSettings(Button.ClickEvent event){
        subjectListForm.setVisible(true);
        playersForm.setVisible(true);
        portLabel.setVisible(true);
        testForm.setVisible(false);
        backButton.setVisible(false);
        countDown.setVisible(false);
        numberOfQuestion.setVisible(false);
        testForm.circle.setVisible(false);
        testForm.square.setVisible(false);
        testForm.triangle.setVisible(false);
        testForm.diamond.setVisible(false);
        testForm.answer1.setVisible(false);
        testForm.answer2.setVisible(false);
        testForm.answer3.setVisible(false);
        testForm.answer4.setVisible(false);

        timer.cancel();
        timer.purge();
        count=Integer.valueOf(subjectListForm.time.getValue());
        countDown.setValue(subjectListForm.time.getValue());

    }
    /*****************************************/




    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }


    private class SocketServerThread extends Thread {
        int count = 0;
        final int SocketServerPORT = 8000+ pin;

        @Override
        public void run() {
            Socket socket = null;
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;

            try {
                serverSocket = new ServerSocket(SocketServerPORT);

                while (true) {
                    socket = serverSocket.accept();
                    dataInputStream = new DataInputStream(
                            socket.getInputStream());
                    dataOutputStream = new DataOutputStream(
                            socket.getOutputStream());

                    final String messageFromClient = dataInputStream.readUTF();

                    count++;
                    message += "#" + count + " from " + socket.getInetAddress()
                            + ":" + socket.getPort() + "\n"
                            + "Msg from client: " + messageFromClient + "\n";

                    Players player = new Players();
                    player.setId(count);
                    player.setPlayerName(messageFromClient);
                    playersList.add(player);
                    playersForm.players.setItems(playersList);
                    if (playersList.size() == 0) {
                        playersForm.numberOfPlayers.setValue("Ningún jugador se ha unido a esta partida");
                    } else {
                        subjectListForm.startButton.setEnabled(true);
                        if (playersList.size() == 1) {
                            playersForm.numberOfPlayers.setValue("1 jugador");
                        } else {
                            playersForm.numberOfPlayers.setValue(String.valueOf(playersList.size()) + " jugadores");
                        }
                    }
                    String msgReply = "Hello from Android, you are #" + count;
                    dataOutputStream.writeUTF(msgReply);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                final String errMsg = e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void startTest(){
        try {
            Integer.valueOf(subjectListForm.time.getValue());
            Integer.valueOf(subjectListForm.numberOfQuestions.getValue());

            subjectListForm.setVisible(false);
            playersForm.setVisible(false);
            portLabel.setVisible(false);
            testForm.setVisible(true);
            backButton.setVisible(true);
            countDown.setVisible(true);
            numberOfQuestion.setVisible(true);
            testForm.startTest();
            startCountDown();
        } catch (NumberFormatException e) {
            Notification notification = new Notification("Campos \"Tiempo\" y \"Nº de preguntas\" deben ser un número");
            notification.show(Page.getCurrent());
        }
    }

    public void startCountDown(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (count >= 0)
                        countDown.setValue("" + count--);

                    if (count==0) {
                        testForm.answer2.setVisible(false);
                        testForm.square.setVisible(false);
                        testForm.answer1.setVisible(false);
                        testForm.circle.setVisible(false);
                        testForm.answer4.setVisible(false);
                        testForm.diamond.setVisible(false);
                    }
                } catch (Exception e) {
                }

            }
        };
        timer.schedule(timerTask, 0, 1000);
    }
}