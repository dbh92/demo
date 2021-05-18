package com.example.demo2.controller;

import com.example.demo2.model.Tutorial;
import com.example.demo2.payload.UploadFileResponse;
import com.example.demo2.repository.TutorialRepository;
import com.example.demo2.service.impl.FileStorageService;
import com.example.demo2.service.TutorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/api")
public class TutorialController {
    @Autowired
    TutorialRepository tutorialRepository;

    @Autowired
    TutorialService tutorialService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/tutorials")
    public String getAllTutorials(@ModelAttribute Tutorial tutorial, Model model) {
        List<Tutorial> tutorialss = tutorialRepository.findAll();
        model.addAttribute("tutorialss", tutorialss);
        return "tutorials";
    }

    @GetMapping("/mutilSearch")
    public String getTutorials(@RequestParam(required = false) String description, Model model, @Param("keyword") String keyword) {
        List<Tutorial> listTutorialBySearch = tutorialService.findTutorialBySearch(keyword);
        model.addAttribute("tutorialss", listTutorialBySearch);
        model.addAttribute("keyword", keyword);
        return "tutorials";
    }

    @GetMapping("/created")
    public String updateTutorial(Model model) {
        Tutorial tutorialData = new Tutorial();
        model.addAttribute("tutorialData", tutorialData);
        return "updated";
    }

    @PostMapping("/save")
    public String createTutorial(@Validated @ModelAttribute("tutorialData") Tutorial tutorialData, BindingResult result, Model model) {
        if (result.hasErrors()) {
//            model.addAttribute("tutorialData", tutorialData);
            System.out.println(result.getFieldErrors());
            return "updated";
        }
//            try {
//                Tutorial tutorialss = tutorialRepository.save(tutorialData);
//                model.addAttribute("tutorialss", tutorialss);
//                return "redirect:/api/tutorials";
//            } catch (Exception e) {
//                e.printStackTrace();
                return "redirect:/api/created";
//            }
    }


    @GetMapping("/update/{id}")
    public String updateTutorial(@PathVariable("id") long id, Model model) {
        Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
        if (tutorialData.isPresent()) {
            model.addAttribute("tutorialData", tutorialData.get());
        } else {
            model.addAttribute("tutorialData", new Tutorial());
        }
        return "updated";
    }

    @GetMapping("/delete/{id}")
    public String deleteTutorial(@PathVariable("id") long id) {
        tutorialRepository.deleteById(id);
        return "redirect:/api/tutorials";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }

    @GetMapping("/uploadPage")
    public String uploadPage(Model model) {
        return "uploadform";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("files") MultipartFile[] files, Model model) {
        List<String> fileNames = fileStorageService.uploadFileService(files);
        model.addAttribute("message", "Files uploaded successfully!");
        model.addAttribute("files", fileNames);
        return "uploadform";
    }

    @GetMapping("/files")
    public String getListFiles(Model model) {
        List<UploadFileResponse> fileInfos = fileStorageService.loadFiles().stream().map(path -> {
            String filename = path.getFileName().toString();
            System.out.println(filename + "         ssssssssssssssssssssssssssssssssssssssss");
            FileChannel fileChannel;
            long fileSize = 0L;
            try {
                fileChannel = FileChannel.open(path);
                fileSize = fileChannel.size();
                System.out.println(fileSize + " bytes");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String fileDownloadUri = MvcUriComponentsBuilder.fromMethodName(TutorialController.class,
                    "downloadFile", path.getFileName().toString()).build().toString();
            String fileType = filename.substring(filename.lastIndexOf(".") + 1);


            return new UploadFileResponse(filename, fileDownloadUri, fileType, fileSize);
        })
                .collect(Collectors.toList());
        model.addAttribute("files", fileInfos);
        return "listfiles";
    }

    /*
     * Download Files
     */
    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource file = fileStorageService.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {

        return findPaginated(1, 5,"description", "asc", model);
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam(value = "pageSize") Integer pageSize,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {

        Page<Tutorial> page = tutorialService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Tutorial> tutorialss = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("tutorialss", tutorialss);
        return "tutorials";
    }
}
