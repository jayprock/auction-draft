package com.bitbus.auctiondraft;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {

    /**
     * Captures all paths that are not already mapped and are not resources (.* suffix)
     */
    @GetMapping("/**/{path:[^.]*}")
    public String forward() {
        return "forward:/";
    }

}
