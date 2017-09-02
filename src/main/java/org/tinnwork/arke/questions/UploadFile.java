package org.tinnwork.arke.questions;

import com.vaadin.server.Page;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import org.tinnwork.arke.MainUI;

import static com.vaadin.ui.UI.getCurrent;

/**
 * Created by fran on 3/07/17.
 */
public class UploadFile  extends CustomComponent implements Upload.SucceededListener,
        Upload.FailedListener, Upload.Receiver {

    protected File tempFile;
    ArrayList<String> list;

    public OutputStream receiveUpload(String filename,
                                      String MIMEType) {
        try {
            tempFile = File.createTempFile("temp", ".csv");
            return new FileOutputStream(tempFile);
        } catch (final java.io.FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void uploadSucceeded(Upload.SucceededEvent event) {
        try {
            Scanner s = new Scanner(new File(tempFile.getAbsolutePath()));
            list = new ArrayList<>();
            while (s.hasNextLine()){
                list.add(s.nextLine());
            }
            addQuestion();

            s.close();

            tempFile.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void uploadFailed(Upload.FailedEvent event) {
    }


    private void addQuestion() {
        MainUI ui = (MainUI) getCurrent();
        Question newQuestion = new Question(null,list.get(0),
                list.get(1), list.get(2),list.get(3),list.get(4),
                false, false, false, true,false,
                " ", 0L);
        try {
            ui.questionService.save(newQuestion);
            Notification notification = new Notification(" preguntas añadidas");
            notification.show(Page.getCurrent());
            setVisible(false);
        } catch (SQLException e) {
        }
    }
}