package com.example.excel.demo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.example.excel.demo.entity.User;
import com.example.excel.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author jingLv
 * @date 2021/01/26
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查询所有
     *
     * @param request HttpServletRequest
     * @return 返回页面
     */
    @RequestMapping("list")
    public String findAll(HttpServletRequest request) {
        List<User> users = userService.findAll();
        request.setAttribute("users", users);
        return "index";
    }

    /**
     * 导入Excel文件
     *
     * @param excelFile excel文件
     * @return 返回跳转list页面
     */
    @RequestMapping("import")
    public String importExcel(MultipartFile excelFile) throws Exception {
        log.info("文件名：{}", excelFile.getOriginalFilename());
        // excel导入
        ImportParams params = new ImportParams();
        List<User> users = ExcelImportUtil.importExcel(excelFile.getInputStream(), User.class, params);
        userService.saveAll(users);
        // 上传完成之后跳转到查询所有信息路径
        return "redirect:/user/list";
    }

    /**
     * 导出excel
     *
     * @param response 响应response
     * @throws IOException io异常
     */
    @RequestMapping("export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        // 查询数据库User的所有数据
        List<User> users = userService.findAll();
        // 用户图片的处理
        users.forEach(user -> {
            try {
                Excel excelAnn = user.getClass().getDeclaredField("photo").getAnnotation(Excel.class);
                user.setPhoto(excelAnn.savePath() + '/' + user.getPhoto());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        });
        // 设置response响应头信息
        response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode("用户列表.xls", "UTF-8"));
        // 生成Excel
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("用户列表信息", "用户信息"), User.class, users);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }
}
