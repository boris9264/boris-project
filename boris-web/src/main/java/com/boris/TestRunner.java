package com.boris;

import com.boris.spring.chapter2.CompactDisc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-main.xml", "classpath:spring/springmvc.xml" })
public class TestRunner {

    @Autowired
    @Qualifier("sgt")
    private CompactDisc sgt;

    @Autowired
    @Qualifier("sgt")
    private CompactDisc cd;

    @Test
    public void cdShouldNotBeNull() {
        cd.play();
        sgt.play();
    }

}
