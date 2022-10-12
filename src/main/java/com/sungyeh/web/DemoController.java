package com.sungyeh.web;

import com.sungyeh.bean.DemoObject;
import com.sungyeh.mq.sender.Sender;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * DemoController
 *
 * @author sungyeh
 */
@RestController
public class DemoController {

    @Setter(onMethod_ = @Autowired)
    private Sender sender;

    @RequestMapping("/send/{id}")
    public void index(@PathVariable String id) {
        sender.send(id);
    }

    @RequestMapping("/test")
    public void index() {
        DemoObject demoObject = new DemoObject();
        demoObject.setName("test");
        demoObject.setDateTime(LocalDateTime.now());
        demoObject.setNumber(123);
        sender.send(demoObject);
    }
}
