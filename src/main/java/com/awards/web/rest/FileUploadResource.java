package com.awards.web.rest;

import com.awards.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@RestController
@RequestMapping("/app")
public class FileUploadResource {
    private final Logger log = LoggerFactory.getLogger(FileUploadResource.class);

    @Inject
    private ServletContext servletContext;

    @RequestMapping(value="/rest/upload", method=RequestMethod.POST)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file){
            try {
                byte[] bytes = file.getBytes();
                String webappRoot = servletContext.getRealPath("/");
                String relativeFolder = File.separator + "images" + File.separator;

                String filename = webappRoot + relativeFolder
                        + file.getOriginalFilename();

                File serverFile = new File(filename);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                return new ResponseEntity<String>(HttpStatus.CREATED);
            } catch (Exception e) {
                log.error("Failed to upload file due to {}", e.getMessage(), e);
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

    }

}
