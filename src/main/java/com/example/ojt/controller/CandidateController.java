package com.example.ojt.controller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.*;

import com.example.ojt.model.dto.request.EduCandidateAddReq;
import com.example.ojt.model.dto.request.UpdateAccountCandidate;
import com.example.ojt.model.dto.response.APIResponse;
import com.example.ojt.model.dto.responsewapper.DataResponse;
import com.example.ojt.model.entity.*;
import com.example.ojt.service.candidate.ICandidateService;
import com.example.ojt.model.entity.EducationCandidate;
import com.example.ojt.service.candidate.ICandidateService;
import com.example.ojt.service.account.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/candidate")
@RequiredArgsConstructor
public class CandidateController {
    private final IAccountService accountService;
    private final ICandidateService candidateService;

    @PutMapping("/update/account")
    public ResponseEntity<?> updateAccount(@Valid @ModelAttribute UpdateAccountCandidate updateAccountCandidate) throws CustomException {
        boolean check = accountService.updateCandidate(updateAccountCandidate);
        if (check) {
            APIResponse response = new APIResponse(200, "Update account success");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            throw new CustomException("Lack of compulsory registration information or invalid information.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    @PostMapping("/education")
    public  ResponseEntity<?> addEducation(@Valid @RequestBody EduCandidateAddReq eduCandidateAddReq) throws CustomException {
        boolean check = candidateService.addEducation(eduCandidateAddReq);
        if (check) {
            APIResponse response = new APIResponse(200, "Create education success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Create Edu fail", HttpStatus.BAD_REQUEST);
        }
    }
   @GetMapping("/education")
    public ResponseEntity<?> getAllEducations(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                          @RequestParam(required = false) String direction,
                                          @RequestParam(defaultValue = "") String search) throws CustomException {
       Page<EducationCandidate> educationCandidates = candidateService.getEducationCandidates(pageable,search,direction);
       APIResponse response = new APIResponse(200, "Get education success");
       return new ResponseEntity<>(new DataResponse<>(response, educationCandidates), HttpStatus.OK);
   }
   @GetMapping("/education/{id}")
    public ResponseEntity<?> getEducationById(@PathVariable Integer id) throws CustomException {
        EducationCandidate educationCandidate = candidateService.getEducationCandidate(id);
        APIResponse response = new APIResponse(200, "Get education success");
        return new ResponseEntity<>(new DataResponse<>(response, educationCandidate), HttpStatus.OK);
   }
   @PutMapping("/education")
    public ResponseEntity<?> updateEducation(@Valid @RequestBody UpdateEduCandidateReq updateEduCandidateReq) throws CustomException {
        boolean check = candidateService.editEducationCandidate(updateEduCandidateReq);
        if (check) {
            APIResponse response = new APIResponse(200, "Update education success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            throw new CustomException("Update education fail", HttpStatus.BAD_REQUEST);
        }
   }
   @DeleteMapping("/education/{id}")
    public ResponseEntity<?> deleteEducation(@PathVariable Integer id) throws CustomException {
        boolean check = candidateService.deleteEducationCandidate(id);
        if (check) {
            APIResponse response = new APIResponse(200, "Delete education success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Delete education fail", HttpStatus.BAD_REQUEST);
        }
   }
   @PostMapping("/experience")
    public ResponseEntity<?> addExperience(@Valid @RequestBody AddExpCandidateReq addExpCandidateReq) throws CustomException {
        boolean check = candidateService.addExp(addExpCandidateReq);
        if (check) {
            APIResponse response = new APIResponse(200, "Create experience success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new CustomException("Create experience fail", HttpStatus.BAD_REQUEST);
   }
   @GetMapping("/experience")
    public ResponseEntity<?> getAllExperience(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                              @RequestParam(required = false) String direction,
                                              @RequestParam(defaultValue = "") String search)  {
        Page<ExperienceCandidate> experienceCandidates = candidateService.getExperienceCandidates(pageable,search,direction);
        APIResponse response = new APIResponse(200, "Get experience success");
        return new ResponseEntity<>(new DataResponse<>(response, experienceCandidates), HttpStatus.OK);
   }
   @GetMapping("/experience/{id}")
    public ResponseEntity<?> getExperienceById(@PathVariable Integer id) throws CustomException {
        ExperienceCandidate experienceCandidate = candidateService.getExperienceCandidate(id);
        APIResponse response = new APIResponse(200, "Get experience success");
        return new ResponseEntity<>(new DataResponse<>(response, experienceCandidate), HttpStatus.OK);
   }
   @PutMapping("/experience")
    public ResponseEntity<?> editExperience(@Valid @RequestBody UpdateExpCandidateReq updateExpCandidateReq) throws CustomException {
        boolean check = candidateService.editExperienceCandidate(updateExpCandidateReq);
        if (check) {
            APIResponse response = new APIResponse(200, "Update experience success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Update experience fail", HttpStatus.BAD_REQUEST);
        }
   }
   @DeleteMapping("/experience/{id}")
    public ResponseEntity<?> deleteExperience(@PathVariable Integer id) throws CustomException {
        boolean check = candidateService.deleteExperienceCandidate(id);
        if (check) {
            APIResponse response = new APIResponse(200, "Delete experience success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Delete experience fail", HttpStatus.BAD_REQUEST);
        }
   }
   @PostMapping("/certificate")
    public ResponseEntity<?> addCertificate(@Valid @RequestBody AddCertificateReq addCertificateReq) throws CustomException {
        boolean check = candidateService.addCertificate(addCertificateReq);
        if (check) {
            APIResponse response = new APIResponse(200, "Add certificate success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Add certificate fail", HttpStatus.BAD_REQUEST);
        }
   }
   @GetMapping("/certificate")
    public ResponseEntity<?> getAllCertificates(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                @RequestParam(required = false) String direction,
                                                @RequestParam(defaultValue = "") String search) throws CustomException {
        Page<CertificateCandidate> certificateCandidates = candidateService.getCertificateCandidates(pageable,search,direction);
        APIResponse response = new APIResponse(200, "Get certificates success");
        return new ResponseEntity<>(new DataResponse<>(response, certificateCandidates), HttpStatus.OK);
   }
   @GetMapping("/certificate/{id}")
    public ResponseEntity<?> getCertificateById(@PathVariable Integer id) throws CustomException {
        CertificateCandidate certificateCandidate = candidateService.getCertificateCandidate(id);
        APIResponse response = new APIResponse(200, "Get certificate success");
        return new ResponseEntity<>(new DataResponse<>(response, certificateCandidate), HttpStatus.OK);
   }
   @DeleteMapping("/certificate/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable Integer id) throws CustomException {
        boolean check = candidateService.deleteCertificate(id);
        if (check) {
            APIResponse response = new APIResponse(200, "Delete certificate success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new CustomException("Delete certificate fail", HttpStatus.BAD_REQUEST);
        }
   }
   @PutMapping("/certificate")
    public ResponseEntity<?> editCertificate(@Valid @RequestBody UpdateCertificateReq updateCertificateReq) throws CustomException {
        boolean check = candidateService.editCertificate(updateCertificateReq);
        if (check) {
            APIResponse response = new APIResponse(200, "Update certificate success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            throw new CustomException("Update certificate fail", HttpStatus.BAD_REQUEST);
        }
   }
   @PostMapping("/project")
    public ResponseEntity<?> addProject(@Valid @RequestBody AddProjectCandidateReq addProjectCandidateReq) throws CustomException {
        boolean check = candidateService.addProject(addProjectCandidateReq);
        if (check) {
            APIResponse response = new APIResponse(200, "Add project success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            throw new CustomException("Add project fail", HttpStatus.BAD_REQUEST);
        }
   };
   @GetMapping("/project")
   public ResponseEntity<?> getProjects(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                        @RequestParam(required = false) String direction,
                                        @RequestParam(defaultValue = "") String search){
       Page<ProjectCandidate> projectCandidates = candidateService.getProjects(pageable,search,direction);
       APIResponse response = new APIResponse(200, "Get projects success");
       return new ResponseEntity<>(new DataResponse<>(response, projectCandidates), HttpStatus.OK);
   }
   @GetMapping("/project/search")
    public ResponseEntity<?> getProjectsBySName(@RequestParam(defaultValue = "") String search) throws CustomException {
       ProjectCandidate projectCandidate = candidateService.findProjectByName(search);
       APIResponse response = new APIResponse(200, "Get projects success");
       return new ResponseEntity<>(new DataResponse<>(response, projectCandidate), HttpStatus.OK);
   }
   @GetMapping("/project/{id}")
    public ResponseEntity<?> getProject(@PathVariable Integer id) throws CustomException {
       ProjectCandidate projectCandidate = candidateService.getProject(id);
       APIResponse response = new APIResponse(200, "Get project success");
       return new ResponseEntity<>(new DataResponse<>(response, projectCandidate), HttpStatus.OK);
   }
   @PutMapping("/project")
    public ResponseEntity<?> editProject(@Valid @RequestBody UpdateProjectReq updateProjectReq) throws CustomException {
       boolean check = candidateService.editProject(updateProjectReq);
       if (check) {
           APIResponse response = new APIResponse(200, "Update project success");
           return new ResponseEntity<>(response, HttpStatus.OK);
       }
       throw new CustomException("Update project fail", HttpStatus.BAD_REQUEST);
   }
   @DeleteMapping("project/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Integer id) throws CustomException {
       boolean check = candidateService.deleteProject(id);
       if (check) {
           APIResponse response = new APIResponse(200, "Delete project success");
           return new ResponseEntity<>(response, HttpStatus.OK);
       } else {
           throw new CustomException("Delete project fail", HttpStatus.BAD_REQUEST);
       }
   }
   @PostMapping("/skill")
    public ResponseEntity<?> addSkill(@Valid@RequestBody AddSkillCandidateReq addSkillCandidateReq) throws CustomException{
       boolean check = candidateService.addSkill(addSkillCandidateReq);
       if (check) {
           APIResponse response = new APIResponse(200, "Add skill success");
           return new ResponseEntity<>(response, HttpStatus.OK);
       } else {
           throw new CustomException("Add skill fail", HttpStatus.BAD_REQUEST);
       }
   }
   @GetMapping("/skill")
    public ResponseEntity<?> getSkills(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                       @RequestParam(required = false) String direction,
                                       @RequestParam(defaultValue = "") String search){
       Page<SkillsCandidate> skillCandidates = candidateService.getSkills(pageable,search,direction);
       APIResponse response = new APIResponse(200, "Get skills success");
       return new ResponseEntity<>(new DataResponse<>(response, skillCandidates), HttpStatus.OK);
   }
   @GetMapping("/skill/{id}")
    public ResponseEntity<?> getSkill(@PathVariable Integer id) throws CustomException {
       SkillsCandidate skillCandidate = candidateService.getSkill(id);
       APIResponse response = new APIResponse(200, "Get skill success");
       return new ResponseEntity<>(new DataResponse<>(response, skillCandidate), HttpStatus.OK);
   }
   @PutMapping("/skill")
    public ResponseEntity<?> editSkill(@Valid @RequestBody UpdateSkillReq updateSkillReq) throws CustomException {
       boolean check = candidateService.updateSkill(updateSkillReq);
       if (check) {
           APIResponse response = new APIResponse(200, "Update skill success");
           return new ResponseEntity<>(response, HttpStatus.OK);
       } else {
           throw new CustomException("Update skill fail", HttpStatus.BAD_REQUEST);
       }
   }
   @DeleteMapping("/skill/{id}")
    public ResponseEntity<?> deleteSkill(@PathVariable Integer id) throws CustomException {
       boolean check = candidateService.deleteSkill(id);
       if (check) {
           APIResponse response = new APIResponse(200, "Delete skill success");
           return new ResponseEntity<>(response, HttpStatus.OK);
       } else {
           throw new CustomException("Delete skill fail", HttpStatus.BAD_REQUEST);
       }
   }
}
