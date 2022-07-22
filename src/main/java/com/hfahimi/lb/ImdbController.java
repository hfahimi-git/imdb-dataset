package com.hfahimi.lb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/imdb")
public class ImdbController {
    final StatService statService;
    final ImdbService imdbService;

    public ImdbController(StatService statService, ImdbService imdbService) {
        this.statService = statService;
        this.imdbService = imdbService;
    }

    @GetMapping("/titles-with-same-alive-director-writer")
    public Object titlesWithSameAliveDirectorAndWriter(@RequestParam(defaultValue = "1") int page,
                                                       HttpServletRequest request) {
        var result = imdbService.titlesWithSameAliveDirectorAndWriter(page);
        var self = new HashMap<>();
        self.put("self", request.getRequestURL() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
        if (page > 1) {
            self.put("first", request.getRequestURL() + "?page=1");
        }
        if (result.previous() > 0) {
            self.put("previous", request.getRequestURL() + "?page=" + result.previous());
        }
        if (result.next() > 0) {
            self.put("next", request.getRequestURL() + "?page=" + result.next());
        }
        if (result.lastPage() > 0) {
            self.put("last", request.getRequestURL() + "?page=" + result.lastPage());
        }


        Map<Object, Object> output = new HashMap<>();
        output.put("links", self);
        output.put("data", result.result());
        return output;
    }

    @GetMapping("/titles-with-actors")
    public Object titlesWithActors(
            @RequestParam(defaultValue = "") String actor1,
            @RequestParam(defaultValue = "") String actor2,
            HttpServletRequest request) {
        var self = new HashMap<>();
        self.put("self", request.getRequestURL() +
                (request.getQueryString() != null ? "?" + request.getQueryString(): ""));
        Map<Object, Object> output = new HashMap<>();
        output.put("links", self);
        output.put("data", imdbService.titlesWithActors(actor1, actor2));
        return output;
    }

    @GetMapping("/stat")
    public Object stat(HttpServletRequest request) {
        var self = new HashMap<>();
        self.put("self", request.getRequestURL());
        Map<Object, Object> output = new HashMap<>();
        output.put("links", self);
        output.put("data", statService.get());
        return output;
    }
}
