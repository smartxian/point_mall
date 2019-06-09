package come.point.mall.pointmallbackend.controller.backend;

import come.point.mall.pointmallbackend.common.ResponseCode;
import come.point.mall.pointmallbackend.common.ServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Controller
@RequestMapping("/file")
public class FileController {
    @RequestMapping("/uploadImg.do")
    public ServerResponse<String> uploadPicture(@RequestParam(value = "file", required = false) MultipartFile file) {
        File targetFile;
        System.out.println(file);
        String fileName = file.getOriginalFilename();//获取文件名加后缀
        if (fileName != null && fileName != "") {
            String path = "/Users/shurui/study/nginx";
            String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());//文件后缀
            fileName = new Date().getTime() + "_" + new Random().nextInt(1000) + fileF;//新的文件名

            //先判断文件是否存在
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String fileAdd = sdf.format(new Date());
            //获取文件夹路径
            File file1 = new File(path + "/" + fileAdd);
            //如果文件夹不存在则创建
            if (!file1.exists() && !file1.isDirectory()) {
                file1.mkdir();
            }
            //将图片存入文件夹
            targetFile = new File(file1, fileName);
            try {
                //将上传的文件写到服务器上指定的文件。
                file.transferTo(targetFile);
                return ServerResponse.createBySuccess(fileAdd + "/" + fileName);

            } catch (Exception e) {
               return ServerResponse.createByErrorCodeMessage(ResponseCode.UPLOAD_IMG_FAIL.getCode(),ResponseCode.UPLOAD_IMG_FAIL.getDesc());
            }
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.UPLOAD_IMG_FAIL.getCode(),ResponseCode.UPLOAD_IMG_FAIL.getDesc());
    }
}