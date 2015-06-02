package com.xyj.common.tool.upload.spring;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:13-10-29 下午5:48
 */
@Controller
@RequestMapping(value = "/admin/file")
public class FileController {

    FileMeta fileMeta = null;
    // 获取系统分隔符
    String fs = System.getProperties().getProperty(
            "file.separator");

    /**
     * 上传文件 返回json对象配合jquery-fileupload
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    LinkedList<FileMeta> upload(MultipartHttpServletRequest request, HttpServletResponse response) {
        StringBuffer path = new StringBuffer(request.getSession().getServletContext().getRealPath("/"));

        LinkedList<FileMeta> files = new LinkedList<FileMeta>();
        //1.创建Iterator对象
        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf = null;

        //2.遍历获取文件
        while (itr.hasNext()) {

            //2.1 get next MultipartFile
            mpf = request.getFile(itr.next());
           // System.out.println(mpf.getOriginalFilename() + " uploaded! " + files.size());
            String fileName=mpf.getOriginalFilename();
            // 获取上传文件的类型
            String fileExt = fileName.substring(
                    fileName.lastIndexOf(".") + 1, fileName.length());

            //2.2 如果文件大于10个，移除第一个
            if (files.size() >= 10)
                files.pop();

            //2.3 创建文件信息
            fileMeta = new FileMeta();
            fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
            fileMeta.setFileType(mpf.getContentType());

            try {
                fileMeta.setBytes(mpf.getBytes());


                File temp = null;

                Calendar calendar = Calendar.getInstance();
                int YY = calendar.get(Calendar.YEAR);
                int MM = calendar.get(Calendar.MONTH) + 1;
                int DD = calendar.get(Calendar.DATE);
                int HH = calendar.get(Calendar.HOUR);
                int NN = calendar.get(Calendar.MINUTE);
                int SS = calendar.get(Calendar.SECOND);

                String dir=fs+ "earupload" + fs+YY + fs + MM + fs + DD + fs;
                path.append(dir);
                //如果上传文件目录不存在则创建
                File uploadDir = new File(path.toString());

                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // 这里定义一个随机数，便于上传文件不同名
                Random r = new Random();
                String date = new SimpleDateFormat(
                        "yyyyMMddhhmmssSSS").format(calendar
                        .getTime());

                String fname=date + r.nextInt(100) + "." + fileExt;
                String newFileName = path.append(fname).toString();

                fileMeta.setFileName(dir+fname);
                //复制文件到本地硬盘
                FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(newFileName));

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //2.4 添加文件信息
            files.add(fileMeta);
        }
        // 返回结果如下：
        // [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
        return files;
    }


    /**
     * 上传文件 返回json对象配合jquery-fileupload
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/ckupload", method = RequestMethod.POST)
    public void ckupload(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {
        LinkedList<FileMeta> fileMetas=this.upload(request,response);
        String callback = request.getParameter("CKEditorFuncNum");
        FileMeta fileMeta=fileMetas.getLast();
        String out="<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction("+callback+", '"+fileMeta.getFileName()+"','');</script>";
        response.getWriter().write(out);
    }
//    /**
//     * ************************************************
//     * URL: /rest/controller/get/{value}
//     * get(): get file as an attachment
//     *
//     * @param response : passed by the server
//     * @param value    : value from the URL
//     * @return void
//     *         **************************************************
//     */
//    @RequestMapping(value = "/get/{value}", method = RequestMethod.GET)
//    public void get(HttpServletResponse response, @PathVariable String value) {
//
//        FileMeta getFile = files.get(Integer.parseInt(value));
//        try {
//            response.setContentType(getFile.getFileType());
//            response.setHeader("Content-disposition", "attachment; filename=\"" + getFile.getFileName() + "\"");
//            FileCopyUtils.copy(getFile.getBytes(), response.getOutputStream());
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }


}
