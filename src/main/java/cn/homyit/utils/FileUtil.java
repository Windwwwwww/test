package cn.homyit.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/17 20:55
 **/
public class FileUtil {
//    @Value("${file.upload.path}")
//    private static String uploadPath;

    public static String fileUpload(MultipartFile file){
        if(file.isEmpty()){
            System.out.println("图片不存在");
        }
        //获取文件名称
        String fileName= file.getOriginalFilename();
        //获取文件后缀
        String suffixName=fileName.substring(fileName.lastIndexOf("."));
        //随机生成一个文件名
        fileName= UUID.randomUUID()+suffixName;
        //设置上传文件的路径

        String uploadPath="/home/answer/ui/images/";
//        String uploadPath="/D:/test";
        //上传
        File dest=new File(uploadPath,fileName);
        //判断文件路径是否存在，如果不存在 创建
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        }catch (Exception e){
            System.out.println("上传出错"+e.getMessage());
        }
        //设置最终的文件名称信息返回前端

        return "https://img.abmy.online"+"/images/"+fileName;
    }

    //接受多个文件
    public static String fileUpload(MultipartFile[] files){
        int length = files.length;//穿过来的文件长度
//        String[] urls = new String[length];
        StringBuilder sb = new StringBuilder();
        if(files.length == 0){
            System.out.println("图片不存在");
            return null;
        }
        String fileName = null;
        String suffixName = null;
        for (int i = 0; i < length; i++) {
            //获取文件名称
            fileName= files[i].getOriginalFilename();
            //获取文件后缀
            if (fileName != null) {
                suffixName =fileName.substring(fileName.lastIndexOf("."));
            }
            //随机生成一个文件名
            fileName= UUID.randomUUID()+suffixName;
            //设置上传文件的路径
            String uploadPath="/home/answer/ui/images/";
            //上传
            File dest=new File(uploadPath,fileName);
            //判断文件路径是否存在，如果不存在 创建
            if(!dest.getParentFile().exists()){
                dest.getParentFile().mkdirs();
            }
            try {
                files[i].transferTo(dest);
            }catch (Exception e){
                System.out.println("上传出错"+e.getMessage());
            }
            //设置最终的文件名称信息返回前端
            sb.append("https://img.abmy.online" + "/images/").append(fileName).append(",");
        }
        String str = sb.toString();
        return str.substring(0,str.length() - 1);

//        return null;
//        return fileName2;
    }
}
