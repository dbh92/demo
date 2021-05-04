//package com.example.demo2.controller;
//
//import com.example.demo2.service.appUserService.AppUserService;
//import com.example.demo2.service.roleService.RoleService;
//import com.example.demo2.service.appUserService.AppUserService;
//import com.example.demo2.service.roleService.RoleService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//
//@Controller
//public class HomeController {
//
//    @Autowired
//    private AppUserService appUserService;
//
//    @Autowired
//    private RoleService roleService;
//
//    @GetMapping("khongcoquyen")
//    public String accessDenied(){
//        return "khongcoquyen";
//    }
//
//    @GetMapping("/")
//    public ModelAndView login1(){
//        ModelAndView modelAndView = new ModelAndView("login");
//        return modelAndView;
//    }
//
//    @GetMapping("/login")
//    public String login(){
//
//        return "login";
//    }

//    @GetMapping("/register")
//    public ModelAndView showRegisterForm(){
//        ModelAndView modelAndView = new ModelAndView("register");
//        modelAndView.addObject("appUser",new AppUser());
//        return modelAndView;
//    }
//
//    @PostMapping("/register")
//    public ModelAndView createNewUser(@Validated @ModelAttribute("appUser") AppUser appUser, BindingResult bindingResult){
//        if (bindingResult.hasFieldErrors()){
//            ModelAndView modelAndView = new ModelAndView("register");
//            return modelAndView;
//        }
//        ModelAndView modelAndView = new ModelAndView("login");
//        Optional<Role> role = roleService.findById((long)2);
//        appUser.setRole(role.get());
//        appUser.setIsActive("true");
//        appUser.setCover("http://res.cloudinary.com/dnulbp9wi/image/upload/v1604209358/pstioavxgnuzkjfcqe48.jpg");
//        appUser.setAvatar("http://res.cloudinary.com/dnulbp9wi/image/upload/v1604211645/me0qfaepnmgmxqfa7mue.jpg");
//        appUserService.save(appUser);
//        modelAndView.addObject("appUser",appUser);
//        return modelAndView;
//    }
//}
