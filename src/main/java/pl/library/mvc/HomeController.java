package pl.library.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/elo")
    public String home(){
        return "index";
    }

    @RequestMapping("/security")
    public String security(){
        return "security";
    }
}
