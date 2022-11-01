package com.sungyeh.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * IndexController
 *
 * @author sungyeh
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index() {
        return "/index";
    }
}
