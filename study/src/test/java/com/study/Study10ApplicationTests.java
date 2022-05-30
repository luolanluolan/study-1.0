package com.study;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : luolan
 * @Date: 2022-04-02 16:31
 * @Description :
 */
@Slf4j
@SpringBootTest
public class Study10ApplicationTests {
    @Test
    public void contextLoads(){
        for (int i = 0; i < 10; i++) {
            new MyThread(i).start();
        }
    }



}

class MyThread extends Thread{
    ThreadLocal threadLocal = new ThreadLocal();

    public MyThread(int var1){
        threadLocal.set(var1);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(6000);
        }catch (Exception e){

        }
        System.out.println(Thread.currentThread().getName()+"---------"+threadLocal.get());
    }

}