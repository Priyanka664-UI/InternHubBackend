package SmartInternshipApp.InternHubBackend.controller;

import SmartInternshipApp.InternHubBackend.entity.InternshipApplication;
import SmartInternshipApp.InternHubBackend.repository.InternshipApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/sync")
public class FileSyncController {

    @Autowired
    private InternshipApplicationRepository applicationRepository;

    @Value("${file.upload.dir}")
    private String uploadDir;

    @GetMapping("/documents")
    public ResponseEntity<List<Map<String, Object>>> getAllDocuments() {
        File folder = new File(uploadDir);
        List<Map<String, Object>> documents = new java.util.ArrayList<>();
        
        if (!folder.exists() || !folder.isDirectory()) {
            return ResponseEntity.ok(documents);
        }

        File[] files = folder.listFiles();
        if (files == null) {
            return ResponseEntity.ok(documents);
        }

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String[] parts = fileName.split("_");
                
                Map<String, Object> doc = new HashMap<>();
                doc.put("fileName", fileName);
                doc.put("size", file.length());
                doc.put("lastModified", file.lastModified());
                
                if (parts.length >= 2) {
                    try {
                        if (parts[0].startsWith("app")) {
                            String appIdStr = parts[0].replace("app", "");
                            doc.put("applicationId", Long.parseLong(appIdStr));
                            doc.put("studentId", null);
                        } else {
                            doc.put("studentId", Long.parseLong(parts[0]));
                            doc.put("applicationId", null);
                        }
                        doc.put("type", parts[1]);
                    } catch (Exception e) {
                        doc.put("applicationId", null);
                        doc.put("studentId", null);
                        doc.put("type", "unknown");
                    }
                }
                
                documents.add(doc);
            }
        }
        
        return ResponseEntity.ok(documents);
    }
}
