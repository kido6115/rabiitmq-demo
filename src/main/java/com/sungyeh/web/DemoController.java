package com.sungyeh.web;

import com.sungyeh.mq.sender.Sender;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DemoController
 *
 * @author sungyeh
 */
@RestController
public class DemoController {

    @Setter(onMethod_ = @Autowired)
    private Sender sender;

    @RequestMapping("/{id}")
    public void index(@PathVariable String id) {
        sender.send(id);
    }
}
