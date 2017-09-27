package net.apanasik.springsecurityapp.controller;

import net.apanasik.springsecurityapp.model.User;
import net.apanasik.springsecurityapp.service.SecurityService;
import net.apanasik.springsecurityapp.service.UserService;
import net.apanasik.springsecurityapp.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;


@Controller
public class UserController {

    private final Object lock = new Object();

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping("/user_manage")
    public String toolBarClick() {
        return "redirect:/welcome";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("users", null);
            modelAndView.setViewName("registration");
            return modelAndView;
        }
        userService.save(userForm);
        securityService.autoLogin(userForm.getUsername(), userForm.getConfirmPassword());
        modelAndView.setViewName("redirect:/welcome");
        modelAndView.addObject("users", userService.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Username or password is incorrect.");
        }

        if (logout != null) {
            model.addAttribute("message", "Logged out successfully.");
        }

        return "login";
    }

    @RequestMapping(value = {"/", "/welcome*"})
    public ModelAndView welcome(@RequestParam(value = "first_name", required = false) String firstName,
                                @RequestParam(value = "last_name", required = false) String lastName) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("welcome");
        modelAndView.addObject("users", userService.findAll());
        if (firstName != null && lastName != null) {
            User newUser = new User();
            newUser.setUsername(firstName + " " + lastName);
            newUser.setPassword(firstName + " " + lastName);
            newUser.setConfirmPassword(firstName + lastName);
            userService.save(newUser);
            securityService.autoLogin(newUser.getUsername(), newUser.getPassword());
        }
        String username = securityService.findLoggedInUsername();
        System.out.println(username);
        if(username != null) {
            User loggedIn = userService.findByUsername(username);
            modelAndView.addObject("accessed", !loggedIn.isBlocked());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(Model model) {
        return "admin";
    }

    @RequestMapping(value = "/blocking", method = RequestMethod.GET)
    public ModelAndView blocking(@ModelAttribute("id") Long id,
                                 @ModelAttribute("user") String user,
                                 @ModelAttribute("do") String steep) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("welcome");
        if (userService.findByUsername(user).isBlocked()) {
            modelAndView.addObject("accessed", false);
        } else {
            modelAndView.addObject("accessed", true);
            modelAndView.addObject("users", userService.updateStatus(id, (!Objects.equals(steep, "unblock"))));
        }
        return modelAndView;
    }

    @RequestMapping(value = "/deleting", method = RequestMethod.GET)
    public ModelAndView deleting(@ModelAttribute("id") Long id,
                                 @ModelAttribute("user") String user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("welcome");
        if (userService.findByUsername(user).isBlocked()) {
           modelAndView.addObject("accessed", false);
        } else {
            userService.delete(id);
            modelAndView.addObject("accessed", true);
            modelAndView.addObject("users", userService.findAll());
        }
        return modelAndView;
    }
}
