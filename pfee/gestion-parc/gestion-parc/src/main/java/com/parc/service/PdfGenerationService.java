package com.parc.service;

import com.parc.domain.entity.FeuilleDeRoute;
import com.parc.domain.entity.Mission;
import com.parc.domain.entity.Maintenance;
import com.parc.domain.entity.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PdfGenerationService {

    public byte[] generateFeuilleDeRoutePdf(FeuilleDeRoute feuilleDeRoute) throws Exception {

        if (feuilleDeRoute == null) {
            throw new IllegalArgumentException("FeuilleDeRoute ne peut pas être null");
        }

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            float margin = 50;
            float pageWidth = page.getMediaBox().getWidth();
            float pageHeight = page.getMediaBox().getHeight();
            float yPosition = pageHeight - 100;

            // ================= HEADER =================
            drawHeader(document, contentStream, pageWidth, pageHeight, margin);

            // ================= TITLE =================
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("FEUILLE DE ROUTE");
            contentStream.endText();

            yPosition -= 30;

            // ================= NUMERO =================
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("N°: " + feuilleDeRoute.getNumeroFeuille());
            contentStream.endText();

            yPosition -= 30;

            // ================= TABLE =================
            if (feuilleDeRoute.getMission() != null) {
                yPosition = drawTable(contentStream, feuilleDeRoute.getMission(), margin, yPosition);
            }

            // ================= SIGNATURE =================
            drawSignature(contentStream, margin, yPosition - 40);

            // ================= FOOTER =================
            drawFooter(contentStream, pageWidth, margin);

            contentStream.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);

            return baos.toByteArray();
        }
    }

    public byte[] generateMaintenanceReportPdf(Maintenance maintenance, Utilisateur operateur) throws Exception {
        if (maintenance == null) {
            throw new IllegalArgumentException("Maintenance ne peut pas etre null");
        }

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            float margin = 50;
            float pageWidth = page.getMediaBox().getWidth();
            float pageHeight = page.getMediaBox().getHeight();
            float yPosition = pageHeight - 100;

            drawHeader(document, contentStream, pageWidth, pageHeight, margin);

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("RAPPORT DE MAINTENANCE");
            contentStream.endText();

            yPosition -= 35;
            contentStream.setFont(PDType1Font.HELVETICA, 11);
            yPosition = writeLine(contentStream, margin, yPosition, "ID Maintenance: " + safe(maintenance.getId()));
            yPosition = writeLine(contentStream, margin, yPosition, "Type: " + safe(maintenance.getType()));
            yPosition = writeLine(contentStream, margin, yPosition, "Statut: " + safe(maintenance.getStatut()));
            yPosition = writeLine(contentStream, margin, yPosition, "Date prevue: " + safe(maintenance.getDatePrevue()));
            yPosition = writeLine(contentStream, margin, yPosition, "Date realisation: " + safe(maintenance.getDateRealisation()));
            yPosition = writeLine(contentStream, margin, yPosition, "Cout: " + safe(maintenance.getCout()) + " TND");
            yPosition = writeLine(contentStream, margin, yPosition, "Vehicule: " + (maintenance.getVehicule() != null ? safe(maintenance.getVehicule().getMatricule()) : "N/A"));
            yPosition = writeLine(contentStream, margin, yPosition, "Garage: " + (maintenance.getGarage() != null ? safe(maintenance.getGarage().getAdresse()) : "N/A"));
            yPosition = writeLine(contentStream, margin, yPosition, "Operateur: " + (operateur != null ? (safe(operateur.getNom()) + " " + safe(operateur.getPrenom())).trim() : "N/A"));
            yPosition = writeLine(contentStream, margin, yPosition, "Rapport probleme: " + safe(maintenance.getRapportProbleme()));
            yPosition = writeLine(contentStream, margin, yPosition, "Genere le: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            drawSignature(contentStream, margin, yPosition - 30);
            drawFooter(contentStream, pageWidth, margin);

            contentStream.close();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private float writeLine(PDPageContentStream contentStream, float x, float y, String value) throws Exception {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(value);
        contentStream.endText();
        return y - 18;
    }

    private String safe(Object value) {
        return value == null ? "N/A" : String.valueOf(value);
    }

    // ================= HEADER =================
    private void drawHeader(PDDocument document, PDPageContentStream contentStream,
                            float pageWidth, float pageHeight, float margin) throws Exception {

        // Fond sombre
        contentStream.setNonStrokingColor(0.2f, 0.2f, 0.2f);
        contentStream.fillRect(0, pageHeight - 80, pageWidth, 80);

        // Logo
        try {
            PDImageXObject logo = PDImageXObject.createFromFile(
                    "src/main/resources/agilphoto.png", document
            );
            contentStream.drawImage(logo, margin, pageHeight - 70, 60, 60);
        } catch (Exception e) {
            System.out.println("Logo non trouvé");
        }

        // Texte AGIL
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
        contentStream.setNonStrokingColor(1f, 0.84f, 0f);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 70, pageHeight - 40);
        contentStream.showText("AGIL ENERGY");
        contentStream.endText();

        // Sous-titre
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.setNonStrokingColor(1f, 1f, 1f);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 70, pageHeight - 60);
        contentStream.showText("Gestion du parc automobile");
        contentStream.endText();
    }

    // ================= TABLE =================
    private float drawTable(PDPageContentStream contentStream, Mission m,
                            float margin, float y) throws Exception {

        float rowHeight = 25;
        float col1Width = 150;
        float col2Width = 350;

        String chauffeur = "";
        String numeroPermis = "";
        if (m.getChauffeur() != null) {
            String nom = m.getChauffeur().getNom() != null ? m.getChauffeur().getNom() : "";
            String prenom = m.getChauffeur().getPrenom() != null ? m.getChauffeur().getPrenom() : "";
            chauffeur = (nom + " " + prenom).trim();
            numeroPermis = m.getChauffeur().getNumeroPermis() != null ? m.getChauffeur().getNumeroPermis() : "";
        }

        String vehicule = "";
        if (m.getVehicule() != null && m.getVehicule().getMatricule() != null) {
            vehicule = m.getVehicule().getMatricule();
        }

        String destination = m.getDestination() != null ? m.getDestination() : "";
        String participants = m.getNombreDeParticipants() != null ? m.getNombreDeParticipants().toString() : "";
        String objetLivrer = m.getObjetALivrer() != null ? m.getObjetALivrer() : "";
        String description = m.getDescription() != null ? m.getDescription() : "";

        String[][] data = {
                {"Destination", destination},
                {"Chauffeur", chauffeur},
                {"N° Permis", numeroPermis},
                {"Véhicule", vehicule},
                {"Participants", participants},
                {"Objet à livrer", objetLivrer},
                {"Description", description}
        };

        // Couleur de texte noir
        contentStream.setNonStrokingColor(0, 0, 0);

        for (String[] row : data) {
            // Bordures
            contentStream.setStrokingColor(0, 0, 0);
            contentStream.setLineWidth(1);
            contentStream.addRect(margin, y - rowHeight, col1Width, rowHeight);
            contentStream.addRect(margin + col1Width, y - rowHeight, col2Width, rowHeight);
            contentStream.stroke();

            // Label (colonne 1)
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
            contentStream.setNonStrokingColor(0, 0, 0);
            contentStream.newLineAtOffset(margin + 5, y - rowHeight + 8);
            contentStream.showText(row[0]);
            contentStream.endText();

            // Valeur (colonne 2)
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 11);
            contentStream.setNonStrokingColor(0, 0, 0);
            contentStream.newLineAtOffset(margin + col1Width + 5, y - rowHeight + 8);
            String value = (row[1] != null && !row[1].isEmpty()) ? row[1] : "—";
            contentStream.showText(value);
            contentStream.endText();

            System.out.println("✅ Row: " + row[0] + " = " + (row[1] != null ? row[1] : "NULL"));

            y -= rowHeight;
        }

        return y;
    }

    // ================= SIGNATURE =================
    private void drawSignature(PDPageContentStream contentStream,
                               float margin, float y) throws Exception {

        // Ligne
        contentStream.moveTo(margin, y);
        contentStream.lineTo(margin + 200, y);
        contentStream.stroke();

        // Texte
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.newLineAtOffset(margin, y - 15);
        contentStream.showText("Signature du chauffeur");
        contentStream.endText();
    }

    // ================= FOOTER =================
    private void drawFooter(PDPageContentStream contentStream,
                            float pageWidth, float margin) throws Exception {

        float y = 30;

        contentStream.moveTo(margin, y);
        contentStream.lineTo(pageWidth - margin, y);
        contentStream.stroke();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 8);
        contentStream.newLineAtOffset(margin, y - 15);
        contentStream.showText("© 2026 AGIL ENERGY - Parc automobile");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(pageWidth - 200, y - 15);
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        contentStream.showText("Généré le: " + date);
        contentStream.endText();
    }
}