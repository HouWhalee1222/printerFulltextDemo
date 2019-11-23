package fulltext.print.demo.controller;

import fulltext.print.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView("login");
        return mv;
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("username") String username,
                              @RequestParam("password") String password, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        if (userService.login(username, password)) {
            mv.setViewName("redirect:search");
            session.setAttribute("loginUser", username);
            return mv;
        } else {
            mv.addObject("msg", "No such username or Invalid password");
            mv.setViewName("login");
            return mv;
        }
    }
}
