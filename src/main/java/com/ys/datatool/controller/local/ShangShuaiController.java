package com.ys.datatool.controller.local;

import com.ys.datatool.service.local.ShangShuaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by mo on @date  2018/4/14.
 *
 * 商帅系统
 */
@RestController
@RequestMapping("/shangshuai")
public class ShangShuaiController {

    @Autowired
    private ShangShuaiService shangShuaiService;

    @RequestMapping("/toShangShuaiExcel")
    public void exportExcel(HttpServletResponse response) throws Exception{
        //shangShuaiService.exportData(response);
    }

}
