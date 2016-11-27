/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.util;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.*;
import jxl.write.*; 
/**
 *
 * @author Administrateur
 */
public class Excel
{
    private final String place="./web/graph/";
    private String nom;

    public Excel() 
    {
        nom = "test";
        System.out.println("testExcel");
    }

    public String getNom() 
    {
        nom="./web/graph/"+nom;
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    
    public void CreateExcel() throws WriteException, IOException 
    {
        System.out.println("CreateExcel");
         WritableWorkbook workbook = null;
        try 
        {
           File fichier = new File(nom+".xls");
           workbook = Workbook.createWorkbook(fichier);
           System.out.println("CreateExcel"+fichier.getPath());
        } catch (IOException ex) 
        {
            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WritableSheet sheet = workbook.createSheet("First Sheet", 0); 
        
        WritableFont times16font = new WritableFont(WritableFont.TIMES, 16, WritableFont.BOLD, true);
        WritableCellFormat times16format = new WritableCellFormat (times16font); 
        Label label = new Label(3,0, "Times 16 bold italic label", times16format);
        sheet.addCell(label);

        WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 10);
        WritableCellFormat arial10format = new WritableCellFormat (arial10font);
        Label label2 = new Label(1,0, "Arial 10 point label", arial10format);
        sheet.addCell(label2); 

        WritableCellFormat integerFormat = new WritableCellFormat (NumberFormats.INTEGER);
        jxl.write.Number number = new jxl.write.Number(0, 4, 3.141519, integerFormat);
        sheet.addCell(number);
        
        WritableCellFormat floatFormat = new WritableCellFormat (NumberFormats.FLOAT);
        jxl.write.Number number3 = new jxl.write.Number(1, 4, 3.141519, floatFormat);
        sheet.addCell(number3); 
   
        // Get the current date and time from the Calendar object
        Date now = java.util.Calendar.getInstance().getTime();
        DateFormat customDateFormat = new DateFormat ("dd MMM yyyy hh:mm:ss");
        WritableCellFormat dateFormat = new WritableCellFormat (customDateFormat);
        DateTime dateCell = new DateTime(0, 6, now, dateFormat);
        sheet.addCell(dateCell); 

        workbook.write();
        workbook.close();
    }
    
    public void UpadteExcel()
    {
        
    }
    
}
