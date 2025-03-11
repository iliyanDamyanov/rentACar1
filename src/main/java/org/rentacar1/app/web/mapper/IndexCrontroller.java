package org.rentacar1.app.web.mapper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexCrontroller {

    @GetMapping("/")
    public String indexPage() {

        return "index";
    }

}
