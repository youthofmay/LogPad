import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

import javax.swing.*;
import java.io.FileNotFoundException;

public class ExportPDF {
    public ExportPDF(Object[][] data) throws FileNotFoundException {
        var header = new String[] {"Category", "CategoryString", "ComputerName", "EventCode", "EventIdentifier", "EventType", "Logfile", "Message", "RecordNumber", "SourceName", "TimeGenerated", "TimeWritten", "Type", "User"};
        var chooseDirectory = new ChooseDirectory();
        String filePath = chooseDirectory.directory + "\\Query.pdf";
        PdfWriter pdfWriter = new PdfWriter(filePath);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument, PageSize.A4);
        Table table = new Table(new float[]{600f});
        for(int i=0; i<data.length; i++) {
            Table record = new Table(new float[]{80f, 420f});
            record.addCell(new Cell()
                    .add("Log Number")
                    .setBold());
            record.addCell(new Cell()
                    .add(Integer.toString(i+1))
                    .setBold());
            for(int j=0; j<14; j++) {
                record.addCell(new Cell().add(header[j]));
                record.addCell(new Cell().add(data[i][j].toString()));
            }
            table.addCell(new Cell().add(record));
        }
        document.add(table);
        document.close();
        JOptionPane.showMessageDialog(new JOptionPane(), "Successfully exported PDF");
    }
}
