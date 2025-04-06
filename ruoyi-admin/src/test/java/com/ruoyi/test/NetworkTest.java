package com.ruoyi.test;

import com.ruoyi.SecurityApplication;
import com.ruoyi.access.service.IAccessCtrlApplHttpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecurityApplication.class)
public class NetworkTest {
    @Autowired
    private IAccessCtrlApplHttpService accessCtrlApplHttpService;

    @Test
    public void testAccess() {
        System.out.println("testAccess:");
        boolean b = accessCtrlApplHttpService.isAccessAllowed();
        System.out.println("b=" + b);
    }
}