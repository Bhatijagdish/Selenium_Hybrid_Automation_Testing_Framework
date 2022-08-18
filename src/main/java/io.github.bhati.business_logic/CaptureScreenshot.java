package io.github.bhati.business_logic;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.*;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;

public class CaptureScreenshot {
    private static final XWPFDocument doc = new XWPFDocument();
    private static final XWPFParagraph para = doc.createParagraph();
    private static final XWPFRun run = para.createRun();
    private static final BrowserDrive driver = new BrowserDrive();

    public static String isDirExists(String ScreenshotDirName){
        /**
         * Method is created to check if folder exists otherwise,
         * it will create a new folder in the home directory.
         */
        String homeDir = System.getProperty("user.home");
        if (!Paths.get(homeDir, ScreenshotDirName, "screenshots").toFile().exists()) {
            Paths.get(homeDir, ScreenshotDirName, "screenshots").toFile().mkdirs();
        }
        return Paths.get(homeDir, ScreenshotDirName, "screenshots").toString();
    }

    public static void cleanDir(String ScreenshotDirName){
        /**
         * Method will clean the screenshot directory
         * created or available in the home directory.
         * The new set of screenshots will be captured
         * in the next set of tests.
         */
        for (File file: Objects.requireNonNull(new File(isDirExists(ScreenshotDirName)).listFiles()))
            if (file.exists())
                file.delete();
    }

    public static String takeScreenShot(String ScreenshotMessage,
                                        String TestCaseNum,
                                        String ScreenshotDirName) {
        try {
            String screenShotFileName = ScreenshotMessage + "_" + TestCaseNum;
            String absPath = Paths.get(isDirExists(ScreenshotDirName), screenShotFileName + ".png").toString();

            File screenshotPng = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshotPng, new File(absPath));
            System.out.println("Screenshot Captured");
            return screenShotFileName;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void pasteScreenshotInWord(String ScreenshotDirName, String WordFileName){
        String screenshotDir = isDirExists(ScreenshotDirName);
        run.setText(new Date().toString());
        run.addBreak();
        run.addBreak();
        try{
            for(File file: Objects.requireNonNull(new File(screenshotDir).listFiles())){
                String screenshotName = file.getName();
                File screenshotAbsfile = Paths.get(screenshotDir, screenshotName).toFile();
                String screenshotAbsPath = Paths.get(screenshotDir, screenshotName).toString();
                int screenshotFormat = doc.PICTURE_TYPE_PNG;
                run.setText(screenshotName);
                run.addBreak();
                run.addPicture(new FileInputStream(screenshotAbsfile), screenshotFormat,
                        screenshotAbsPath, Units.toEMU(500), Units.toEMU(500));
            }
            String wordFilePath = Paths.get(Paths.get(screenshotDir).getParent().toString(), WordFileName)
                    +"_"+System.currentTimeMillis()+".docx";
            FileOutputStream out = new FileOutputStream(wordFilePath);
            doc.write(out);
            out.close();
            doc.close();
            System.out.println("Word file of screenshots is successfully created.");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
