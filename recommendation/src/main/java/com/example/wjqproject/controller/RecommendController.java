package com.example.wjqproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
public class RecommendController {
    //Skin_type[] types = new Skin_type[6];
    String[] types;
    public RecommendController() {
        String type1 = "very light skin\n" +
                "UV radiation leads to sunburn within 10 minutes, skin doesn't tan";
        String type2 = "light skin\n" +
                "UV radiation leads to sunburn within 20 minutes, skin hardly tans or tans only moderately";
        String type3 = "light or light brown skin\n" +
                "UV radiation leads to sunburn within 30 minutes, skin tans easily";
        String type4 = "light brown or olive-colored skin\n" +
                "UV radiation leads to sunburn within 50 minutes, skin soon becomes deeply tanned";
        String type5 = "dark brown skin\n" +
                "UV radiation only leads to sunburn after more than 60 minutes, skin doesn't become darker";
        String type6 = "dark brown or black skin\n" +
                "UV radiation only leads to sunburn after more than 60 minutes, skin doesn't become darker";
        types = new String[]{type1, type2, type3, type4, type5, type6};

    }

    String recommendation1  = "Wear sunglasses on bright days.\n" +
            "If you burn easily, cover up and use broad spectrum SPF 30+ sunscreen.\n" +
            "Watch out for bright surfaces, like sand, water and snow, which reflect UV and increase exposure.";
    String recommendation2  = "Stay in shade near midday when the sun is strongest.\n" +
            "If outdoors, wear protective clothing, a wide-brimmed hat, and UV-blocking sunglasses.\n" +
            "Generously apply broad spectrum SPF 30+ sunscreen every 2 hours, even on cloudy days, and after swimming or sweating. \n" +
            "Watch out for bright surfaces, like sand, water and snow, which reflect UV and increase exposure.";

    String recommendation3 = "Reduce time in the sun between 10 a.m. and 4 p.m.\n" +
            "If outdoors, seek shade and wear protective clothing, a wide-brimmed hat, and UV-blocking sunglasses.\n" +
            "Generously apply broad spectrum SPF 30+ sunscreen every 2 hours, even on cloudy days, and after swimming or sweating. \n" +
            "Watch out for bright surfaces, like sand, water and snow, which reflect UV and increase exposure.";
    String recommendation4 = "Minimize sun exposure between 10 a.m. and 4 p.m.\n" +
            "If outdoors, seek shade and wear protective clothing, a wide-brimmed hat, and UV-blocking sunglasses.\n" +
            "Generously apply broad spectrum SPF 30+ sunscreen every 2 hours, even on cloudy days, and after swimming or sweating. \n" +
            "Watch out for bright surfaces, like sand, water and snow, which reflect UV and increase exposure.";

    String recommendation5 = "Try to avoid sun exposure between 10 a.m. and 4 p.m.\n" +
            "If outdoors, seek shade and wear protective clothing, a wide-brimmed hat, and UV-blocking sunglasses.\n" +
            "Generously apply broad spectrum SPF 30+ sunscreen every 2 hours, even on cloudy days, and after swimming or sweating.\n" +
            "Watch out for bright surfaces, like sand, water and snow, which reflect UV and increase exposure.";
    String[] arr = {recommendation1, recommendation2, recommendation3, recommendation4, recommendation5};

    int uvType = 0;
    @PostMapping("/getUV")
    public String getUV(@RequestParam(value = "uv",defaultValue="") Integer uv){

        System.out.println( "UV=" + uv);
        int type = 0;
        if(uv >=0 && uv <=2){
            type = 0;
        }else if(uv >=3 && uv <=5){
            type = 1;
        }else if(uv >=6 && uv <=7){
            type = 2;
        }
        uvType = type;
        return "Set up the success";

    }
    //http://localhost:8080/getRecommendProject?longitude=12&latitude=34
    @GetMapping("/getRecommendProject")
    public String getRecommendProject(@RequestParam(value = "skinColor",defaultValue="") String skinColor) throws IOException {
        System.out.println( "skinColor=" + skinColor);
        if(skinColor.length() < 2){
            return "please input the skin tones";
        }
        System.out.println(skinColor);
        //todo-调用api 获取紫外线 uv
//        String url = "http://localhost:8080/getTime?skinColor=1&hairColor=2&eyeColor=3";
//        String res = HttpClientHelper.sendGet(url);
//        System.out.println("Res" + res);
        //int uv = 2; //uv值
        //todo-uv 查数据库 获取推荐内容
//        int type = 0;
//        if(uv >=0 && uv <=2){
//            type = 0;
//        }else if(uv >=3 && uv <=5){
//            type = 1;
//        }else if(uv >=6 && uv <=7){
//            type = 2;
//        }
        //step3根据skinColor获取建议
        int skin_type = 0;
        for (int i = 0; i < types.length; i++) {
            String[] split = types[i].split("\n");
            if( split[0].indexOf(skinColor) != -1){
                skin_type = i;
            }
        }
        String resultTime = types[skin_type].split("\n")[1];
        String content = resultTime + '\n' + arr[uvType];
        return content;
    }
}
