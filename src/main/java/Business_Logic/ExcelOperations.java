package Business_Logic;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class ExcelOperations {

    private XSSFWorkbook workbook;
    private FileInputStream file;
    private FileOutputStream fileOut;
    private XSSFSheet sheet;
    private XSSFRow row;
    private XSSFCell cell;
    private final String path;

    public ExcelOperations(String path){
        /*
        This constructor will actually call all the class methods with the associated excel file.
        Don't need to update the excel file location in each method.
         */
        this.path = path;
        try{
            file= new FileInputStream(path);
            workbook = new XSSFWorkbook(file);
            sheet = workbook.getSheetAt(0);
            file.close();
        }catch(Exception e){
            if (file != null){
                try{
                    file.close();
                }catch (IOException ex) {
                    ex.printStackTrace();
                }
            }else{
                System.out.println("Invalid Excel File Path");
            }
            e.printStackTrace();
        }
    }

    public String getCellData(String sheetName, int rowNum, int colNum){
        /*
        This method will extract the data from excel based on the information type
        even if the information grasped by formula. Type of values:
        Alphabetical value
        Numeric value
        Date
         */
        String cellData = "";
        if (rowNum < 0 || rowNum > 1048575) {
            System.out.println("Invalid Row Number given");
            return cellData;
        }
        if (colNum < 0 || colNum > 16383) {
            System.out.println("Invalid Col Number given");
            return cellData;
        }
        int index = workbook.getSheetIndex(sheetName);
        if (index < 0) {
            System.out.println("Invalid Sheet Name");
            return cellData;
        }
        sheet = workbook.getSheetAt(index);
        row = sheet.getRow(rowNum);
        if (row == null)
            return cellData;
        cell = row.getCell(colNum);
        if (cell == null)
            return cellData;
        if (cell.getCellType() != CellType.FORMULA) {
            if (cell.getCellType() == CellType.STRING)
                cellData = cell.getStringCellValue();
            else if (cell.getCellType() == CellType.NUMERIC) {
                cellData = String.valueOf((long) cell.getNumericCellValue());
                if (DateUtil.isCellDateFormatted(cell)) {
                    double date = cell.getNumericCellValue();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(DateUtil.getJavaDate(date));
                    cellData = cal.get(Calendar.MONTH) + 1 + "/" +
                            cal.get(Calendar.DAY_OF_MONTH) + "/" +
                            String.valueOf(cal.get(Calendar.YEAR)).substring(2);
                }
            } else if (cell.getCellType() == CellType.BOOLEAN)
                cellData = String.valueOf(cell.getBooleanCellValue());
            else if (cell.getCellType() == CellType.BLANK)
                return cellData;
        }else{
            cellData = cell.getRawValue();
        }
        return cellData;
    }

    public void closeExcel(){
        /*
        This method will make sure that the file will be closed after the test execution
        if left open during execution.
         */
        try{
            Runtime.getRuntime().exec("cmd /c taskkill /f /im execl.exe");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public int getRowCount(String sheetName){
        /*
        This method will count the row in which the data is available.
        The count range between 0 and 1048575.
         */
        int rowCount = 0;
        int index = workbook.getSheetIndex(sheetName);
        if (index<0) {
            System.out.println("Invalid Sheet Name");
            return rowCount;
        }
        sheet = workbook.getSheetAt(index);
        rowCount = sheet.getLastRowNum()+1;
        return rowCount;
    }

    public int getColCount(String sheetName){
        /*
        This method will count the columns for row 0 only because the data should start from row 0.
        The count range between 0 and 16383.
         */
        int colCount = 0;
        int index = workbook.getSheetIndex(sheetName);
        if (index<0) {
            System.out.println("Invalid Sheet Name");
            return colCount;
        }
        sheet = workbook.getSheetAt(index);
        colCount = sheet.getRow(0).getLastCellNum();
        return colCount;
    }

    public boolean setCellData(String sheetName, int rowNum, int colNum, String data){
        /*
        This method will write/update the excel file with the provided value.
         */
        boolean result = false;
        try {
            if (rowNum < 0 || rowNum > 1048575) {
                System.out.println("Invalid Row Number given");
                return result;
            }
            if (colNum < 0 || colNum > 16383) {
                System.out.println("Invalid Col Number given");
                return result;
            }
            int index = workbook.getSheetIndex(sheetName);
            if (index < 0) {
                System.out.println("Invalid Sheet Name");
                return result;
            }
            sheet = workbook.getSheetAt(index);
            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum);
            if (row == null)
                row = sheet.createRow(rowNum);
            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);
            cell.setCellValue(data);
            if (fileOut != null)
                fileOut.close();
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
            result = true;
        } catch (Exception e){
            try{
                fileOut.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return result;
    }
}
