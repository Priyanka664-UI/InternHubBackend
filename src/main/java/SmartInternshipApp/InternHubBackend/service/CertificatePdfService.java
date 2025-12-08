package SmartInternshipApp.InternHubBackend.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@Service
public class CertificatePdfService {
    
    public byte[] generateCertificatePdf(String studentName, String internshipTitle, 
                                         String companyName, String certificateNumber, 
                                         LocalDateTime completionDate) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            DeviceRgb red = new DeviceRgb(231, 76, 60);
            DeviceRgb green = new DeviceRgb(39, 174, 96);
            
            document.add(new Paragraph("CERTIFICATE OF ACHIEVEMENT")
                .setFontSize(32)
                .setBold()
                .setFontColor(red)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(80));
            
            document.add(new Paragraph("This is to certify that")
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(30));
            
            document.add(new Paragraph(studentName.toUpperCase())
                .setFontSize(28)
                .setBold()
                .setFontColor(green)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20));
            
            document.add(new Paragraph("has successfully completed the internship program")
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20));
            
            document.add(new Paragraph(internshipTitle)
                .setFontSize(20)
                .setBold()
                .setFontColor(red)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(15));
            
            document.add(new Paragraph("at " + companyName)
                .setFontSize(18)
                .setBold()
                .setFontColor(green)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10));
            
            String dateStr = completionDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
            document.add(new Paragraph("Issue Date: " + dateStr)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(40));
            
            document.add(new Paragraph("Certificate No: " + certificateNumber)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10)
                .setFontColor(ColorConstants.GRAY));
            
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate certificate PDF", e);
        }
    }
}
