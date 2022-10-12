package com.sungyeh.bean;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DemoObject
 *
 * @author sungyeh
 */
@Data
public class DemoObject implements Serializable {

    private static final long serialVersionUID = 7466610242114149824L;
    private String name;

    private LocalDateTime dateTime;

    private int number;

}
