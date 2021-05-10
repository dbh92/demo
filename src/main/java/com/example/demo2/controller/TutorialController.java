package com.example.demo2.controller;

import com.example.demo2.model.Tutorial;
import com.example.demo2.payload.UploadFileResponse;
import com.example.demo2.repository.TutorialRepository;
import com.example.demo2.service.fileStorageService.FileStorageService;
import com.example.demo2.service.tutorialService.TutorialService;
import com.example.demo2.model.Tutorial;
import com.example.demo2.repository.TutorialRepository;
import com.example.demo2.service.tutorialService.TutorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
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
    public String getAllTutorials(@ModelAttribute Tutorial tutorial, Model model) throws Exception {

        List<Tutorial> tutorialss = new ArrayList<Tutorial>();
        tutorialss = tutorialRepository.findAll();
        model.addAttribute("tutorialss", tutorialss);
        return "tutorials";
    }

    @GetMapping("/tutorials/search")
    public String search(
            @RequestParam(required = false) long id,
            @RequestParam(required = false) String description,
            Model model){

        Tutorial tutorialss = tutorialRepository.findById(id).orElse(new Tutorial());
        model.addAttribute("tutorialss", tutorialss);
        return "tutorials";
    }

    @PostMapping("/tutorials/description")
    public String getTutorialById(@RequestParam String description, Model model) {
        Tutorial tutorialss = tutorialRepository.findByDescription(description);
        model.addAttribute("tutorialss", tutorialss);
        return "tutorials";
    }

    @GetMapping("/tutorials/mutilSearch")
    public String getTutorials(@RequestParam (required = false) String description, @RequestParam (required = false) Long id, Model model) {
//        long idL = 0;
//        try{
//            idL = Long.parseLong(id);
//        }catch (Exception e) {
//            idL = 0;
//        }
        List<Tutorial> tutorialss = tutorialService.findTutorialBySearch(description, id);
        model.addAttribute("tutorialss", tutorialss);
        return "tutorials";
    }

    @GetMapping("/tutorials/created")
    public String updateTutorial(@ModelAttribute Tutorial tutorial, Model model) {
        Tutorial tutorialNew = new Tutorial();
        model.addAttribute("tutorialData", tutorialNew);
        return "updated";
    }

    @PostMapping("/tutorials/save")
    public String createTutorial(@ModelAttribute Tutorial tutorial, Model model) {
        Tutorial tutorial1 = tutorialRepository.save(tutorial);
        model.addAttribute("tutorial1", tutorial1);
        return "redirect:/api/tutorials";
    }

    @GetMapping("/tutorials/update/{id}")
    public String updateTutorial(@PathVariable("id") long id, Model model) {
        Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
        if (tutorialData.isPresent()) {
            model.addAttribute("tutorialData", tutorialData.get());
        } else {
            model.addAttribute("tutorialData", new Tutorial());
            model.addAttribute("fail", "false");
        }
        return "updated";
    }

    @GetMapping("/tutorials/delete/{id}")
    public String deleteTutorial(@PathVariable("id") long id) {
        tutorialRepository.deleteById(id);
        return "redirect:/api/tutorials";
    }

    @DeleteMapping("/tutorials/delete/all")
    @ResponseBody
    public ResponseEntity<HttpStatus> deleteAllTutorials() {
        try {
            tutorialRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/published")
    @ResponseBody
    public ResponseEntity<List<Tutorial>> findByPublished() {
        try {
            List<Tutorial> tutorials = tutorialRepository.findByPublished(true);

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/403")
    public String accessDenied(){
        return "403";
    }

    @GetMapping("/uploadPage")
    public String uploadPage(Model model) {
        return "uploadform";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("files") MultipartFile[] files, Model model) {
        List<String> fileNames = null;
        try {
            fileNames = Arrays.asList(files)
                    .stream()
                    .map(file -> {
                        fileStorageService.store(file);
                        return file.getOriginalFilename();
                    })
                    .collect(Collectors.toList());
            model.addAttribute("message", "Files uploaded successfully!");
            model.addAttribute("files", fileNames);
        } catch (Exception e) {
            model.addAttribute("message", "Fail!");
            model.addAttribute("files", fileNames);
        }
        return "uploadform";
    }

    @GetMapping("/files")
    public String getListFiles(Model model) {
        List<UploadFileResponse> fileInfos = fileStorageService.loadFiles().map(
                path -> {
                    String filename = path.getFileName().toString();
                    String fileDownloadUri = MvcUriComponentsBuilder.fromMethodName(TutorialController.class,
                            "downloadFile", path.getFileName().toString()).build().toString();
                    String fileType = filename.substring(filename.lastIndexOf(".") + 1);
                    Long size = path.getFileName().spliterator().getExactSizeIfKnown();
                    return new UploadFileResponse(filename, fileDownloadUri, fileType, size);
                }
        )
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

        return findPaginated(1, "description", "asc", model);
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = 5;

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
